package com.example.gpsk1.triper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.gpsk1.triper.fragment.AccountFragment;
import com.example.gpsk1.triper.fragment.ChatFragment;
import com.example.gpsk1.triper.fragment.PeopleFragment;

public class Main2Activity extends AppCompatActivity {
    private int mode;
    ImageView howto;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        howto = (ImageView)findViewById(R.id.howto);
        relativeLayout = (RelativeLayout)findViewById(R.id.main2activity_relativeLayout);
        MyApplication myApplication = (MyApplication)getApplication();
        mode = myApplication.getMode(); // 모드 번호 받기
       // Log.v("메인2액티비티", String.valueOf(mode));

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main2activity_bottomnavigationview);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_people:
                        getFragmentManager().beginTransaction().replace(R.id.main2activity_framelayout,new PeopleFragment()).commit();
                        howto.setVisibility(View.INVISIBLE);
                        relativeLayout.setBackgroundResource(R.color.white);
                        return true;
                    case R.id.action_chat:
                        getFragmentManager().beginTransaction().replace(R.id.main2activity_framelayout, new ChatFragment()).commit();
                        howto.setVisibility(View.INVISIBLE);
                        relativeLayout.setBackgroundResource(R.color.white);
                        return true;
                    case R.id.action_account:
                        getFragmentManager().beginTransaction().replace(R.id.main2activity_framelayout, new AccountFragment()).commit();
                        howto.setVisibility(View.INVISIBLE);
                        relativeLayout.setBackgroundResource(R.color.white);

                }

                return false;
            }
        });



    }

    public int getMode(){
        return mode;
    }



}
