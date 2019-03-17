package com.example.kartat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class FragmentMaps extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        String jpgUrl = getArguments().getString("jpgUrl");
        ImageView imageView = view.findViewById(R.id.map);
        loadImageFromUrl(imageView,jpgUrl);
        return view;
    }

    //Loads image from url and add image to imageview
    //String url, images url
    private void loadImageFromUrl(final ImageView imageView, String url){
        Picasso.with(getActivity()).load(url)
                .error(R.drawable.notfound).resize(3000,3000).onlyScaleDown().centerInside()
                .into(imageView,new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getActivity(), "Valitettavasti kuvaa ei ole saatavilla",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
