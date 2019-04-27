package com.example.kbrent.myapplication.ContainerClasses;

import android.widget.CompoundButton;

public class Filter {

    public String bigText;
    public String smallText;
    public boolean enabled;
    public CompoundButton.OnCheckedChangeListener listener;

    public Filter(String bigText, String smallText, boolean enabled,
                  CompoundButton.OnCheckedChangeListener listener) {
        this.bigText = bigText;
        this.smallText = smallText;
        this.enabled = enabled;
        this.listener = listener;
    }

}
