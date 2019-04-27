package com.example.kbrent.myapplication.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.kbrent.myapplication.ContainerClasses.Filter;
import com.example.kbrent.myapplication.R;

import java.util.ArrayList;

class TextHolder {
    TextView bigText;
    TextView smallText;
    Switch buttonSwitch;
}

public class FilterAdapter extends ArrayAdapter<Filter> {

    ArrayList<Filter> filters;

    public FilterAdapter(Context context, ArrayList<Filter> filters) {
        super(context, 0, filters);
        this.filters = filters;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Filter filter = getItem(position);

        TextHolder textHolder = new TextHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_filter_node, parent, false);
            textHolder.bigText = convertView.findViewById(R.id.bigText);
            textHolder.smallText = convertView.findViewById(R.id.smallText);
            textHolder.buttonSwitch = convertView.findViewById(R.id.switchButton);
            convertView.setTag(textHolder);
        } else {
            textHolder = (TextHolder)convertView.getTag();
        }

        textHolder.bigText.setText(filter.bigText);
        textHolder.smallText.setText(filter.smallText);
        textHolder.buttonSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    filters.get(position).enabled = isChecked;
                    filters.get(position).listener.onCheckedChanged(buttonView, isChecked);
                });
        textHolder.buttonSwitch.setChecked(filter.enabled);

        return convertView;
    }

}
