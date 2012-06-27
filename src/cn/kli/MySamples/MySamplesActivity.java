package cn.kli.MySamples;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MySamplesActivity extends Activity {
	private final static String ITEM_ID = "id";
	private final static String ITEM_TITLE = "title";
	private final static String ITEM_SUMMARY = "summary";
	ListView mList;
	Context mContext;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        initListView();
    }
	
	private void initListView(){
		mList = (ListView)findViewById(R.id.samples_list);
		mList.setAdapter(getAdapter());
		mList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				HashMap<String,Object> map = (HashMap<String,Object>)mList.getItemAtPosition(position);
				int id = Integer.valueOf(String.valueOf(map.get(ITEM_ID)));
				switch(id){
				case 1:
					mContext.startActivity(new Intent(mContext,DialogAnimationSample.class));
					break;
				case 2:
					mContext.startActivity(new Intent(mContext,SampleOfBroadcastReceiver.class));
				case 3:
					mContext.startActivity(new Intent(mContext,SampleOfVdisk.class));
				case 4:
					mContext.startActivity(new Intent(mContext,SampleOfContactsList.class));
				}
			}
			
		});
	}
	
	private ListAdapter getAdapter(){
		SimpleAdapter sa = new SimpleAdapter(this, createList(), 
							R.layout.main_list_item, 
							new String[]{ITEM_TITLE,ITEM_SUMMARY}, 
							new int[]{R.id.title,R.id.summary});
		return sa;
	}
	
	private ArrayList<HashMap<String,Object>> createList(){
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		list.add(createMap(1,"Dialog","Dialog,单击dialog外不消失"));
		list.add(createMap(2,"BroadCastReceiver","广播接收"));
		list.add(createMap(3,"Vdisk","新浪微盘"));
		list.add(createMap(4,"Contact List","ingenic 拨号查询"));
		return list;
	}
	
	private HashMap<String,Object> createMap(int id,String key,String value){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put(ITEM_ID, id);
		map.put(ITEM_TITLE, key);
		map.put(ITEM_SUMMARY, value);
		return map;
	}
}