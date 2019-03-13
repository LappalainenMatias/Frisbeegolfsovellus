package com.matias.kartat;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.kartat.R;

public class splash extends AppCompatActivity {

    ImageView imageView;

    static splash aSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        aSplash= this;
        imageView = findViewById(R.id.imageView);

        rotate(1);
    }

    void rotate(final int angle){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setRotation(angle);
                rotate(angle+3);
            }
        },10);
    }

    public static splash getInstance(){
        return aSplash;
    }
}
