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

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by dd on 2017/3/11.
 */

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView imageView;
	private ImageView goBack;
    private String url;
    private android.support.v7.widget.Toolbar toolbar;
    private static final  String EXTRA_WEB="com.task.dd.greenbox.Fragment.KnowFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weblayout);
        toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.iv_refresh);
		goBack = (ImageView) findViewById(R.id.iv_back);
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
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
				webView.reload();
            }
        });

        //toolbar.setNavigationIcon(R.mipmap.ic_greedbox80xr);
        //toolbar.setTitle("GreenBox");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

       url=getIntent().getStringExtra(EXTRA_WEB);


        webView= (WebView) findViewById(R.id.webview);
        webView.loadUrl(url);
        webView.setWebViewClient( new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
		WebSettings settings=webView.getSettings();
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
		settings.setJavaScriptEnabled(true);//适应JavaScript
		settings.setDomStorageEnabled(true);//适应Html5

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
