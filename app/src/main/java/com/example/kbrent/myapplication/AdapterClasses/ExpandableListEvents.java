package com.example.kbrent.myapplication.AdapterClasses;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kbrent.myapplication.ContainerClasses.Event;
import com.example.kbrent.myapplication.ContainerClasses.Person;
import com.example.kbrent.myapplication.ContainerClasses.User;
import com.example.kbrent.myapplication.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

public class ExpandableListEvents extends BaseExpandableListAdapter {

    private Context _context;
    private List<Event> events;
    public LayoutInflater inflater;

    public ExpandableListEvents(Context context, List<Event> events) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this._context = context;
        this.events = events;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.events.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) { convertView = inflater.inflate(R.layout.expandable_child,null); }

        TextView expandText = convertView.findViewById(R.id.expandableText);
        Event event = events.get(childPosition);
        Person person  = User.peopleMap.get(event.getPersonID());

        expandText.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")" +
                System.getProperty("line.separator")+person.getFirstName() + " " + person.getLastName());

        ImageView markerImage = convertView.findViewById(R.id.expandableImageIcon);
        Drawable markerDrawable = new IconDrawable(_context, FontAwesomeIcons.fa_map_marker).color(-7829368 ).sizeDp(40);
        markerImage.setImageDrawable(markerDrawable);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.events.size();
    }

    @Override
    public Object getGroup(int groupPosition) { return "EVENT"; }

    @Override
    public int getGroupCount() { return 1; }

    @Override
    public long getGroupId(int groupPosition) { return groupPosition; }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) { convertView = inflater.inflate(R.layout.expandable_header,null); }

        TextView textView = convertView.findViewById(R.id.expandableListHeader);
        textView.setText("LIFE EVENTS");

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
