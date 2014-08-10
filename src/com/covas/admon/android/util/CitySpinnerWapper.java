package com.covas.admon.android.util;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CitySpinnerWapper {
	
	public static void init(final Activity activity,
							final Spinner spinnerSiDO, final Spinner spinnerGUGUN,
							String city, String town){
		
		final ArrayList<String> cityArr = CityInfo.getCityArr();
		
		ArrayList<String> townArr = CityInfo.getTownArr(city);
		
		if( townArr == null ){
			city = CityInfo.DEFAULT_CITY;
			town =  CityInfo.DEFAULT_TOWN;
		}
		
		
		setSpinner(activity, spinnerSiDO, cityArr);
    	setSpinner(activity, spinnerGUGUN, CityInfo.getTownArr(city));
        
    	spinnerSiDO.setSelection(cityArr.indexOf(city), true);
    	spinnerGUGUN.setSelection(CityInfo.getTownArr(city).indexOf(town));
    	
    	spinnerSiDO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				setSpinner( activity, spinnerGUGUN, CityInfo.getTownArr(cityArr.get(position)) );
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}
	
	private static void setSpinner(Activity activity, Spinner spinner, ArrayList<String> arrList){
		ArrayAdapter<String> aa = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, arrList);
    	aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner.setAdapter(aa);
    	spinner.setPrompt("");
	}
}
