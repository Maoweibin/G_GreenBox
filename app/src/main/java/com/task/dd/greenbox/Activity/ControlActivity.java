package com.task.dd.greenbox.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.task.dd.greenbox.R;
import com.task.dd.greenbox.bean.Pot_Message;
import com.task.dd.greenbox.bean.SingleBean;
import com.task.dd.greenbox.jsonpull.PotMessageJson;
import com.task.dd.greenbox.tool.GradientImageView;
import com.task.dd.greenbox.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 花盆的控制
 * Created by dd on 2017/4/22.
 */

public class ControlActivity extends Activity {
	private ImageView imageView;//显示图片
	private ImageView backImageView;//返回的IV

	private ProgressBar pb_temperature;//温度高度条
	private ProgressBar pb_sunshine;//光照高度条
	private ProgressBar pb_humidity;//湿度高度条
	private ProgressBar pb_water;//水高度条
	private ProgressBar pb_soil_humidity;
	private GradientImageView iv_water;//水图标
	private GradientImageView iv_water_back;//水图标背景
	private GradientImageView iv_sunshine;//阳光图片
	private GradientImageView iv_sunshine_back;//阳光背景
	private ImageView iv_recode;//自动图标
	//	private ImageView iv_auto;//自动图标
	private TextView tv_sun;
	private TextView tv_water;
	private TextView tv_humidity;
	private TextView tv_temperature;
	private TextView tv_soil_humidity;
	private TextView potname;
	private Context context;
	private Pot_Message pot_message;
	private String switch_num;
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final String TAG = "MainActivity";
	private SingleBean singlebean;
	private String pot_id;
	private static final String POT_ID = "PotFragment.PotID";
	private String lightglobal = "";
	private String light_first;
	private String water_first;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_control);
		imageView = (ImageView) findViewById(R.id.iv_show);
		backImageView = (ImageView) findViewById(R.id.iv_pot_back);//返回

		pb_temperature = (ProgressBar) findViewById(R.id.progressbar_temperature);
		pb_sunshine = (ProgressBar) findViewById(R.id.progressbar_sun);
		pb_humidity = (ProgressBar) findViewById(R.id.progressbar_humidity);
		pb_water = (ProgressBar) findViewById(R.id.progressbar_water);
		pb_soil_humidity = (ProgressBar) findViewById(R.id.progressbar_soil);
		iv_water = (GradientImageView) findViewById(R.id.iv_water_button);//水按钮
		iv_water_back = (GradientImageView) findViewById(R.id.iv_water_back);//水背景
		iv_sunshine_back = (GradientImageView) findViewById(R.id.iv_sunshine_back);//光照背景
		iv_sunshine = (GradientImageView) findViewById(R.id.iv_sun_button);//光照按钮
		iv_recode = (ImageView) findViewById(R.id.iv_recode_button);
//		iv_auto = (ImageView) findViewById(R.id.iv_auto_button);
		tv_sun = (TextView) findViewById(R.id.tv_sun);
		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);
		tv_water = (TextView) findViewById(R.id.tv_water);
		tv_soil_humidity = (TextView) findViewById(R.id.tv_soil_humidity);
		potname = (TextView) findViewById(R.id.pot_head_name);
		String pot_head_name = getIntent().getExtras().getString("POT_HEAD_NAME");
		potname.setText(pot_head_name);

		String PotID = getIntent().getExtras().getString("PotID");
		pot_id = PotID;
		//water_switch= (IOSSwitchView) findViewById(R.id.switch_water);


		//进入马上刷新数据
		//用okHttp框架,以下都采用异步的方式
		//获取花盆传感器的信息
		getPotStatus();

