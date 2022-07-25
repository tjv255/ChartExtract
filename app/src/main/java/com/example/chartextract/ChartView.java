/*
Tyler Vogel
11161080
tjv255
03/13/20
CMPT381 A3
 */

package com.example.chartextract;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;


public class ChartView extends View implements ChartModelListener {
    ChartModel model;
    InteractionModel iModel;
    ChartController controller;
    Paint paint;
    Paint borderPaint; // used to put borders on yellow point circles
    boolean chartSet;


    public ChartView(Context aContext){
        super(aContext);
        paint = new Paint();
        borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStrokeWidth(6);
        borderPaint.setStyle(Paint.Style.STROKE);
        this.setFocusable(true);
        chartSet = false;
        this.setBackgroundColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try{
            // Draw graph
            Rect dstRectForRender;
            dstRectForRender = new Rect(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(model.chartImage, null, dstRectForRender, null);
            iModel.setViewSize(getWidth(), getHeight());
            controller.setChartSet(true);

            // Draw rubber band
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
            for(int i=0; i<4; i++){
                //canvas.drawCircle(iModel.points[i].x, iModel.points[i].y, 50, paint);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(iModel.handles.get(i).centerX, iModel.handles.get(i).centerY, iModel.handles.get(i).radius, paint);
            }
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(iModel.selectionRect, paint);

            //Draw points
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.FILL);
            for(Circle p : iModel.points){
                canvas.drawCircle(p.centerX, p.centerY, p.radius, paint);
                canvas.drawCircle(p.centerX, p.centerY, p.radius, borderPaint);
            }

        }catch(Exception ignored){ }

    }



    public void setModel(ChartModel aModel) {
        model = aModel;
    }
    public void setIModel(InteractionModel anIModel){
        iModel = anIModel;
    }
    public void setController(ChartController aController){
        controller = aController;
        this.setOnTouchListener(controller);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        iModel.setViewSize(this.getWidth(), this.getHeight());
    }

    public void modelChanged() {
        this.invalidate();
    }


}
