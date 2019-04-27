package com.example.kbrent.myapplication.ActivityClasses;

import android.util.Log;
import android.widget.CompoundButton;

import com.example.kbrent.myapplication.ContainerClasses.User;

class buttonListener implements CompoundButton.OnCheckedChangeListener
{

    private String description;
    public buttonListener(String description) {
        this.description = description;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.e("HTTP", " clicking on event switch "+description+" val "+Boolean.toString(isChecked));
        if(description.toLowerCase().equals("male")) {
            User.filterMale = isChecked;
        } else if(description.toLowerCase().equals("female")) {
            User.filterFemale = isChecked;
        } else if(description.toLowerCase().equals("father")) {
            User.filterFather = isChecked;
        } else if(description.toLowerCase().equals("mother")) {
            User.filterMother = isChecked;
        } else {
            User.eventFilterMap.put(description, isChecked);
        }
    }
}
