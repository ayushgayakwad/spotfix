package com.ambition.spotfix;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;

import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private String user_location = "";
    private LinearLayout linearx;
    private LinearLayout linears;
    private LinearLayout linear7;
    private LinearLayout linear8;
    private TextView report;
    private TextView history;
    private ScrollView vscroll1;
    private TextView language;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    static double mLatitude,mLongitude;
    private SharedPreferences userdata;
    static Address address;
    private FirebaseAnalytics mFirebaseAnalytics;

    private DatabaseReference user = _firebase.getReference("user");
    private ChildEventListener _user_child_listener;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        loadLanguagePreference();
        setContentView(R.layout.activity_home);

        // Need to check permission regularly
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES
            }, 1);
        }

        linear7 = findViewById(R.id.linear7);
        linear8 = findViewById(R.id.linear8);
        linearx = findViewById(R.id.linearx);
        linears = findViewById(R.id.linears);
        report = findViewById(R.id.report);
        history = findViewById(R.id.history);
        vscroll1 = findViewById(R.id.vscroll1);

        userdata = getSharedPreferences("userdata", Activity.MODE_PRIVATE);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));
        _rippleRoundStroke(report, "#4c6ef4", "#f5f5f5", 25, 2, "#eeeeee");
        _rippleRoundStroke(history, "#4c6ef4", "#f5f5f5", 25, 2, "#eeeeee");

        FirebaseApp.initializeApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        vscroll1.setVerticalScrollBarEnabled(false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        linears.setOnClickListener(v -> {
            Intent n = new Intent(this, MapView.class);
            startActivity(n);
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
        });

        linear7.setOnClickListener(v -> {
            Intent n = new Intent(this,Info.class);
            startActivity(n);
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
        });

        linear8.setOnClickListener(v -> {
            Intent n = new Intent(this,Profile.class);
            startActivity(n);
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
        });

        report.setOnClickListener(v -> {
            Intent n = new Intent(this,Report.class);
            startActivity(n);
        });

        history.setOnClickListener(v -> {
            Intent n = new Intent(this,History.class);
            startActivity(n);
        });

        // sharedPref
        getLocation();

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

        user.removeEventListener(_user_child_listener);
        user_location = "user/".concat(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user = _firebase.getReference(user_location);

        ValueEventListener valuelistener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _param1) {
                GenericTypeIndicator <HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap< String, Object>>() {};
                try {
                    HashMap <String, Object> _map= _param1.getValue(_ind);

                    try{
                        userdata.edit().putString("name", _map.get("name").toString()).commit();
                        userdata.edit().putString("cn", _map.get("cn").toString()).commit();
                        userdata.edit().putString("city", _map.get("city").toString()).commit();
                        userdata.edit().putString("district", _map.get("district").toString()).commit();
                        userdata.edit().putString("state", _map.get("state").toString()).commit();
                        userdata.edit().putString("email", _map.get("email").toString()).commit();
                        userdata.edit().putString("uid", _map.get("uid").toString()).commit();
                    }catch(Exception e){

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        user.addValueEventListener(valuelistener2);
    }


    // callback for requestLocationUpdate
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull @org.jetbrains.annotations.NotNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
        }
    };

    // Updating the global variable latitude and longitude from getLocation
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Permission for location is not granted",Toast.LENGTH_SHORT).show();
            return;
        }
       locationRequest = LocationRequest.create();
       locationRequest.setInterval(100000);
       locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if(location!=null){
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                userdata.edit().putString("latitude", (Double.toString(mLatitude))).commit();
                userdata.edit().putString("longitude", (Double.toString(mLongitude))).commit();
            }
            else{
                Toast.makeText(getApplicationContext(),"Cannot get location",Toast.LENGTH_SHORT).show();
            }
        });

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
        finishAffinity();
    }
}