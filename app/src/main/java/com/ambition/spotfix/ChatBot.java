package com.ambition.spotfix;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ChatBot extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private String timestamp = "";

    private ListView chatListView;
    private EditText chatInput;
    private Button sendButton;
    private ProgressBar uploadProgressBar;

    private ArrayAdapter<String> chatAdapter;
    private ArrayList<String> chatMessages;

    private String department;
    private String description;
    private Uri imageUri;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private SharedPreferences userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFE7EEFB);
        Window window = this.getWindow();
        window.setNavigationBarColor(Color.parseColor("#1944f1"));

        chatListView = findViewById(R.id.chatListView);
        chatInput = findViewById(R.id.chatInput);
        sendButton = findViewById(R.id.sendButton);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);

        userdata = getSharedPreferences("userdata", Activity.MODE_PRIVATE);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);
        chatListView.setAdapter(chatAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        chatInput.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; }}.getIns((int)50, (int)0, 0xFFFFFFFF, 0xFFEEEEEE));
        _rippleRoundStroke(sendButton, "#0026c0", "#f5f5f5", 100, 2, "#eeeeee");

        sendMessage("Hello " + userdata.getString("name", "") + "! Welcome to SpotFix. Please choose a department: \n- water;\n- electricity;\n- construction;\n- sanitation;");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatInput.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    processMessage(message);
                    chatInput.setText("");
                }
            }
        });
    }

    private void processMessage(String message) {
        addMessage("You: " + message, true);

        if (department == null) {
            if (message.equalsIgnoreCase("water") || message.equalsIgnoreCase("electricity") ||
                    message.equalsIgnoreCase("construction") || message.equalsIgnoreCase("sanitation")) {
                department = message.toLowerCase(Locale.ROOT);
                sendMessage("Please describe the issue:");
            } else {
                sendMessage("Invalid department. Please select one of: water, electricity, construction, sanitation");
            }
        } else if (description == null) {
            description = message;
            sendMessage("Please upload an image of the issue:");
            openFileChooser();
        }
    }

    private void sendMessage(String message) {
        addMessage("Bot: " + message, false);
    }

    private void addMessage(String message, boolean isUser) {
        chatMessages.add(message);
        chatAdapter.notifyDataSetChanged();
        chatListView.smoothScrollToPosition(chatMessages.size() - 1);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadIssue();
        }
    }

    private void uploadIssue() {
        if (imageUri != null) {
            uploadProgressBar.setVisibility(View.VISIBLE);
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
            // Upload the image
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    saveIssueToDatabase(imageUrl);
                                    uploadProgressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatBot.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            uploadProgressBar.setVisibility(View.GONE);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgressBar.setProgress((int) progress);
                        }
                    });
        }
    }

    private void saveIssueToDatabase(String imageUrl) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        String issueId = String.valueOf(new Random().nextInt(900000000) + 100000000);
        String status = "unsolved";
        timestamp = getCurrentTimestamp();
        Map<String, Object> issue = new HashMap<>();
        issue.put("issue_id", issueId);
        issue.put("name", userdata.getString("name", ""));
        issue.put("uid", userId);
        issue.put("cn", userdata.getString("cn", "")); // Replace with actual contact number
        issue.put("desc", description);
        issue.put("status", status);
        issue.put("longitude", userdata.getString("longitude", ""));
        issue.put("latitude", userdata.getString("latitude", ""));
        issue.put("reported_time", timestamp);
        issue.put("image", imageUrl);
        issue.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());

        databaseReference.child(department).push().setValue(issue);

        sendMessage("Issue reported successfully! Thank you.");
    }

    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
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
