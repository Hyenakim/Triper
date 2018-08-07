package com.example.gpsk1.triper;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gpsk1.triper.model.GuideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class GuideActivity extends AppCompatActivity {
    private Button signup;
    private EditText email;
    private EditText name;
    private EditText password;
    private String where;
    private String speech1;
    private String speech2;
    private ImageView profile;
    private Uri imageUri;
    private static final int PICK_FROM_ALBUM = 10;

    String[] place = {"Seoul", "Busan", "Gwangju", "Gangneung", "Jeju"};
    String[] language = {"English", "Japanese", "Chinese", "French", "Spanish"};
    String[] language2 = {"X", "English", "Japanese", "Chinese", "French", "Spanish"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        profile = (ImageView)findViewById(R.id.GuideActivity_imageview_profile);
        email = (EditText)findViewById(R.id.GuideActivity_edittext_email);
        name = (EditText)findViewById(R.id.GuideActivity_edittext_name);
        password = (EditText)findViewById(R.id.GuideActivity_edittext_password);
        signup = (Button)findViewById(R.id.GuideActivity_button_signup);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        // 지역 관련 스피너
        Spinner spinner_place = (Spinner)findViewById(R.id.GuideActivity_spinner_place);
        ArrayAdapter<String> adapter_place = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, place);
        adapter_place.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_place.setAdapter(adapter_place);

        spinner_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                where = place[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                where = null;
            }
        });

        // 언어1 관련 스피너
        Spinner spinner_language1 = (Spinner)findViewById(R.id.GuideActivity_spinner_language1);
        ArrayAdapter<String> adapter_language1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, language);
        adapter_language1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_language1.setAdapter(adapter_language1);

        spinner_language1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speech1 = language[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                speech1 = null;
            }
        });

        // 언어2 관련 스피너
        Spinner spinner_language2 = (Spinner)findViewById(R.id.GuideActivity_spinner_language2);
        ArrayAdapter<String> adapter_language2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, language2);
        adapter_language2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_language2.setAdapter(adapter_language2);

        spinner_language2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speech2 = language2[position];
                if(speech2 == "X" )
                    speech2 = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                speech2 = null;
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("시작", "시작");
               if(email.getText().toString() == null || password.getText().toString() == null || name.getText().toString() == null || where == null ||
                       speech1 == null || imageUri == null ){
                   Toast.makeText(GuideActivity.this, "빈 칸이 존재합니다.", Toast.LENGTH_LONG).show();
                   return;
               }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(GuideActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String uid = task.getResult().getUser().getUid();

                        FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                @SuppressWarnings("VisibleForTests")
                                String imageUri = task.getResult().getStorage().getDownloadUrl().toString();
                                GuideModel guidemodel = new GuideModel();
                                guidemodel.guideName = name.getText().toString();
                                guidemodel.place = where;
                                guidemodel.language1 = speech1;
                                guidemodel.language2 = speech2;
                                guidemodel.profilImageUrl = imageUri;
                                guidemodel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                FirebaseDatabase.getInstance().getReference().child("Guide").child(uid).setValue(guidemodel);
                                Toast.makeText(GuideActivity.this, "회원가입 완료", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            profile.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }
}