//		getPhoto();

		setListener();//设置监听

	}


	private void setListener() {
		backImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();//点击注销
			}
		});

		iv_water.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pot_message == null) {
					Util.showToast(getApplicationContext(), "控制失败，请先打开花盆");
					return;
				}

				pot_message.setWater("1");
				ControlActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
							iv_water_back.setImageDrawable(getResources().getDrawable(R.drawable.button_bg_focused));
					}
				});

				light_first = pot_message.getLight();

				switch_num = (pot_message.getLight() + pot_message.getWater());
				Log.e("switch", switch_num);
				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder()
						.url("http://srms.telecomlab.cn/ZZX/flower/index/switch1?uid=" + pot_id + "&switch=" + switch_num)
						.get()
						.build();
				// Response response = client.newCall(request).execute();
				Call call = client.newCall(request);
				call.enqueue(new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {
						Log.i(TAG, "onFailure: " + e);
						ControlActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Util.showToast(getApplicationContext(), "开关失效，请检查网络");

								pot_message.setLight(light_first);
								pot_message.setWater("0");

								ControlActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										showSwitchMessage();
									}
								});

							}
						});

					}

					@Override
					public void onResponse(Call call, Response response) throws IOException {
						String jsonString = response.body().string();
						try {
							JSONObject jsonObject = new JSONObject(jsonString);
							int result = Integer.parseInt(jsonObject.getString("status"));
							String message = jsonObject.getString("message");

							if (result == 1) {
								Util.showToast(getApplicationContext(), "花盆已浇水");

								//下面通过这个方法在主线程更新UI
								ControlActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
											iv_water_back.setImageDrawable(getResources().getDrawable(R.drawable.button_bg_normal));
									}
								});
							} else {
								pot_message.setLight(light_first);
								pot_message.setWater("0");
								Util.showToast(getApplicationContext(), message);
								Log.e("message", message);
								ControlActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										showSwitchMessage();
									}
								});
							}

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "好像出错了~", Toast.LENGTH_LONG).show();
						}

					}
				});

			}

		});

		iv_sunshine.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {
				if (pot_message == null) {
					Util.showToast(getApplicationContext(), "控制失败，请先打开花盆");
					return;
				}

				if (pot_message.getLight().equals("0")) {
					light_first = "0";
					pot_message.setLight("1");
					Log.e("Light", pot_message.getLight());

				} else {
					light_first = "1";
					pot_message.setLight("0");
					Log.e("Light", pot_message.getLight());

				}
				pot_message.setWater("0");

						if (pot_message.getLight().equals("0")) {
							iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_normal));
						} else {
							iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_focused));
						}


				switch_num = (pot_message.getLight() + pot_message.getWater());
				Log.e("switch", switch_num);
				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder()
						.url("http://srms.telecomlab.cn/ZZX/flower/index/switch1?uid=" + pot_id + "&switch=" + switch_num)
						.get()
						.build();
				// Response response = client.newCall(request).execute();
				Call call = client.newCall(request);
				call.enqueue(new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {
						Log.i(TAG, "onFailure: " + e);
						ControlActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Util.showToast(getApplicationContext(), "开关失效，请检查网络");

								pot_message.setLight(light_first);
								pot_message.setWater("0");

								ControlActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										showSwitchMessage();
									}
								});

							}
						});

					}

					@Override
					public void onResponse(Call call, Response response) throws IOException {
						String jsonString = response.body().string();
						try {
							JSONObject jsonObject = new JSONObject(jsonString);
							int result = Integer.parseInt(jsonObject.getString("status"));
							String message = jsonObject.getString("message");

							if (result == 1) {
								Util.showToast(getApplicationContext(), "灯光控制成功");

								//下面通过这个方法在主线程更新UI
								ControlActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if (pot_message.getLight().equals("0")) {
											iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_normal));
										} else {
											iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_focused));
										}
									}
								});
							} else {
								pot_message.setLight(light_first);
								pot_message.setWater("0");
								Util.showToast(getApplicationContext(), message);
								Log.e("message", message);
								ControlActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if (pot_message.getLight().equals("0")) {
											iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_normal));
										} else {
											iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_focused));
										}
									}
								});
							}

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "好像出错了~", Toast.LENGTH_LONG).show();
						}

					}
				});


			}

		});

		iv_recode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.showToast(getApplicationContext(), "记录");
			}

		});

