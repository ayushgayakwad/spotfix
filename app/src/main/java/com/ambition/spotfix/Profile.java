package com.ambition.spotfix;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private HashMap<String, Object> map = new HashMap<>();

    private String versionName="";

    private LinearLayout linearx;
    private LinearLayout linears;
    private LinearLayout linear7;
    private LinearLayout linear8;
    private LinearLayout linearp;
    private LinearLayout address;
    private LinearLayout ec;
    private Button logout;
    private TextView name;
    private TextView city;
    private TextView district;
    private TextView phone;
    private TextView email;
    private TextView state;
    private TextView uid;
    private ProgressBar progressbar1;
    private ScrollView vscroll1;
    private Intent i = new Intent();
    private SharedPreferences userdata;

    private DatabaseReference user = _firebase.getReference("user");
    private ChildEventListener _user_child_listener;

    private FirebaseAuth auth;
    private OnCompleteListener<Void> auth_updateEmailListener;
    private OnCompleteListener<Void> auth_updatePasswordListener;
    private OnCompleteListener<Void> auth_emailVerificationSentListener;
    private OnCompleteListener<Void> auth_deleteUserListener;
    private OnCompleteListener<Void> auth_updateProfileListener;
    private OnCompleteListener<AuthResult> auth_phoneAuthListener;
    private OnCompleteListener<AuthResult> auth_googleSignInListener;
    private OnCompleteListener<AuthResult> _auth_create_user_listener;
    private OnCompleteListener<AuthResult> _auth_sign_in_listener;
    private OnCompleteListener<Void> _auth_reset_password_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        linear7 = findViewById(R.id.linear7);
        linear8 = findViewById(R.id.linear8);
        linearx = findViewById(R.id.linearx);
        linears = findViewById(R.id.linears);
        linearp = findViewById(R.id.linearp);
        logout = findViewById(R.id.logout);
        vscroll1 = findViewById(R.id.vscroll1);
        progressbar1 =  findViewById(R.id.progressbar1);
        name = findViewById(R.id.name);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        district = findViewById(R.id.district);
        address = findViewById(R.id.address);
        uid = findViewById(R.id.uid);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        userdata = getSharedPreferences("userdata", Activity.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));

        vscroll1.setVerticalScrollBarEnabled(false);
        progressbar1.setVisibility(View.GONE);

        _rippleRoundStroke(logout, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");

        address.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; }}.getIns((int)16, (int)0, 0xFFFFFFFF, 0xFFE7EEFB));

        name.setText(userdata.getString("name", ""));
        city.setText("City: "+userdata.getString("city", ""));
        district.setText("District: "+userdata.getString("district", ""));
        state.setText("State: "+userdata.getString("state", ""));
        uid.setText("UID: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
        phone.setText("Phone Number: "+userdata.getString("cn", ""));
        email.setText("Email: "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
        linearp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog1 = new AlertDialog.Builder(Profile.this).create();
                View inflate = getLayoutInflater().inflate(R.layout.change_pwd,null);
                dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog1.setView(inflate);
                TextView t1c = (TextView) inflate.findViewById(R.id.t1c);

                TextView t2c = (TextView) inflate.findViewById(R.id.t2c);

                TextView b1c = (TextView) inflate.findViewById(R.id.b1c);

                TextView b2c = (TextView) inflate.findViewById(R.id.b2c);

                LinearLayout bgc = (LinearLayout) inflate.findViewById(R.id.bgc);
                t1c.setText("Change password?");
                t2c.setText("You will receive an email to change the password for ".concat(FirebaseAuth.getInstance().getCurrentUser().getEmail()).concat(" account."));
                b1c.setText("Continue");
                b2c.setText("Cancel");
                _rippleRoundStroke(bgc, "#FFFFFF", "#000000", 15, 0, "#000000");
                _rippleRoundStroke(b1c, "#FF1944f1", "#f5f5f5", 100, 2, "#eeeeee");
                b1c.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
                    dialog1.dismiss();
                    progressbar1.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addOnCompleteListener(_auth_reset_password_listener);
                }
                });
                b2c.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
                    dialog1.dismiss();
                }
                });
                dialog1.setCancelable(true);
                dialog1.show();
            }
        });

        linears.setOnClickListener(v -> {
            Intent n = new Intent(this, MapView.class);
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


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                FirebaseAuth.getInstance().signOut();
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setClass(getApplicationContext(), Auth.class);
                startActivity(i);
                finish();
            }
        });

        _auth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
                if (_success) {
                    progressbar1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "An email to change your password has been sent to your account.", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressbar1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Oops! An error occured. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        };

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
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}