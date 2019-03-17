package com.example.kartat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class FragmentsInfo extends Fragment {

    ListView listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        listview = view.findViewById(R.id.listlanes);
        String webUrl = getArguments().getString("webUrl");
        getwebsite(webUrl);
        return view;
    }


    private void getwebsite(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder all_lanes = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("html>body>div>div>#rata-vaylakuvaukset>div>span");
                    all_lanes.append(doc.select("html>body>div>div>div>div>span>p").text()).append("\n\n");
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getActivity(),
                                R.layout.lane_list,
                                R.id.lane,
                                all_lanes.toString().split("\n\n")){

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                try {
                                    TextView lane_number_view = view.findViewById(R.id.lane);
                                    TextView par_length_view = view.findViewById(R.id.par_length);

                                    String lane_number = all_lanes.toString().split("\n\n")[position].split("\n")[0].replace("NULL","");

                                    if(position==0){
                                        lane_number_view.setText("Radan kuvaus");
                                        par_length_view.setText(lane_number.split("Lue lisää")[0].replace("NULL",""));
                                    }
                                    else{
                                        String length_par = all_lanes.toString().split("\n\n")[position].split("\n")[1].replace("NULL","");
                                        lane_number_view.setText(lane_number);
                                        par_length_view.setText(length_par);
                                    }

                                } catch (Exception e){
                                    Log.i("Exceptions ",e.toString()+" "+url);
                                    Toast.makeText(getActivity(), "Valitettavasti väylä tietoja ei ole saatavilla",
                                            Toast.LENGTH_LONG).show();
                                }

                                return view;}};

                        listview.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
    }
}