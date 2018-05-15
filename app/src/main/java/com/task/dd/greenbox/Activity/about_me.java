package com.task.dd.greenbox.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.task.dd.greenbox.R;

/**
 * Created by Mr.辉 on 2018/5/10.
 */

public class about_me extends Activity {
	private ImageView icon_back;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_me);
		icon_back= (ImageView) findViewById(R.id.icon_back);
		icon_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


	}
}
