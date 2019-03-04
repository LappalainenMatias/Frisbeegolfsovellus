package com.example.kartat;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String url = "https://frisbeegolfradat.fi/radat/haku";
    ListView listview;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.listview);
        FloatingActionButton search = findViewById(R.id.fabSearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });
        getwebsite();

    }


    //Loads image from url and add image to imageview
    //String url, images url
    private void loadImageFromUrl(ImageView imageView,String url){
        Picasso.with(this).load(url)
                .error(R.drawable.notfound).resize(300,300).centerInside()
                .into(imageView,new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    private void getwebsite(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder all_data = new StringBuilder();//Includes Name of the field, Place of the field (city), Amount of lanes, website url, jpg url
                final StringBuilder name_place_lanes = new StringBuilder();//Includes Name of the field, Place of the field (city), Amount of lanes
                final StringBuilder map_url = new StringBuilder();//Includes only jpg url (https://...jpg)
                String field_name_website_url = "";
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("table#radatlistaus>tbody>tr");
                    for (Element link : links){
                        String img_url = link.select("a[href]:contains(Kartta)").attr("href");
                        if(img_url == ""){img_url="Karttaa ei ole";}
                        String field_name = link.select("td.rataCol>a").text();
                        field_name_website_url = link.select("td.rataCol>a").attr("href")
                                .replace(" ","_");
                        String place = link.select("td.paikkaCol").text();
                        String lane = link.select("td").get(4).text();

                        all_data.append(field_name)
                                .append("\n")
                                .append(place)
                                .append("\n")
                                .append("Väyliä : "+lane)
                                .append("\n")
                                .append(img_url)
                                .append("\n")
                                .append(url+field_name_website_url)
                                .append("\n")
                                .append("\n");

                        name_place_lanes.append(field_name)
                                .append("\n")
                                .append(place)
                                .append("\n")
                                .append("Väyliä : "+lane)
                                .append("\n")
                                .append("\n");

                        map_url.append(img_url)
                                .append("\n");

                    }
                } catch (IOException e){
                    all_data.append("Error : "+field_name_website_url+"\n"+e.getMessage());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                MainActivity.this,
                                R.layout.activity_list,
                                R.id.textview,
                                name_place_lanes.toString().split("\n\n")){

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                ImageView image = view.findViewById(R.id.imageView);
                                loadImageFromUrl(image,map_url.toString().split("\n")[position]);
                                return view;}};
                        listview.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
    }
}

