package com.example.kartat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.ShadowLayout;
import com.matias.kartat.splash;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    String url = "https://frisbeegolfradat.fi/radat/haku";
    ListView listview;
    EditText edittext;

    Boolean list_view_full = false;
    Boolean getDataStarted = false;
    FrameLayout frame;
    ShadowLayout shadow;

    ArrayList<listData> all_data = new ArrayList<>();//
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Intent myIntent = new Intent(MainActivity.this, splash.class);
        MainActivity.this.startActivity(myIntent);

        listview = findViewById(R.id.listview);
        edittext = findViewById(R.id.search);
        frame = findViewById(R.id.framelayout);
        shadow = findViewById(R.id.shadow);

        shadow.setVisibility(View.INVISIBLE);
        edittext.setVisibility(View.INVISIBLE);
        frame.setVisibility(View.INVISIBLE);

        tryGetData();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent myIntent = new Intent(MainActivity.this, mapActivity.class);

                TextView txtName = arg1.findViewById(R.id.Name);
                TextView txtCity = arg1.findViewById(R.id.Place);
                TextView imgurl = arg1.findViewById(R.id.urljpg);
                TextView weburl = arg1.findViewById(R.id.urlweb);

                String name = txtName.getText().toString();
                String city = txtCity.getText().toString();
                String urlimg = imgurl.getText().toString();
                String urlweb = weburl.getText().toString();

                myIntent.putExtra("Name", name);
                myIntent.putExtra("City", city);
                myIntent.putExtra("urlimg", urlimg);
                myIntent.putExtra("urlweb", urlweb);

                MainActivity.this.startActivity(myIntent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
        adapter = new CustomAdapter(all_data,MainActivity.this,listview);

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(list_view_full){
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    void tryGetData(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(getDataStarted==false) {
                    getwebsite();
                    tryGetData();
                }
            }
        }, 5000);
    }

    private void getwebsite() {
        try{
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("table#radatlistaus>tbody>tr");
                    getDataStarted = true;
                    for (Element link : links) {

                        String img_url = link.select("a[href]:contains(Kartta)").attr("href");
                        if (img_url == "") {
                            img_url = "Karttaa ei ole";
                        }
                        String field_name = link.select("td.rataCol>a").text();
                        String field_name_website_url = link.select("td.rataCol>a")
                                .attr("href")
                                .replace(" ", "_");
                        String place = link.select("td.paikkaCol").text();
                        String lane = link.select("td").get(4).text();

                        all_data.add(new listData(field_name, place, "Väyliä : " + lane, img_url, "https://frisbeegolfradat.fi" + field_name_website_url));
                    }

                } catch (IOException e) {
                    Log.i("Error", e.toString());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listview.setAdapter(adapter);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                list_view_full = true;
                            }
                        }, 500);
                        splash.getInstance().finish();
                        edittext.setVisibility(View.VISIBLE);
                        frame.setVisibility(View.VISIBLE);
                        shadow.setVisibility(View.VISIBLE);

                    }
                });

            }

        }).start();
        }catch (Exception e){
            Log.i("Error ",e.toString());
            Toast.makeText(MainActivity.this, "Verkko yhteyttä ei saatu muodostettua", Toast.LENGTH_LONG).show();
        }
    }

}




