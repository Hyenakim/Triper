package com.example.gpsk1.triper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gpsk1.triper.fragment.PeopleFragment;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getFragmentManager().beginTransaction().replace(R.id.main2activity_framelayout,new PeopleFragment()).commit();

    }
}
