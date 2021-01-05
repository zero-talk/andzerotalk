package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.whatsapp.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements Serializable {

    EditText userET, passET, emailET;
    Button buttonRegister;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;

    //성별선택
    RadioGroup gender;
    String gender_case;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        userET = findViewById(R.id.userEditText);
        passET = findViewById(R.id.passEditText);
        emailET = findViewById(R.id.emailEditText);
        buttonRegister = findViewById(R.id.buttonRegister);
        gender = findViewById(R.id.gender);

        //firebase 인증
        auth = FirebaseAuth.getInstance();

        //다음 버튼
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_text = userET.getText().toString();
                String email_text = emailET.getText().toString();
                String pass_text = passET.getText().toString();

                if(TextUtils.isEmpty(username_text) || TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)|| gender_case == null){
                    Toast.makeText(RegisterActivity.this, "입력하지 않은 칸이 있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    RegisterNow(username_text, email_text, pass_text, gender_case);
                }
            }
        });

        //성별 선택하기
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group.getId()==R.id.gender){
                    switch(checkedId){
                        case R.id.gender_man:
                            gender_case = "남성";
                            break;
                        case R.id.gender_woman:
                            gender_case = "여성";
                            break;
                        case R.id.gender_none:
                            gender_case = "미선택";
                            break;
                    }
                }
            }
        });
    }

    private void RegisterNow(final String username, String email, String password, String gender){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            myRef = FirebaseDatabase.getInstance()
                                    .getReference("MyUsers").child(userid);

                            //HashMap
                            HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userid);
                                hashMap.put("username", username);
                                hashMap.put("gender", gender);
                                hashMap.put("imageURL", "default");
                                hashMap.put("status", "offline");
                                hashMap.put("hashtotal", "1");

                            //질문 액티비티로 이동
                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Intent intent =  new Intent(RegisterActivity.this, QuestionRegisterActivity.class);
                                       intent.putExtra("id", userid);
                                       intent.putExtra("username", username);
                                       intent.putExtra("gender", gender);
                                       intent.putExtra("imageURL", "default");
                                       intent.putExtra("status", "offline");
                                       intent.putExtra("hashtotal", "1");
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                       startActivity(intent);
                                       finish();
                                   }
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "입력한 내용을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //텍스트 외 다른 부분 클릭시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}