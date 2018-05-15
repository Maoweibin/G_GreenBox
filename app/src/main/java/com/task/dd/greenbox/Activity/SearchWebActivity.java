package com.task.dd.greenbox.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.task.dd.greenbox.R;
import com.task.dd.greenbox.tool.Util;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Mr.辉 on 2018/5/14.
 */

public class SearchWebActivity extends AppCompatActivity{
	private WebView webView;
	private ImageView imageView;
	private ImageView goBack;
	private String url;
	private android.support.v7.widget.Toolbar toolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchweblayout);
//		toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

		url="http://www.huabaike.com";

		webView= (WebView) findViewById(R.id.searchwebview);
		imageView = (ImageView) findViewById(R.id.iv_refresh);
		imageView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Util.showToast(getApplicationContext(),"重新加载");
				webView.reload();
			}
		});

		goBack = (ImageView) findViewById(R.id.web_back);
		goBack.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(webView.canGoBack()){
					webView.goBack();
				}
				else {
					finish();
				}
			}
		});

		webView.loadUrl(url);
		Util.showToast(getApplicationContext(),"正在加载...");
		webView.setWebViewClient( new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				return super.shouldOverrideUrlLoading(view, request);
			}
		});
		WebSettings settings=webView.getSettings();
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);//适应Html5
	}
	//处理并消费掉该 Back 事件，防止一按返回就注销Activity
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
