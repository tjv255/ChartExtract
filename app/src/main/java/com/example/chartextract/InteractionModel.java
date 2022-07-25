/*
Tyler Vogel
11161080
tjv255
03/13/20
CMPT381 A3
 */
package com.example.chartextract;

import android.graphics.Rect;

import java.math.BigDecimal;
import java.util.ArrayList;

// This interaction model stores all of the information for the chart points from question 2

public class InteractionModel {

    ArrayList<ChartModelListener> subscribers;
    ArrayList<Circle> handles;
    float viewWidth, viewHeight;
    Circle selectedCircle;
    float selectedX, selectedY;
    Rect selectionRect;
    ArrayList<Circle> points;
    //Selection bounds;

    // the bounds within the rubber band on ChartView, set on AxisView
    private float minY, maxY;
    private float minX, maxX;

    public InteractionModel(){
        subscribers = new ArrayList<>();
        handles = new ArrayList<>();
        points = new ArrayList<>();

        selectedX = 0;
        selectedY = 0;
    }
    public void setViewSize(float width, float height){
        viewWidth = width;
        viewHeight = height;
        if(handles.isEmpty()){
            setHandles(viewWidth/8,viewHeight/8, viewWidth/3, viewHeight/3);
        }
    }

    public void setHandles(float x, float y, float dX, float dY) {
        handles.add( new Circle(x, y, 20));
        handles.add( new Circle(dX, y, 20));
        handles.add( new Circle(x, dY, 20));
        handles.add( new Circle(dX, dY, 20));
        selectionRect = new Rect((int) handles.get(0).getCenterX(), (int) handles.get(0).getCenterY(),
                (int) handles.get(3).getCenterX(), (int) handles.get(3).getCenterY());
        notifySubscribers();
    }
    public void createPoint(float x, float y){
        Circle newPoint = new Circle(x, y, 15);

        selectedX = minX + ((newPoint.getCenterX() - handles.get(2).getCenterX()) / selectionRect.width()) * (maxX-minX);
        // Choose decimal places based on digit size
        if(selectedX < 1 && selectedX>-1) selectedX = round(selectedX, 4);
        else if(selectedX<100 && selectedX>-100)  selectedX = round(selectedX, 2);
        else if (selectedX<1000 && selectedX>-1000) selectedX = round(selectedX, 1);
        else selectedX = round(selectedX, 0);

        selectedY = minY + ((handles.get(2).getCenterY() - newPoint.getCenterY()) / selectionRect.height()) * (maxY - minY);
        // Choose decimal points based on digit size
        if(selectedY < 1 && selectedY>-1) selectedY = round(selectedY, 4);
        else if(selectedY<100 && selectedY>-100) selectedY = round(selectedY, 2);
        else if (selectedY<1000 && selectedY>-1000) selectedY = round(selectedY, 1);
        else selectedY = round(selectedY, 0);

        points.add( newPoint );
        notifySubscribers();
    }
//    public void createTemp(float x, float y){
//        Circle temp = new Circle(x, y, 15);
//
//    }

    public boolean checkHandlesHit(float x, float y) {
        if (handles.isEmpty()){
            return false;
        }
        return handles.stream().anyMatch(h -> h.contains(x,y));
    }
    public boolean checkPointsHit(float x, float y) {
        if(points.isEmpty()){
            return false;
        }
        return points.stream().anyMatch(p -> p.contains(x,y));
    }
    public Circle find(float x, float y) {
        Circle found = null;
        for (Circle h : handles) {
            if (h.contains(x,y)) {
                found = h;
                return found;
            }
        }
        for(Circle p: points) {
            if (p.contains(x, y)) {
                found = p;
            }
        }
        return found;
    }

    public void setSelected(Circle c) {
        selectedCircle = c;
        notifySubscribers();
    }
    public void moveHandle(Circle h, float dx, float dy) {
        h.move(dx, dy);
        if (h.equals(handles.get(0))) {
            handles.get(1).move(0, dy);
            handles.get(2).move(dx, 0);
        } else if (h.equals(handles.get(1))) {
            handles.get(0).move(0, dy);
            handles.get(3).move(dx, 0);
        } else if (h.equals(handles.get(2))) {
            handles.get(0).move(dx, 0);
            handles.get(3).move(0, dy);
        } else if (h.equals(handles.get(3))) {
            handles.get(1).move(dx, 0);
            handles.get(2).move(0, dy);
        }
        selectionRect = new Rect((int) handles.get(0).getCenterX(), (int) handles.get(0).getCenterY(),
                (int) handles.get(3).getCenterX(), (int) handles.get(3).getCenterY());
        notifySubscribers();
    }
    public void movePoint(Circle c, float dx, float dy){
        c.move(dx, dy);

        selectedX = minX + ((c.getCenterX() - handles.get(2).getCenterX()) / selectionRect.width()) * (maxX-minX);
        // Choose decimal places based on digit size
        if(selectedX < 1 && selectedX>-1) selectedX = round(selectedX, 4);
        else if(selectedX<100 && selectedX>-100)  selectedX = round(selectedX, 2);
        else if (selectedX<1000 && selectedX>-1000) selectedX = round(selectedX, 1);
        else selectedX = round(selectedX, 0);

        selectedY = minY + ((handles.get(2).getCenterY() - c.getCenterY()) / selectionRect.height()) * (maxY - minY);
        // Choose decimal points based on digit size
        if(selectedY < 1 && selectedY>-1) selectedY = round(selectedY, 4);
        else if(selectedY<100 && selectedY>-100) selectedY = round(selectedY, 2);
        else if (selectedY<1000 && selectedY>-1000) selectedY = round(selectedY, 1);
        else selectedY = round(selectedY, 0);
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

    public Circle getSelectedCircle(){
        return selectedCircle;
    }

    public float getSelectedX() {
        return selectedX;
    }

    public float getSelectedY() {
        return selectedY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }
    private static float round(float d, int decimalPlace){
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
