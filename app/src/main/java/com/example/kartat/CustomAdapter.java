package com.example.kartat;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<listData> implements Filterable {

    ArrayList<listData> dataList;
    ArrayList<listData> original_list;
    ListView listview;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtPlace;
        TextView txtLanes;
        TextView txtImg;
        TextView txtWeb;
    }

    public CustomAdapter(ArrayList<listData> data, Context context, ListView vList) {
        super(context, R.layout.activity_list, data);
        this.dataList = data;
        this.mContext=context;
        this.original_list = dataList;
        this.listview = vList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        listData dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_list, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.Name);
            viewHolder.txtPlace = (TextView) convertView.findViewById(R.id.Place);
            viewHolder.txtLanes= (TextView) convertView.findViewById(R.id.Lanes);
            viewHolder.txtImg = (TextView) convertView.findViewById(R.id.urljpg);
            viewHolder.txtWeb= (TextView) convertView.findViewById(R.id.urlweb);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtPlace.setText(dataModel.getPlace());
        viewHolder.txtLanes.setText(dataModel.getAmount_lanes());
        viewHolder.txtImg.setText(dataModel.getImgstring());
        viewHolder.txtWeb.setText(dataModel.getWebstring());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                try {
                    dataList = (ArrayList<listData>) results.values;
                    CustomAdapter adapter = new CustomAdapter(dataList, mContext, listview);
                    listview.setAdapter(adapter);
                }
                catch (Exception e){
                    Log.i("Error ", e.toString());
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                dataList = original_list;

                FilterResults results = new FilterResults();
                ArrayList<listData> FilteredArrayNames = new ArrayList<listData>();

                constraint = constraint.toString().toLowerCase();
                for (listData list_object : dataList) {
                    String all_txt = list_object.getName()+" "+list_object.getPlace()+" "+list_object.getAmount_lanes()+" "+list_object.getImgstring()+" "+list_object.getWebstring();
                    if (all_txt.toLowerCase().contains(constraint.toString()))  {
                        FilteredArrayNames.add(list_object);
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }
    }

