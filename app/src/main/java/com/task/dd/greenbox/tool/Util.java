package com.task.dd.greenbox.tool;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Mr.辉 on 2018/5/9.
 */

public class Util extends FragmentActivity {
	private static Toast toast;

	//所有的Toast必须使用该方法！
	//修复了多次点击导致Toast显示时间过长的BUG，用户体验感极好
	//因为toast的实现需要在activity的主线程才能正常工作，所以传统的非主线程不能使toast显示在actvity上，通过Handler可以使自定义线程运行于Ui主线程。
	public static void showToast(Context context, String content) {

		if (toast == null) {
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();

	}

	public void ClickLeft(View view) {
		finish();
	}

}
