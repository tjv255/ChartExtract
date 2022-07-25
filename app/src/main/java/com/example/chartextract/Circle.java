/*
Tyler Vogel
11161080
tjv255
03/13/20
CMPT381 A3
 */
package com.example.chartextract;
import android.graphics.Point;
import android.util.Pair;


public class Circle {
    float radius;
    float centerX;
    float centerY;


    public Circle(float x, float y, float aRadius){
        radius = aRadius;
        centerX = x;
        centerY = y;
    }

    public boolean contains(float x, float y) {
        // Increment on all sides to help fix the fat finger problem
        return (x <= centerX+radius+30 && x >= centerX-radius-30 && y<= centerY+radius+30 && y>= centerY-radius-30);
    }
    public void move(float dx, float dy){
        centerX = centerX+dx;
        centerY = centerY+dy;
    }
    public void setCenter(float x, float y){
        centerX = x;
        centerY = y;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }


    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }


    public float getRadius() {
        return radius;
    }
}
