package com.ambition.spotfix;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Auth extends AppCompatActivity {

    private LinearLayout linear1;
    private LinearLayout linear3;
    private LinearLayout linear5;
    private LinearLayout linear6;
    private TextView textview3;
    private LinearLayout linear7;
    private LinearLayout linear8;
    private Button button1;
    private Button button2;

    private Intent INTENT = new Intent();
    private FirebaseAuth fbAUTH;
    private OnCompleteListener<AuthResult> _fbAUTH_create_user_listener;
    private OnCompleteListener<AuthResult> _fbAUTH_sign_in_listener;
    private OnCompleteListener<Void> _fbAUTH_reset_password_listener;
    private OnCompleteListener<Void> fbAUTH_updateEmailListener;
    private OnCompleteListener<Void> fbAUTH_updatePasswordListener;
    private OnCompleteListener<Void> fbAUTH_emailVerificationSentListener;
    private OnCompleteListener<Void> fbAUTH_deleteUserListener;
    private OnCompleteListener<Void> fbAUTH_updateProfileListener;
    private OnCompleteListener<AuthResult> fbAUTH_phoneAuthListener;
    private OnCompleteListener<AuthResult> fbAUTH_googleSignInListener;


    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_auth);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        linear1 = findViewById(R.id.linear1);
        linear3 = findViewById(R.id.linear3);
        linear5 = findViewById(R.id.linear5);
        linear6 = findViewById(R.id.linear6);
        textview3 = findViewById(R.id.textview3);
        linear7 = findViewById(R.id.linear7);
        linear8 = findViewById(R.id.linear8);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        fbAUTH = FirebaseAuth.getInstance();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                INTENT.setClass(getApplicationContext(), Login.class);
                startActivity(INTENT);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                INTENT.setClass(getApplicationContext(), Register.class);
                startActivity(INTENT);
            }
        });

        fbAUTH_updateEmailListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        fbAUTH_updatePasswordListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        fbAUTH_emailVerificationSentListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        fbAUTH_deleteUserListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        fbAUTH_phoneAuthListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                final boolean _success = task.isSuccessful();
                final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";

            }
        };

        fbAUTH_updateProfileListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        fbAUTH_googleSignInListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                final boolean _success = task.isSuccessful();
                final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";

            }
        };

        _fbAUTH_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        _fbAUTH_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";

            }
        };

        _fbAUTH_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();

            }
        };
    }

    private void initializeLogic() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));
        _rippleRoundStroke(button1, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");
        _rippleRoundStroke(button2, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            INTENT.setClass(getApplicationContext(), Home.class);
            startActivity(INTENT);
            finish();
        }
        else {

        }
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

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
