package cn.kli.MySamples;

import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SampleOfBroadcastReceiver extends Activity {
	private final static String SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
	private TextView tv;
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if(arg1.getAction().equals(SIM_STATE_CHANGED)){
				Log.i("klilog",SIM_STATE_CHANGED);
				outPutIntentExtras(arg1);
				tv.setText(tv.getText().toString() + "\n" + SIM_STATE_CHANGED);
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.broadcast_receiver_sample);
		tv = (TextView)findViewById(R.id.textView1);
		registerReceiver();
	}
	
	private void registerReceiver(){
		final IntentFilter filter = new IntentFilter();
		filter.addAction(SIM_STATE_CHANGED);
		registerReceiver(receiver, filter);
	}

	private void outPutIntentExtras(Intent intent){
		Bundle bundle = intent.getExtras();
		Set<String> set = bundle.keySet();
		for(String key:set){
			Log.i("klilog","key = "+key + ", value = "+bundle.get(key));
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(receiver);
	}
	
}
