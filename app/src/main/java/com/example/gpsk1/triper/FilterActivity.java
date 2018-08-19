package com.example.gpsk1.triper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.gpsk1.triper.fragment.PeopleFragment;

import java.lang.reflect.Array;

public class FilterActivity extends Activity{

    private Button check;
    private String resultPlace;
    private String resultLanguage;

    String[] place = {"X","Seoul", "Busan", "Gwangju", "Gangneung", "Jeju"};
    String[] language = {"X","English", "Japanese", "Chinese", "French", "Spanish"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter);

        check = (Button)findViewById(R.id.filterActivity_button_OK);

        final MyApplication myApplication = (MyApplication)getApplication();
        resultPlace = null;
        resultLanguage = null;

        /* 장소 관련 스피너 */
        Spinner spinnerP = (Spinner)findViewById(R.id.filterActivity_spinner_place);
        ArrayAdapter<String> adapterP = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, place);
        adapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerP.setAdapter(adapterP);

        spinnerP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultPlace = place[position];
                if(resultPlace == "X")
                    resultPlace = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               resultPlace = null;
            }
        });

        /* 언어 관련 스피너 */
        Spinner spinnerL = (Spinner)findViewById(R.id.filterActivity_spinner_language);
        ArrayAdapter<String> adapterL = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, language);
        adapterL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerL.setAdapter(adapterL);

        spinnerL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultLanguage = language[position];
                if(resultLanguage == "X")
                    resultLanguage = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resultLanguage = null;
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("place", resultPlace);
                returnIntent.putExtra("language", resultLanguage);
                setResult(RESULT_OK, returnIntent);
                finish();
                }
        });

    }


}
