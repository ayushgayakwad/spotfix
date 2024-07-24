package com.ambition.spotfix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Info extends AppCompatActivity {
    private LinearLayout linearx;
    private LinearLayout linears;
    private LinearLayout linear7;
    private LinearLayout linear8;
    private ScrollView vscroll1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguagePreference();
        setContentView(R.layout.activity_info);

        linear7 = findViewById(R.id.linear7);
        linear8 = findViewById(R.id.linear8);
        linearx = findViewById(R.id.linearx);
        linears = findViewById(R.id.linears);
        vscroll1 = findViewById(R.id.vscroll1);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));

        vscroll1.setVerticalScrollBarEnabled(false);

        linear8.setOnClickListener(v -> {
            Intent n = new Intent(this,Profile.class);
            startActivity(n);
            finish();
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
        });

        linears.setOnClickListener(v -> {
            Intent n = new Intent(this, MapView.class);
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