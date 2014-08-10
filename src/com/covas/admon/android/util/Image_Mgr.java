/*
 * Copyright (c) DoRan Communications Co., Ltd. All rights reserved. 
 * author : 신기웅(moai.kiz@gmail.com)
 * This article may not be published, rewritten or redistributed.
 */

package com.covas.admon.android.util;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

//import com.covas.admon.android.util.LRUCache.OnRemovedEntry;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import android.os.Build;
import android.support.v4.util.LruCache;

/** 설명 
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2012. 12. 13.
 * @version 1.0.0
 */
public class Image_Mgr {//implements OnRemovedEntry<String, Bitmap> {
	
	private	String 											localImageDirectory;
	
	//public ConcurrentHashMap<String, Bitmap>			imagePool;
	//public HashMap<String, Bitmap>						imagePool;
	private LruCache<String, Bitmap>						imagePool;
	
	private Context 											context;
	
	private OnImageLoadComplete 						onImageLoadComplete;
	
	public HashMap<String, Integer>					retryMap;
	
	private boolean											isReleased = false;
	
	public interface OnImageLoadComplete{
		void onImageLoaded(String imgName, Bitmap bitmap, Object object);
	}
	
	public void setOnImageLoadCompleteListener(OnImageLoadComplete onImageLoadComplete){
		this.onImageLoadComplete = onImageLoadComplete;
	}
	
