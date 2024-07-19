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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private boolean isPasswordVisible = false;

    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linear5;
    private TextView textview1;
    private TextView rpt;
    private LinearLayout linear7;
    private LinearLayout linear9;
    private LinearLayout linear8;
    private LinearLayout linear10;
    private LinearLayout linear11;
    private ImageView view;
    private EditText email;
    private EditText pass;
    private Button button1;
    private ProgressBar progressbar1;

    private Intent INTENT = new Intent();
    private FirebaseAuth fbAUTH;
    private OnCompleteListener<AuthResult> _fbAUTH_sign_in_listener;

    private DatabaseReference user = _firebase.getReference("user");
    private ChildEventListener _user_child_listener;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_login);
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
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        rpt = findViewById(R.id.rpt);
        button1 = findViewById(R.id.button1);
        progressbar1 = findViewById(R.id.progressbar1);
        fbAUTH = FirebaseAuth.getInstance();

        rpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                INTENT.setClass(getApplicationContext(), Reset.class);
                startActivity(INTENT);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (email.getText().toString().trim().equals("")) {
                    //Email field empty
                    Toast.makeText(getApplicationContext(), "Please enter your email address...", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (pass.getText().toString().trim().equals("")) {
                        //Password field empty
                        Toast.makeText(getApplicationContext(), "Please enter your password...", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressbar1.setVisibility(View.VISIBLE);
                        fbAUTH.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim()).addOnCompleteListener(Login.this, _fbAUTH_sign_in_listener);
                    }
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                int cursorPosition = pass.getSelectionStart();
                if (isPasswordVisible) {
                    //show password
                    pass.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = false;
                    view.setImageResource(R.drawable.hide);
                }
                else {
                    //hide password
                    pass.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                    isPasswordVisible = true;
                    view.setImageResource(R.drawable.show);
                }
                pass.setSelection(cursorPosition);
            }
        });


        _user_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        user.addChildEventListener(_user_child_listener);

        _fbAUTH_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                if (_success) {
                    progressbar1.setVisibility(View.GONE);
                    FirebaseAuth auth = FirebaseAuth.getInstance(); com.google.firebase.auth.FirebaseUser user = auth.getCurrentUser(); if (user.isEmailVerified()) {
                        INTENT.setClass(getApplicationContext(), Home.class);
                        startActivity(INTENT);
                        finish();
                    } else {
                        progressbar1.setVisibility(View.GONE);
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getApplicationContext(), "Please verify your email address.",  Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressbar1.setVisibility(View.GONE);
                    FirebaseAuth.getInstance().signOut();
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
        _rippleRoundStroke(button1, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");
        email.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; }}.getIns((int)50, (int)0, 0xFFFFFFFF, 0xFFEEEEEE));
        pass.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; }}.getIns((int)50, (int)0, 0xFFFFFFFF, 0xFFEEEEEE));
        isPasswordVisible = true;
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
