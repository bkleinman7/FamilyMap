package com.example.kbrent.myapplication.ContainerClasses;

import android.graphics.drawable.Drawable;

public class Search {

    public String displayText;
    public String type;
    public String typeID;
    public Drawable imageType;

    public Search(String displayText, String type, String typeID, Drawable imageType)
    {
        this.displayText = displayText;
        this.imageType = imageType;
        this.type = type;
        this.typeID = typeID;
    }

}
