package com.example.whatsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageTask;

import java.io.Serializable;
import java.util.HashMap;

public class QuestionRegisterActivity extends AppCompatActivity implements Serializable {

    EditText pro_q1, pro_q2, pro_q3;
    Button button_question_register, button_skip_question;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_register);

        pro_q1 = findViewById(R.id.pro_q1);
        pro_q2 = findViewById(R.id.pro_q2);
        pro_q3 = findViewById(R.id.pro_q3);

        auth = FirebaseAuth.getInstance();

        //스킵버튼
        button_skip_question = findViewById(R.id.button_skip_question);
        button_skip_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionRegisterActivity("", "", "");
            }
        });

        //등록버튼
        button_question_register = findViewById(R.id.button_question_register);
        button_question_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profile_q1 = pro_q1.getText().toString();
                String profile_q2 = pro_q2.getText().toString();
                String profile_q3 = pro_q3.getText().toString();

                if(profile_q1.equals(null)) { profile_q1 = ""; }
                if(profile_q2.equals(null)) { profile_q2 = ""; }
                if(profile_q3.equals(null)) { profile_q3 = ""; }

                    QuestionRegisterActivity(profile_q1, profile_q2, profile_q3);
                }
        });
    }

    //질문 등록
    private void QuestionRegisterActivity(String pro_q1, String pro_q2, String pro_q3) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();

        myRef = FirebaseDatabase.getInstance()
                .getReference("MyUsers").child(userid);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String gender = intent.getStringExtra("gender");
        String imageURL = intent.getStringExtra("imageURL");
        String status = intent.getStringExtra("status");

        //Hashmap
        HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", userid);
            hashMap.put("username", username);
            hashMap.put("gender", gender);
            hashMap.put("imageURL", imageURL);
            hashMap.put("status", status);
            hashMap.put("pro_q1", pro_q1);
            hashMap.put("pro_q2", pro_q2);
            hashMap.put("pro_q3", pro_q3);

        //메인 화면으로 이동
        myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(QuestionRegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}