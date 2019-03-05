package com.example.kartat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class mapActivity extends AppCompatActivity {

    ImageButton goBack;
    ImageView map;
    TextView txtName;
    TextView txtCity;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String city = intent.getStringExtra("City");
        String jpg = intent.getStringExtra("urlimg");
        String web = intent.getStringExtra("urlweb");

        goBack = findViewById(R.id.goBack);
        map = findViewById(R.id.map);
        txtName = findViewById(R.id.Name);
        txtCity = findViewById(R.id.City);
        listview = findViewById(R.id.listlanes);

        txtName.setText(name);
        txtCity.setText(city);

        getwebsite(web);
        loadImageFromUrl(map,jpg);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Loads image from url and add image to imageview
    //String url, images url
    private void loadImageFromUrl(ImageView imageView, String url){
        Picasso.with(this).load(url)
                .error(R.drawable.notfound).resize(3000,3000).onlyScaleDown().centerInside()
                .into(imageView,new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
    }


    private void getwebsite(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder all_lanes = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("html>body>div>div>#rata-vaylakuvaukset>div>span");
                    for (Element link : links){
                        String lane_number = link.select("div>h4").text();
                        String lane_lenght_par = link.select("div>p").text();

                        all_lanes.append(lane_number)
                                .append("\n")
                                .append(lane_lenght_par)
                                .append("\n\n");
                    }
                } catch (IOException e){
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                mapActivity.this,
                                R.layout.lane_list,
                                R.id.lane,
                                all_lanes.toString().split("\n\n")){

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);

                                String lane_number = all_lanes.toString().split("\n\n")[position].split("\n")[0];
                                String length_par = all_lanes.toString().split("\n\n")[position].split("\n")[1];

                                TextView lane_number_view = view.findViewById(R.id.lane);
                                TextView par_length_view = view.findViewById(R.id.par_length);

                                lane_number_view.setText(lane_number);
                                par_length_view.setText(length_par);

                                return view;}};

                        listview.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
    }
}
