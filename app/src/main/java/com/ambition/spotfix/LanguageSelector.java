package com.ambition.spotfix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LanguageSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selector);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));

        Button englishButton = findViewById(R.id.english_button);
        Button hindiButton = findViewById(R.id.hindi_button);
        Button kannadaButton = findViewById(R.id.kannada_button);

        _rippleRoundStroke(englishButton, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");
        _rippleRoundStroke(hindiButton, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");
        _rippleRoundStroke(kannadaButton, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");

        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguagePreference("en");
                setLocale("en");
            }
        });

        hindiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguagePreference("hi");
                setLocale("hi");
            }
        });

        kannadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguagePreference("kn");
                setLocale("kn");
            }
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void saveLanguagePreference(String languageCode) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

    public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
        android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
        GG.setColor(Color.parseColor(_focus));
        GG.setCornerRadius((float)_round);
        GG.setStroke((int) _stroke,
                Color.parseColor("#" + _strokeclr.replace("#", "")));
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
        _view.setBackground(RE);
    }
}
