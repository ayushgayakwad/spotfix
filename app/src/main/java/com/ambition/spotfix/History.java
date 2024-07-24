package com.ambition.spotfix;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class History extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> historylist = new ArrayList<>();
    private ListView listview1;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        loadLanguagePreference();
        setContentView(R.layout.activity_history);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        listview1 = findViewById(R.id.listview1);
    }

    private void initializeLogic() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));
        listview1.setVerticalScrollBarEnabled(false);

        fetchUserIssues(FirebaseAuth.getInstance().getCurrentUser().getUid());

        listview1.setAdapter(new Listview1Adapter(historylist));
    }

    private void fetchUserIssues(String userId) {
        DatabaseReference waterRef = FirebaseDatabase.getInstance().getReference("water");
        DatabaseReference electricityRef = FirebaseDatabase.getInstance().getReference("electricity");
        DatabaseReference constructionRef = FirebaseDatabase.getInstance().getReference("construction");
        DatabaseReference sanitationRef = FirebaseDatabase.getInstance().getReference("sanitation");

        fetchIssuesFromNode(waterRef, userId);
        fetchIssuesFromNode(electricityRef, userId);
        fetchIssuesFromNode(constructionRef, userId);
        fetchIssuesFromNode(sanitationRef, userId);
    }

    private void fetchIssuesFromNode(DatabaseReference ref, String userId) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.child("uid").getValue(String.class);
                    if (userId.equals(uid)) {
                        HashMap<String, Object> issue = new HashMap<>();
                        issue.put("issue_id", snapshot.child("issue_id").getValue() != null ? snapshot.child("issue_id").getValue().toString() : "");
                        issue.put("desc", snapshot.child("desc").getValue(String.class));
                        issue.put("reported_time", snapshot.child("reported_time").getValue(String.class));
                        issue.put("status", snapshot.child("status").getValue(String.class));
                        issue.put("image", snapshot.child("image").getValue(String.class));
                        issue.put("latitude", snapshot.child("latitude").getValue(String.class));
                        issue.put("longitude", snapshot.child("longitude").getValue(String.class));
                        historylist.add(issue);
                    }
                }
                ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _view, ViewGroup _viewGroup) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;

            if (_v == null) {
                _v = _inflater.inflate(R.layout.historyview, null);
            }

            final LinearLayout linear1 = _v.findViewById(R.id.linear1);
            final LinearLayout linear4 = _v.findViewById(R.id.linear4);
            final ImageView image = _v.findViewById(R.id.image);
            final TextView id = _v.findViewById(R.id.id);
            final TextView description = _v.findViewById(R.id.description);
            final TextView date = _v.findViewById(R.id.date);
            final TextView status = _v.findViewById(R.id.status);
            final TextView location = _v.findViewById(R.id.location);
            HashMap<String, Object> issue = historylist.get(_position);
            if(issue.get("status").toString().equals("solved")) {
                _SetCornerRadius(description, 25, 0, "#478778");
                _SetCornerRadius(linear4, 50, 12, "#C1E1C1");
            }
            else{
                _SetCornerRadius(description, 25, 0, "#800020");
                _SetCornerRadius(linear4, 50, 12, "#FAA0A0");
            }
            id.setText(getString(R.string.issue_id) + ": " + issue.get("issue_id").toString());
            description.setText(issue.get("desc").toString());
            date.setText(getString(R.string.reported_on) + ": " + issue.get("reported_time").toString());
            status.setText(getString(R.string.status) + ": " + issue.get("status").toString());
            location.setText(getString(R.string.location) + ": " + issue.get("latitude").toString()+", " + issue.get("longitude").toString());
            String imageUrl = issue.get("image") != null ? issue.get("image").toString() : "";
            Glide.with(getBaseContext()).load(imageUrl).into(image);
            return _v;
        }
    }

    private void _SetCornerRadius(View _view, double _radius, double _shadow, String _color) {
        android.graphics.drawable.GradientDrawable ab = new android.graphics.drawable.GradientDrawable();
        ab.setColor(Color.parseColor(_color));
        ab.setCornerRadius((float) _radius);
        _view.setElevation((float) _shadow);
        _view.setBackground(ab);
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
    }
}
