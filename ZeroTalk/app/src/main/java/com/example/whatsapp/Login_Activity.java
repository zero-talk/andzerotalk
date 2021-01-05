package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    EditText userETLogin, passETLogin;
    Button buttonLogin, registerBtn;

    //test

    //Firebase
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        //firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //자동로그인
        if (firebaseUser != null) {
            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        userETLogin = findViewById(R.id.userETLogin);
        passETLogin = findViewById(R.id.passETLogin);
        buttonLogin = findViewById(R.id.buttonLogin);

        auth = FirebaseAuth.getInstance();

        //등록 버튼
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        //로그인 버튼
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = userETLogin.getText().toString();
                String pass_text = passETLogin.getText().toString();

                //만약 이메일이나 패스워드가 비어있으면
                if(TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)){
                    Toast.makeText(Login_Activity.this, "이메일 또는 비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                } else {
                    auth.signInWithEmailAndPassword(email_text, pass_text)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                     //로그인이 성공적이지 않으면
                                    } else {
                                        Toast.makeText(Login_Activity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });


    }
}