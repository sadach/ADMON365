/*
 * Copyright (c) DoRan Communications Co., Ltd. All rights reserved. 
 * author : 신기웅(moai.kiz@gmail.com)
 * This article may not be published, rewritten or redistributed.
 */

package com.covas.admon.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * 설명
 * 
 * @author 신기웅(moai.kiz@gmail.com)
 * @since 2012. 5. 25.
 * @version 1.0.0
 */
public class FileUtils {

	public static Bitmap getBitmapFromFilePath(String strFilePath, String fileName) {

		File file = new File(strFilePath, fileName);

		return getBitmapFromFile(file);
	}

	public static Bitmap getBitmapFromFile(File file) {
		Bitmap bitmap = null;

		try {
			if (file.exists() == false) return null;
			
			
			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			
			//TODO OutOfMemory 에러 발생 
			/*FileInputStream inputStream = new FileInputStream(file);

			BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);
			inputStream.close();

			Bitmap bm = bitmapDrawable.getBitmap().copy(Config.ARGB_8888, true);
			bitmapDrawable.getBitmap().recycle();
			bitmap = bm;*/

		}
		catch (Exception e) {
			e.printStackTrace();
			bitmap = null;
		}
		return bitmap;
	}

	public static boolean saveImageToFileCache(InputStream inputStream, String strFilePath, String fileName) {
		boolean flag = true;

		try {
			
			File file = new File(strFilePath, fileName);
			FileOutputStream fileOutput = new FileOutputStream(file);

			FlushedInputStream fis = new FlushedInputStream(inputStream);
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			
			while ((bufferLength = fis.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
			}

			fileOutput.flush();
			fileOutput.close();
			inputStream.close();
			
		}
		catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public static File saveBitmapToFileCache(Bitmap bitmap, String strFilePath, String fileName) {
		File fileCacheItem = new File(strFilePath, fileName);
		
		OutputStream out = null;

		//boolean flag = true;

		try {
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);
			
			if("p".equalsIgnoreCase( String.valueOf(fileName.charAt(fileName.lastIndexOf(".")+1)) )){
				bitmap.compress(CompressFormat.PNG, 100, out);
			}else{
				bitmap.compress(CompressFormat.JPEG, 100, out);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (out != null) out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				//flag = false;
			}
		}
		
		return fileCacheItem;
	}
	
	static class FlushedInputStream extends FilterInputStream {
	    public FlushedInputStream(InputStream inputStream) {
	        super(inputStream);
	    }

	    @Override
	    public long skip(long n) throws IOException {
	        long totalBytesSkipped = 0L;
	        while (totalBytesSkipped < n) {
	            long bytesSkipped = in.skip(n - totalBytesSkipped);
	            if (bytesSkipped == 0L) {
	                  int bytes = read();
	                  if (bytes < 0) {
	                      break;  // we reached EOF
	                  } else {
	                      bytesSkipped = 1; // we read one byte
	                  }
	           }
	            totalBytesSkipped += bytesSkipped;
	        }
	        return totalBytesSkipped;
	    }
	}
	
	public static String getRemoteFileToString(String fileURL) throws MalformedURLException, IOException{
		
		return getRemoteFileString(fileURL, HTTP.UTF_8);
	}
	
	public static String getRemoteFileString(String fileURL, String charset) throws MalformedURLException, IOException{
		
		String result = "";
		
		InputStream is = null;
		InputStreamReader isr = null; 
		is = new URL(fileURL).openStream();
		isr = new InputStreamReader(is, charset);
		StringBuffer sb = new StringBuffer(); int c;
		while ((c = isr.read()) != -1) {sb.append((char) c);}
		isr.close(); is.close();
		
		result = sb.toString();
		
		return result;
	}
}
