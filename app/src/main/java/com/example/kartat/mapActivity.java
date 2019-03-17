package com.example.kartat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class mapActivity extends AppCompatActivity {

    ImageButton goBack;
    TextView txtName;
    TextView txtCity;
    ImageButton maps_button;
    String jpg;
    String web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String city = intent.getStringExtra("City");
        jpg = intent.getStringExtra("urlimg");
        web = intent.getStringExtra("urlweb");

        goBack = findViewById(R.id.goBack);
        txtName = findViewById(R.id.Name);
        txtCity = findViewById(R.id.City);
        maps_button = findViewById(R.id.maps_button);
        final ImageButton btnMap = findViewById(R.id.maps);
        final ImageButton btnInfo = findViewById(R.id.info);
        final ImageButton btnReview = findViewById(R.id.review);

        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMap.startAnimation(animScale);
                Bundle bundle = new Bundle();
                bundle.putString("jpgUrl", jpg);
                FragmentTransaction fragmentTransaction = mapActivity.this.getSupportFragmentManager().beginTransaction();
                FragmentMaps fragobj = new FragmentMaps();
                fragobj.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment,fragobj);
                fragmentTransaction.commit();
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnInfo.startAnimation(animScale);
                Bundle bundle = new Bundle();
                bundle.putString("webUrl", web);
                FragmentTransaction fragmentTransaction = mapActivity.this.getSupportFragmentManager().beginTransaction();
                FragmentsInfo fragobj = new FragmentsInfo();
                fragobj.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment,fragobj);
                fragmentTransaction.commit();
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReview.startAnimation(animScale);
                FragmentTransaction fragmentTransaction = mapActivity.this.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment,new FragmentReview());
                fragmentTransaction.commit();
            }
        });

        txtName.setText(name);
        txtCity.setText(city);

        maps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maps_button.startAnimation(animScale);
                open_maps(web);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack.startAnimation(animScale);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void open_maps(final String website_url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(website_url).get();
                    String maps_url = doc.select("html>body>div>div>div>div>ul>span>li>p>a").attr("href");
                    String lat = maps_url.split("q=")[1].split(",")[0];
                    String lng = maps_url.split("q=")[1].split(",")[1];
                    String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    mapActivity.this.startActivity(intent);
                } catch (Exception e) {
                    Log.i("",e.toString());
                }
            }
        }).start();

    }
}
