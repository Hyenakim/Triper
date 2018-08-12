package com.example.gpsk1.triper;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;

    private RadioButton radioTourist;
    private RadioButton radioGuide;
    private RadioGroup radioGroup;
    private Button login;
    private Button signup;
    private FirebaseAuth firebaseauth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 전역변수 */
        final MyApplication myApplication = (MyApplication)getApplication(); // 전역변수 가져오기
        myApplication.setMode(0); // 관광객 모드로 초기화

        firebaseauth = FirebaseAuth.getInstance();
        firebaseauth.signOut();
        id = (EditText)findViewById(R.id.loginActivity_edittext_id);
        password = (EditText)findViewById(R.id.loginActivity_edittext_password);

        /* 라디오 버튼 */
        radioTourist = (RadioButton)findViewById(R.id.loginActivity_radio_user);
        radioGuide = (RadioButton)findViewById(R.id.loginActivity_radio_guide);
        radioGroup = (RadioGroup)findViewById(R.id.loginActivity_radioGroup);
        radioTourist.setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.loginActivity_radio_user){
                    myApplication.setMode(0); // 관광객 모드
                    //Log.e("관광객 선택", String.valueOf(myApplication.getMode()));
                }
                else{
                    myApplication.setMode(1); // 가이드 모드
                    //Log.e("가이드 선택", String.valueOf(myApplication.getMode()));
                }
            }
        });

        login = (Button)findViewById(R.id.loginActivity_button_login);
        signup = (Button)findViewById(R.id.loginActivity_button_sign);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEvent();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SelectActivity.class));
            }
        });

        //로그인 인터페이스 리스너

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //로그인
                    Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                }else{
                    //로그아웃
                }
            }
        };
    }

    void loginEvent(){
        firebaseauth.signInWithEmailAndPassword(id.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //로그인 완료 판단
                //실패했을 경우 작동
                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseauth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseauth.removeAuthStateListener(authStateListener);
    }
}
