package com.quanlichitieu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartScreenActivity extends AppCompatActivity {
    private static int SPLASH=2000;
    Animation animation;
    private ImageView imageView;
    private TextView appName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        animation= AnimationUtils.loadAnimation(this,R.anim.animation);

        imageView=findViewById(R.id.imageView);
        appName=findViewById(R.id.appName);
        imageView.setAnimation(animation);
        appName.setAnimation(animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();



            }
        },SPLASH);
    }

    private void nextActivity() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent=new Intent(StartScreenActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }
        else{
            Intent intent=new Intent(StartScreenActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }

    }
}