	public Image_Mgr(Context context){
		
		this.context = context;
		
		
		initLruCache();
		//imagePool = new ConcurrentHashMap<String, Bitmap>();
		
		
		retryMap = new HashMap<String, Integer>();
		
		localImageDirectory = context.getDir("", 0).getAbsolutePath()+"/prdtImg";
		
		File imgDir = new File(localImageDirectory);
		
		if(imgDir.exists() == false){
			imgDir.mkdirs();
		}
	}
	
	
	private void initLruCache(){
		
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    final int cacheSize = maxMemory / 8;
	    imagePool = new LruCache<String, Bitmap>(cacheSize) {
	        @SuppressLint("NewApi")
			@Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	        	int bytes = 80000;
	        	if( Build.VERSION.SDK_INT < 12 ){
	        		bytes = bitmap.getWidth() * bitmap.getHeight() * 4 / 1024;
	        	}else{
	        		bytes = bitmap.getByteCount() / 1024;
	        	}
	        	
	            return bytes;
	        }
	    };
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	    	imagePool.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return imagePool.get(key);
	}
	
	
	public Bitmap loadImage(final String imageURL, final String imgName, final Object target){
		
		//Logger.d("로드이미지!! = "+imgName);
		
		if(imagePool == null) {
			Logger.d( Image_Mgr.class, "func = loadImage, error = (imagePool == null)" );
			return null;
		}
		
		Bitmap bitmap = getBitmapFromMemCache(imgName);
		
		if( bitmap != null && !bitmap.isRecycled() ){
			//loadImageComplete(imgName, imagePool.get(imgName), target);
			return bitmap;
		}else{
			getImageFileLocal(imageURL, imgName, target);
		}
		
		return null;
	}
	
	private void getImageFileLocal(final String imageURL, final String imgName, final Object target){
		AsyncProgress ap = new AsyncProgress(context);
		ap.setOnRequestComplete(new AsyncProgress.OnRequestComplete() {
			
			@Override
			public Object process(String task, String param) {
				
				Bitmap bitmap = null;
				File file = new File(localImageDirectory, imgName);
				if(file.exists()){
					
					try {
						URL url = new URL(imageURL);
						HttpURLConnection conn = (HttpURLConnection)url.openConnection();
						conn.connect();
						
						long fileSize = conn.getContentLength()*10000L;
						
						if(file.lastModified() == fileSize){
							Logger.d(" 파일사이즈 같음!! "+ imgName + ", " + (fileSize/10000L));
							bitmap = FileUtils.getBitmapFromFile(file);
						}else{
							Logger.d(" 파일사이즈 다름!! "+ imgName + ", "  + (file.lastModified()/10000L)+", "+(fileSize/10000L));
							bitmap = null;
						}
						conn.disconnect();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				return bitmap;
			}
			
			@Override
			public void onRequestComplete(Object result, String task, String param) {
				if(result == null){
					getImageFileWeb(imageURL, imgName, target);
				}else{
					Bitmap bitmap = (Bitmap)result;
					
					if(imagePool == null) return;
					addBitmapToMemoryCache(imgName, bitmap);
					loadImageComplete(imgName, bitmap, target);
				}
			}
		});
		ap.execute("");
	}
	
	
	private void getImageFileWeb(final String imageURL, final String imgName, final Object target){
		
		Log.d("FRENz", "이미지 웹요청!! = "+imgName);
		
		AsyncProgress ap = new AsyncProgress(context);
		ap.setOnRequestComplete(new AsyncProgress.OnRequestComplete() {
			
			@Override
			public Object process(String task, String param) {
				
				Bitmap bitmap = null;
				URL url;
				try {
					url = new URL(imageURL);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.connect();
					
					long fileSize = conn.getContentLength()*10000L;
					
					//bitmap = BitmapFactory.decodeStream(url.openStream());
					bitmap = BitmapFactory.decodeStream(conn.getInputStream());
					File savedFile = FileUtils.saveBitmapToFileCache(bitmap, localImageDirectory, imgName);
					
					//System.out.println( imgName+", 원래값 = "+savedFile.lastModified() );
					
					savedFile.setLastModified(fileSize);
					
					conn.disconnect();
					//System.out.println( imgName+", 수정값 = "+savedFile.lastModified() );
					
				} catch (Exception e) {
					e.printStackTrace();
					bitmap = null;
				}
				
				return bitmap;
			}
			
			@Override
			public void onRequestComplete(Object result, String task, String param) {
				
				if(result == null){
					if(retryMap == null) return;
					if(retryMap.containsKey(imgName)){
						Logger.d("이미지 로드 실패 = "+imgName+", 재시도 끝");
						return;
					}else{
						retryMap.put(imgName, 1);
						Logger.d("이미지 로드 실패 = "+imgName+", 재시도 = "+retryMap.get(imgName));
						getImageFileWeb(imageURL, imgName, target);
					}
					
					
					/*File fromFile = new File(profilePicturePath, imgName);
					if(fromFile.exists()){
						fromFile.delete();
					}*/
				}else{
					Bitmap bitmap = (Bitmap)result;
					
					if(imagePool == null) return;
					addBitmapToMemoryCache(imgName, bitmap);
					loadImageComplete(imgName, bitmap, target);
				}
			}
		});
		ap.execute("");
	}
	
	private void loadImageComplete(String imgName, Bitmap bitmap, final Object target){
		
		if(bitmap == null){
			Logger.d("로드 실패!! = "+imgName);
		}else{
			Logger.d("로드 성공!! = "+imgName);
		}
		
		//addBitmapToMemoryCache(imgName, bitmap);
		
		synchronized(onImageLoadComplete){
			if(onImageLoadComplete != null){
				if(isReleased) return;
				onImageLoadComplete.onImageLoaded(imgName, bitmap, target);
			}
		}
		
	}
	
	public void release(){
		
		isReleased = true;
		
		onImageLoadComplete = null;
		imagePool.evictAll();
		/*for (Iterator<Entry<String, Bitmap>> iter = imagePool.entrySet().iterator(); iter.hasNext();) {
		    Map.Entry<String, Bitmap> entry = (Map.Entry<String, Bitmap>) iter.next();
		    Bitmap bitmap = (Bitmap)entry.getValue();
		    if( bitmap.isRecycled() == false ) bitmap.recycle();
		    bitmap = null;
		}*/
		
		//TODO thread nullpointerException 
		//imagePool.clear();
		imagePool = null;
		retryMap.clear();
		retryMap = null;
	}

	
}
