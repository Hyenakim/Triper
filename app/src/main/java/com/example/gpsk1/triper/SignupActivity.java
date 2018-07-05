package com.example.gpsk1.triper;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gpsk1.triper.model.UserModel;
//import com.google.android.gms.internal.ui;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private ImageView profile;
    //private Uri imageUri; //프로필사진 추가시 필요

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        profile = (ImageView)findViewById(R.id.signupActivity_imageview_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });

        email = (EditText)findViewById(R.id.signupActivity_edittext_email);
        name = (EditText)findViewById(R.id.signupActivity_edittext_name);
        password = (EditText)findViewById(R.id.signupActivity_edittext_password);
        signup = (Button)findViewById(R.id.signupActivity_edittext_signup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString()==null||name.getText().toString()==null||password.getText().toString()==null) { //빈칸이 있으면 다시
                    return;
                }
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                UserModel userModel = new UserModel();
                                userModel.userName = name.getText().toString();
                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                            }
                        });
            }
        });
    }

}
