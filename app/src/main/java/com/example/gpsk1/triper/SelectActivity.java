package com.example.gpsk1.triper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {

    private Button tourist;
    private Button guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        tourist = (Button)findViewById(R.id.selectActivity_Button_Tourist);
        guide = (Button)findViewById(R.id.selectActivity_Button_Guide);

        tourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this, SignupActivity.class));
            }
        }); // 관광객 버튼 클릭 -> Signup Activity로

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this, GuideActivity.class));
            }
        });


    }
}
