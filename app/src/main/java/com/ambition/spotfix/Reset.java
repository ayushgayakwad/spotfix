package com.ambition.spotfix;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Reset extends AppCompatActivity {

    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linear5;
    private TextView textview1;
    private LinearLayout linear7;
    private LinearLayout linear9;
    private LinearLayout linear8;
    private LinearLayout linear10;
    private LinearLayout linear11;
    private ImageView view;
    private EditText rp_email;
    private Button rpb;
    private ProgressBar progressbar1;

    private Intent INTENT = new Intent();
    private FirebaseAuth fbAUTH;
    private OnCompleteListener <Void> _auth_reset_password_listener;
    private OnCompleteListener<AuthResult> _fbAUTH_sign_in_listener;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_reset);
        initialize(_savedInstanceState);
        FirebaseApp.initializeApp(this);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        linear2 = findViewById(R.id.linear2);
        linear3 = findViewById(R.id.linear3);
        linear5 = findViewById(R.id.linear5);
        linear7 = findViewById(R.id.linear7);
        linear9 = findViewById(R.id.linear9);
        linear8 = findViewById(R.id.linear8);
        linear10 = findViewById(R.id.linear10);
        linear11 = findViewById(R.id.linear11);
        view = findViewById(R.id.view);
        rp_email = findViewById(R.id.rp_email);
        rpb = findViewById(R.id.rpb);
        progressbar1 = findViewById(R.id.progressbar1);
        fbAUTH = FirebaseAuth.getInstance();

        rpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (rp_email.getText().toString().trim().equals("")) {
                    //Email field empty

                }
                else {
                    progressbar1.setVisibility(View.VISIBLE);
                    fbAUTH.sendPasswordResetEmail(rp_email.getText().toString()).addOnCompleteListener(_auth_reset_password_listener);
                }
            }
        });

        _auth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final Boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                if (_success) {
                    //On email sent
                    progressbar1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "An email containing link to reset your account's password has been sent to your email address.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    //Error
                    progressbar1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), _errorMessage,  Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void initializeLogic() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));
        rp_email.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; }}.getIns((int)50, (int)0, 0xFFFFFFFF, 0xFFEEEEEE));
        _rippleRoundStroke(rpb, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");
        progressbar1.setVisibility(View.GONE);
    }

    public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
        GradientDrawable GG = new GradientDrawable();
        GG.setColor(Color.parseColor(_focus));
        GG.setCornerRadius((float)_round);
        GG.setStroke((int) _stroke,
                Color.parseColor("#" + _strokeclr.replace("#", "")));
        android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
        _view.setBackground(RE);
    }
}
