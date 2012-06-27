package cn.kli.MySamples;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

public class DialogAnimationSample extends Activity {
	Button mShowDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_animation_sample);
		mShowDialog = (Button)findViewById(R.id.show);
		mShowDialog.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				AlertDialog dialog = createDialog();
				dialog.getWindow().setWindowAnimations(android.R.anim.slide_in_left);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
			
		});
	}
	
	private AlertDialog createDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Dialog");
		builder.setMessage("Dialog Animation Test");
		return builder.create();
	}
	
	private Animation createAnimation(){
		Animation anim = new TranslateAnimation(0, 1, 0, 1);
		return anim;
	}
}
