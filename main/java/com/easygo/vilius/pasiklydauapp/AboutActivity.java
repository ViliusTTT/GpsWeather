package com.easygo.vilius.pasiklydauapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;


/**
 * AboutActivity klase- klasė skirta atidaryti about skyreliui
 */
public class AboutActivity extends AppCompatActivity {
    /**
     * onCreate metodas nustato about puslapio webview
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);

        WebView webView = (WebView)findViewById(R.id.about_webview);
        webView.loadUrl("file:///android_asset/html/about.html");

    }
}