//		iv_auto.setOnClickListener(new View.OnClickListener() {
//
//
//			@Override
//			public void onClick(View v) {
//
//
//				//下面通过这个方法在主线程更新UI
//				ControlActivity.this.runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						showMessage();
//					}
//				});
//
//			}
//
//		});

	}

	private void getPotStatus() {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("http://srms.telecomlab.cn/ZZX/flower/login/pot_one_info?uid=" + pot_id)
				.get()
				.build();
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				ControlActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "获取花盆传感器失败，检查网络", Toast.LENGTH_LONG).show();
					}
				});

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				//Response response = client.newCall(request).execute();
				String jsonString = response.body().string();
				PotMessageJson potJson = new PotMessageJson();
				try {
					pot_message = potJson.messagePull(jsonString);

					//下面通过这个方法在主线程更新UI
					ControlActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showMessage();
						}
					});


				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "获取数据失败，请打开花盆", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	public void getPhoto() {
		//不知道为什么使用Glide无法加载图片，该url下。
		/*Glide.with(getApplicationContext())
				.load("http://api.yeelink.net/v1.0/device/354593/sensor/400698/photo/content")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                       // Log.d(TAG, "onException: " + e.toString()+"  model:"+model+" isFirstResource: "+isFirstResource);
                        Util.showToast(getApplicationContext(),"加载失败~");
                        imageView.setImageResource(R.mipmap.d);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                       // Log.e(TAG, "isFromMemoryCache:"+isFromMemoryCache+"  model:"+model+" isFirstResource: "+isFirstResource);

                        return false;

                    }
                })

                .centerCrop()
                .into(imageView);*/

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("http://srms.telecomlab.cn/ZZX/flower/login/pot_one_info?uid=" + pot_id)
				.get()
				.build();
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				ControlActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//网络图片请求成功，更新到主线程的ImageView
						Toast.makeText(getApplicationContext(), "花盆图片获取失败，请检查网络", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String jsonString = response.body().string();
				PotMessageJson potJson = new PotMessageJson();
				try {
					pot_message = potJson.messagePull(jsonString);


					//下面通过这个方法在主线程更新UI
					ControlActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showMessage();
						}
					});

//					URL url = new URL(image_url);
//					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//					final BufferedInputStream is = new BufferedInputStream(conn.getInputStream());
//					final Bitmap bitmap = BitmapFactory.decodeStream(is);
//
//					ControlActivity.this.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							imageView.setImageBitmap(bitmap);
//						}
//					});


//					byte[] bytes = response.body().bytes();
//					final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							//网络图片请求成功，更新到主线程的ImageView
//							imageView.setImageBitmap(bmp);
//						}
//					});

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "花盆图片加载失败~", Toast.LENGTH_SHORT).show();
				}

			}
		});

		if (pot_message != null) {
			Glide
					.with(context)
					.load(pot_message.getUrl())
					.into(imageView);
		}
	}


	private void showMessage() {

		tv_temperature.setText(pot_message.getTemperature());
		tv_humidity.setText(pot_message.getHumidity());
		tv_soil_humidity.setText(pot_message.getSoil_humidity());
		tv_water.setText(pot_message.getWater_level());
		tv_sun.setText(pot_message.getSun());

//		注意：数据支持整数，如果是小数的话将会报错
		pb_temperature.setProgress(Integer.valueOf(pot_message.getTemperature()));
		pb_humidity.setProgress(Integer.valueOf(pot_message.getHumidity()));
		pb_soil_humidity.setProgress(Integer.valueOf(pot_message.getSoil_humidity()));
		pb_water.setProgress(Integer.valueOf(pot_message.getWater_level()));
		pb_sunshine.setProgress(Integer.valueOf(pot_message.getSun()));

		if (pot_message.getLight().equals("0")) {
			iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_normal));
		} else {
			iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_focused));
		}

		if (pot_message.getWater().equals("0")) {
			iv_water_back.setImageDrawable(getResources().getDrawable(R.drawable.button_bg_normal));
		} else {
			iv_water_back.setImageDrawable(getResources().getDrawable(R.drawable.button_bg_focused));
		}

	}

	private void showSwitchMessage(){
		if (pot_message.getLight().equals("0")) {
			iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_normal));
		} else {
			iv_sunshine_back.setImageDrawable(getResources().getDrawable(R.drawable.button_light_bg_focused));
		}

		if (pot_message.getWater().equals("0")) {
			iv_water_back.setImageDrawable(getResources().getDrawable(R.drawable.button_bg_normal));
		} else {
			iv_water_back.setImageDrawable(getResources().getDrawable(R.drawable.button_bg_focused));
		}
	}
}
