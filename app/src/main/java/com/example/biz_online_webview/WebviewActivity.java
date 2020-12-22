package com.example.biz_online_webview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {
    private WebView webView;
    private String url = "http://192.168.0.37:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 웹뷰와 안드로이드와 연결해 핸드폰에 기능을 제어함
        webView.addJavascriptInterface(new WebViewInterface(), "Android"); //웹뷰에 JavascriptInterface를 연결

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.e("현재url: ", view.getUrl());
                return true;
            }
        });
        webView.loadUrl(url);
    }

    // 웹뷰와 안드로이드와 연결해 핸드폰에 기능을 제어함
    private class WebViewInterface {

        // 기기의 공유 기능 작동함
        @JavascriptInterface
        public void doShare(final String arg1) {
            new Handler().post(new Runnable() {
                public void run() {
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, arg1); // 내용
                    startActivity(Intent.createChooser(shareIntent, "공유하기")); // 공유창 제목
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}