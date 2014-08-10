package com.covas.admon.android.base;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.covas.admon.android.App;

/**
 * Tabhost구조 안에서 각각의 하위 Activity를 애니메이션 등을 이용해 처리해주는 base클래스 (ActivityGroup)
 * 
 * @author 신기웅(tlsrldnd0418@naver.com)
 * @since 2012. 3. 29.
 * @version 1.0.0
 */
public abstract class FlipperBase extends ActivityGroup {

	protected ViewFlipper flipper;
	protected Animation outToRight;
	protected Animation inFromRight;
	protected Animation outToLeft;
	protected Animation inFromLeft;
	protected float aniMoveValue = 1.0f;
	protected long aniDuration = 300;
	protected Interpolator interpolator;
	
	protected LocalActivityManager lam;
	protected ArrayList<String> viewStack;
	
	protected App app;
	
	protected Button blockBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBase();
		init();
	}
	
	private void setBase(){
		flipper = new ViewFlipper(this);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		flipper.setLayoutParams(lp);
		//flipper = (ViewFlipper) findViewById(R.id.flipper);
		
		setContentView(flipper);
		
		lam = getLocalActivityManager();
		lam.dispatchDestroy(true);
		viewStack = new ArrayList<String>();
		
		//interpolator = new DecelerateInterpolator(0.3f);
		setAnimation();
		
		app = (App)getApplicationContext();
		
		blockBtn = new Button(getBaseContext());
		blockBtn.setBackgroundColor(Color.alpha(0));
		addContentView(blockBtn, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		lockScreen(false);
	}
	
	protected abstract void init();
	
	/**
	 * Intent에 Flag를 자동으로 달아서 리턴해주는 메소드
	 * @param cls
	 * @return
	 */
	public Intent newIntent(Class<?> cls){
		Intent intent = new Intent(getBaseContext(), cls);
		//intent.putExtra("className", cls.getSimpleName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}
	
	/**
	 * 화면상 애니메이션으로 이동하고 있더라도 시스템상에서는 
	 * 애니메이션이 끝나기 전까지는 기존의 뷰가 그자리에 그대로 있기 때문에
	 * 버튼이 여러번 눌려 기능이 중첩되는 경우가 생긴다.<br>
	 * 때문에 리스너가 등록된 clickable 오브젝트를 다 막는것 보다는 애니메이션 시작 시 맨위에 버튼으로 덮었다가
	 * 애니메이션이 끝날 시 버튼을 감춰주는 식으로 화면을 잠궈줌
	 * @param on_off
	 */
	private void lockScreen(boolean on_off){
		if(on_off) blockBtn.setVisibility(View.VISIBLE);
		else blockBtn.setVisibility(View.GONE);
	}
	
	/**
	 * 리스너가 달린 outToRight 애니메이션을 리턴해줌
	 * @return
	 */
	private Animation outToRightAni(){
		Animation outToRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, aniMoveValue,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outToRight.setDuration(aniDuration);
		//outToRight.setInterpolator(interpolator);
		outToRight.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				lockScreen(true);
			}
			@Override
			public void onAnimationRepeat(Animation animation){}
			@Override
			public void onAnimationEnd(Animation animation) {
				removeView();
				lockScreen(false);
			}
		});
		return outToRight;
	}
	/**
	 * 리스너가 달린 outToLeft 애니메이션을 리턴해줌
	 * @return
	 */
	private Animation outToLeftAni(){
		Animation outToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -aniMoveValue,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outToLeft.setDuration(aniDuration);
		//outToLeft.setInterpolator(interpolator);
		outToLeft.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				lockScreen(true);
			}
			@Override
			public void onAnimationRepeat(Animation animation){}
			@Override
			public void onAnimationEnd(Animation animation) {
				lockScreen(false);
			}
		});
		return outToLeft;
	}
	
	/**
	 * 기본 애니메이션을 미리 정의함
	 */
	private void setAnimation(){
		/*outToRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, aniMoveValue,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outToRight.setDuration(aniDuration);
		outToRight.setInterpolator(interpolator);
		outToRight.setAnimationListener(this);*/
		
		inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, aniMoveValue,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(aniDuration);
		//inFromRight.setInterpolator(interpolator);
		//inFromRight.setAnimationListener(this);
		
		/*outToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -aniMoveValue,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outToLeft.setDuration(aniDuration);
		outToLeft.setInterpolator(interpolator);
		outToLeft.setAnimationListener(this);*/
		
		
		inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -aniMoveValue,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(aniDuration);
		//inFromLeft.setInterpolator(interpolator);
		//inFromLeft.setAnimationListener(this);
		
	}

	/**
	 * startActivity의 효과를 줌 (첫페이지 등록시는 애니메이션 처리 안함)
	 * @param intent
	 */
	public void addActivity(Intent intent){
		addActivity(intent, true);
	}
	
	/**
	 * startActivity의 효과를 줌 (첫페이지 등록시는 애니메이션 처리 안함)
	 * @param intent
	 */
	public void addActivity(Intent intent, boolean withAni){
		//intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		String className = intent.getComponent().getShortClassName();
		className = className.substring(className.lastIndexOf(".")+1);
		//toastShow(className);
		viewStack.add(className);
		flipper.addView(lam.startActivity(className, intent).getDecorView());
		//if(flipper.getChildCount() > 1){
		if(viewStack.size() > 1 && withAni){
			flipper.setInAnimation(inFromRight);
			flipper.setOutAnimation(outToLeftAni());
			flipper.showNext();
		}
		onResumeChecker(viewStack.get(viewStack.size()-1));
	}
	
	/**
	 * Activity의 finish와 같은 효과를 줌 (애니메이션과함께 현재 액티비티를 끝냄)
	 */
	public void popActivity(){
		//if(flipper.getChildCount() <= 1){
		
		//제일 첫 페이지일 경우 어플종료 다이얼로그 띄움
		if(viewStack.size() <= 1){
			app.exitAlert(this);
			//this.finish();
			return;
		}
		viewStack.remove(viewStack.size()-1);
		lam.destroyActivity(lam.getCurrentId(), true);
		flipper.setInAnimation(inFromLeft);
		flipper.setOutAnimation(outToRightAni());
		flipper.showPrevious();
		onResumeChecker(viewStack.get(viewStack.size()-1));
	}
	
	public void changeView(){
		
	}
	
	/**
	 * LocalActivityManager의 경우 하위 Activity들이 정상적인 라이프사이클을 타지 않으므로
	 * 부모의 onResume에서 현재Activity가 누군지 감시해서 직접 알려줌
	 * @param clsName
	 */
	public void onResumeChecker(String clsName){}
	
	/**
	 * FIXME : ActivityGroup 의 자식들은 onResume등이 제대로 호출되지 않는 문제가있음
	 * BaseActivity를 상속한 액티비티일 경우 onResume에 상응하는 함수를 호출해 주게 변경 필요
	 */
	@Override
	protected void onResume() {
		super.onResume();
		//lam.getCurrentActivity().onResume();
	}
	
	/**
	 * 정의하지 않을 경우 메인Activity종료로 감지하기 때문에 재정의해서 popActivity()로 돌려줌
	 */
	@Override
	public void onBackPressed() {
		popActivity();
	}
	
	/**
	 * ViewFlipper에서 마지막 뷰를 제거
	 */
	private void removeView(){
		flipper.removeViewAt(flipper.getChildCount()-1);
	}
	
	/**
	 * 한번에 두개를 pop시켜야할 때 쓰이는 메소드
	 * @param num
	 */
	public void popActivity(int num){
		viewStack.remove(viewStack.size()-2);
		lam.destroyActivity(lam.getCurrentId(), true);
		flipper.removeViewAt(flipper.getChildCount()-1+num);
		popActivity();
	}
	
	/**
	 * 모든 뷰를 제거함(초기화 할 경우에 쓰임)
	 */
	public void removeAll(){
		viewStack.clear();
		lam.removeAllActivities();
		flipper.removeAllViews();
	}
	
	/**
	 * Toast를 간단히 띄우게 하는 메소드
	 * @param msg
	 */
	public void toastShow(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}
