package com.example.kartat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends FragmentActivity {

    String url = "https://frisbeegolfradat.fi/radat/haku";
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.listview);
        final FloatingActionButton search = findViewById(R.id.fabsearch);
        getwebsite();
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
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
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
                                R.id.Name,
                                name_place_lanes.toString().split("\n\n")){

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);

                                String name=all_data.toString().split("\n\n")[position].split("\n")[0];
                                String place=all_data.toString().split("\n\n")[position].split("\n")[1];
                                String amount_lanes=all_data.toString().split("\n\n")[position].split("\n")[2];
                                String imgstring=all_data.toString().split("\n\n")[position].split("\n")[3];
                                String webstring=all_data.toString().split("\n\n")[position].split("\n")[4];

                                TextView nameView = view.findViewById(R.id.Name);
                                TextView placeView = view.findViewById(R.id.Place);
                                TextView lanesView = view.findViewById(R.id.Lanes);
                                TextView urlimg = view.findViewById(R.id.urljpg);
                                TextView urlweb = view.findViewById(R.id.urlweb);

                                nameView.setText(name);
                                placeView.setText(place);
                                lanesView.setText(amount_lanes);
                                urlimg.setText(imgstring);
                                urlweb.setText(webstring);

                                return view;}};
                        listview.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
    }
}

