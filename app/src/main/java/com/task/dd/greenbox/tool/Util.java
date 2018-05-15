package com.task.dd.greenbox.tool;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Mr.辉 on 2018/5/9.
 */

public class Util extends FragmentActivity{
	private static Toast toast;

	//所有的Toast必须使用该方法！
	//修复了多次点击导致Toast显示时间过长的BUG，用户体验感极好
	public static void showToast(Context context,
								 String content) {
		if (toast == null) {
			toast = Toast.makeText(context,
					content,
					Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}

	public void ClickLeft(View view) {
		finish();
	}

}
