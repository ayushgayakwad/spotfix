package com.ambition.spotfix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MapView extends AppCompatActivity {

    private LinearLayout linearx;
    private LinearLayout linears;
    private LinearLayout linear7;
    private LinearLayout linear8;
    private TextView textview5;
    private WebView webview1;
    private ProgressBar progressbar1;
    private Timer _timer = new Timer();
    private TimerTask timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguagePreference();
        setContentView(R.layout.activity_mapview);

        linear7 = findViewById(R.id.linear7);
        linear8 = findViewById(R.id.linear8);
        linearx = findViewById(R.id.linearx);
        linears = findViewById(R.id.linears);
        webview1 = findViewById(R.id.webview1);
        textview5 = findViewById(R.id.textView5);
        progressbar1 = findViewById(R.id.progressbar1);

        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.getSettings().setSupportZoom(true);
        webview1.getSettings().setLoadWithOverviewMode(true);
        webview1.getSettings().setUseWideViewPort(true);
        webview1.getSettings().setBuiltInZoomControls(true);
        webview1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview1.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview1.getSettings().setDomStorageEnabled(true);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));

        webview1.loadUrl("https://spotfix-ambition.web.app/");

        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Check the URL and decide what to do
                if (url.startsWith("https://spotfix-ambition.web.app/")) {
                    // Handle the URL within your app
                    return false; // Let the WebView load this URL
                } else {
                    // Let the WebView load this URL (or open in a browser)
                    view.loadUrl(url);
                    return true; // Indicate that we've handled the URL ourselves
                }
            }
        });

        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
                final String _url = _param2;
                progressbar1.setVisibility(View.VISIBLE);
                timer = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressbar1.setProgress(webview1.getProgress());
                            }
                        });
                    }
                };
                _timer.scheduleAtFixedRate(timer, (int)(50), (int)(50));
                super.onPageStarted(_param1, _param2, _param3);
            }

            @Override
            public void onPageFinished(WebView _param1, String _param2) {
                final String _url = _param2;
                progressbar1.setVisibility(View.GONE);
                textview5.setVisibility(View.GONE);
                super.onPageFinished(_param1, _param2);
            }
        });

        linear8.setOnClickListener(v -> {
            Intent n = new Intent(this,Profile.class);
            startActivity(n);
            finish();
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
        });

        linear7.setOnClickListener(v -> {
            Intent n = new Intent(this,Info.class);
            startActivity(n);
            finish();
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
        });

        linearx.setOnClickListener(v -> {
            Intent n = new Intent(this, Home.class);
            startActivity(n);
            finish();
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
        });
    }

    public void loadLanguagePreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("language", "en");
        setLocale(languageCode);
    }

    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}