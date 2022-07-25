/*
Tyler Vogel
11161080
tjv255
03/13/20
CMPT381 A3
 */
package com.example.chartextract;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ChartModel {
    //ArrayList<Handles>
    Bitmap chartImage;
    String curLocation;

    ArrayList<ChartModelListener> subscribers;
    public ChartModel(){
        chartImage = null;
        curLocation = "(0,0)";
        subscribers = new ArrayList<>();
    }


    public void setChartImage(Bitmap anImage){
        chartImage = anImage;
        notifySubscribers();
    }

    public void addSubscriber(ChartModelListener subscriber) {
        subscribers.add(subscriber);
    }

    private void notifySubscribers() {
        for (ChartModelListener sl : subscribers) {
            sl.modelChanged();
        }
    }

}