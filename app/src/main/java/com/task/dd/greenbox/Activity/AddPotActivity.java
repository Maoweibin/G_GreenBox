package com.task.dd.greenbox.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.task.dd.greenbox.MainActivity;
import com.task.dd.greenbox.R;
import com.task.dd.greenbox.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 点击添加花盆的时候加上的activity
 * 需要添加的功能是点击添加图片
 * Created by dd on 2017/4/15.
 */

public class AddPotActivity extends Activity {
	private EditText editText;//植物昵称
	private Button button;//确定按钮
	private ImageView imageView;//头像
	private ImageView imageView_back;//返回
	private String user_id;
	private String pot_id;
	private String pot_name;
	private static final String POT_ID = "POT_ID";
	private static final String USER_ID = "USER_ID";
	private static final String ID_STRING = "id_string";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_pot);
		pot_id = getIntent().getExtras().getString(POT_ID);
		user_id = getIntent().getExtras().getString(USER_ID);

		editText = (EditText) findViewById(R.id.ed_pot_name);
		button = (Button) findViewById(R.id.button_add_pot);
		imageView = (ImageView) findViewById(R.id.iv_pot_image);
		imageView_back = (ImageView) findViewById(R.id.iv_add_pot_back);

		imageView_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击确定后上传
				//获得url后调用http类上传
				//写一个判断 ，返回有数据的话就 结束这个activity
				pot_name = editText.getText().toString().trim();
					Log.e("pot_name",pot_name);
					Log.e("pot_id",pot_id);
					Log.e("user_id",user_id);
				if (pot_name.equals("") ) {
					Util.showToast(getApplicationContext(),"花盆名不能为空");
				}else {
					OkHttpClient client = new OkHttpClient();
					final Request request = new Request.Builder()
							.url("http://srms.telecomlab.cn/ZZX/flower/login/bind?uid=" + pot_id + "&name=" + pot_name + "&parent_id=" + user_id)
							.get()
							.build();
					Call call = client.newCall(request);
					call.enqueue(new Callback() {
						@Override
						public void onFailure(Call call, IOException e) {
							AddPotActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), "注册失败,请检查网络", Toast.LENGTH_SHORT).show();
								}
							});
						}

						@Override
						public void onResponse(Call call, Response response) throws IOException {

							//到底返回了什么？
							String jsonString = response.body().string();
							Log.e("jsonString", jsonString);
							try {
								JSONObject jsonObject = new JSONObject(jsonString);
								int result = Integer.parseInt(jsonObject.getString("status"));
								if (result == 1) {
									AddPotActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {

											Util.showToast(getApplicationContext(),"添加花盆成功！");
											//Intent intent=newIntent(getApplicationContext(), MainActivity.class,user_phone);
											Intent intent = new Intent(getApplicationContext(), MainActivity.class);
											intent.putExtra(ID_STRING, user_id);
											startActivity(intent);
											finish();

										}
									});
								} else if (result == 0) {
									AddPotActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Util.showToast(getApplicationContext(),"花盆已被领走了");
										}
									});
								}
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(getApplicationContext(), "好像出错了~", Toast.LENGTH_SHORT).show();
							}

						}

					});


				}
			}
		});
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.showToast(getApplicationContext(),"功能正在完善~");

			}
		});


		//点击二维码后联网实现了绑定，然后跳转到这里，在这里向服务器提供用户的花盆的昵称。
		//1.实现bean（昵称）上传
		//2.实现点击更换图片，图片的上传。


	}
}
