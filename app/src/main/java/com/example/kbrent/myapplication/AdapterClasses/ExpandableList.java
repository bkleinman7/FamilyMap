package com.example.kbrent.myapplication.AdapterClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kbrent.myapplication.ContainerClasses.Person;
import com.example.kbrent.myapplication.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableList extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private Context _context;
    private HashMap<Person, String> persons;
    private ArrayList<Person> personArrayList;

    public ExpandableList(Context context, HashMap<Person, String> persons) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this._context = context;
        this.persons = persons;
        this.personArrayList = new ArrayList<>();
        this.personArrayList.addAll(this.persons.keySet());
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.personArrayList.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.expandable_child,null);
        }

        TextView itemText = convertView.findViewById(R.id.expandableText);
        Person person = this.personArrayList.get(childPosition);

        String item_content = person.getFirstName() + " " + person.getLastName() + System.getProperty("line.separator") +
                this.persons.get(person);

        itemText.setText(item_content);

        ImageView markerImage = convertView.findViewById(R.id.expandableImageIcon);

        Drawable genderIcon;

        if(person.getGender().equals("m")) {
            genderIcon = new IconDrawable(_context, FontAwesomeIcons.fa_male).color(-16776961).sizeDp(40);
        } else {
            genderIcon = new IconDrawable(_context, FontAwesomeIcons.fa_female).color(-65536).sizeDp(40);
        }

        markerImage.setImageDrawable(genderIcon);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.persons.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return "FAMILY";
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandable_header,null);
        }

        TextView textView = convertView.findViewById(R.id.expandableListHeader);

        textView.setText("FAMILY");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

