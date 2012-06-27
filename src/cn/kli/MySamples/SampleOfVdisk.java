package cn.kli.MySamples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SampleOfVdisk extends Activity implements OnClickListener {
	private Button mAccess;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_vdisk);
		mAccess = (Button)findViewById(R.id.access);
		mAccess.setOnClickListener(this);
//	     StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()        
//         .detectDiskReads()        
//         .detectDiskWrites()        
//         .detectNetwork()   // or .detectAll() for all detectable problems        
//         .penaltyLog()        
//         .build());        
//  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()        
//         .detectLeakedSqlLiteObjects()     
//         .penaltyLog()        
//         .penaltyDeath()        
//         .build());    
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.access:
			Log.i("klilog","time = "+System.currentTimeMillis());
			new Thread(new Runnable(){
				@Override
				public void run() {
//					exec("http://www.baidu.com");
					exec("http://openapi.vdisk.me/?m=auth&a=get_token&account=huabeiyipilang@gmail.com&password=wowangle&appkey=9832210&signature=51d0bb8cff9454b641ea081068462e22&time=1337598185342");					
				}
				
			}).start();
			break;
		}
	}

	private void exec(String cmd){
		try {
			URL url = new URL(cmd);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String s;
			StringBuilder sb = new StringBuilder();
			while((s = reader.readLine())!=null){
				sb.append(s);
			}
			reader.close();
			Log.i("klilog","sb = "+sb);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
