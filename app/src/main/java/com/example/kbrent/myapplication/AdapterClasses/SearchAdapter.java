package com.example.kbrent.myapplication.AdapterClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kbrent.myapplication.ContainerClasses.Search;
import com.example.kbrent.myapplication.R;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<Search> {

    private Context context;
    private ArrayList<Search> searchList = new ArrayList<>();

    public SearchAdapter(@NonNull Context context_, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Search> list) {
        super(context_, 0 , list);
        context = context_;
        searchList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.activity_search_item, parent, false);
        }

        Search currentSearch = searchList.get(position);

        ImageView image = listItem.findViewById(R.id.searchImage);
        image.setImageDrawable(currentSearch.imageType);

        TextView name = listItem.findViewById(R.id.searchTextList);
        name.setText(currentSearch.displayText);

        return listItem;
    }
}