package com.example.chartextract;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;


public class DetailView extends View implements ChartModelListener {
    private static final int SCALING_FACTOR = 2;

    ChartModel model;
    InteractionModel iModel;
    Paint paint;


    Context context;
    //controller

    public DetailView(Context aContext) {
        super(aContext);
        this.context = aContext;
        paint = new Paint();
        this.setBackgroundColor(Color.CYAN);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if(model.chartImage != null) {
                Circle c = iModel.getSelectedCircle();
                //clipBounds = canvas.getClipBounds();

                if(c!=null){
                    float normX = ((c.getCenterX())/iModel.viewWidth)*getWidth();
                    float normY = ((c.getCenterY())/iModel.viewHeight)*getHeight();
                    canvas.translate(-normX, -normY);
                    canvas.scale(SCALING_FACTOR,SCALING_FACTOR);
                    canvas.drawLine(0, normY / 2, getWidth(), normY / 2, paint);
                    canvas.drawLine(normX / 2, 0, normX / 2, getHeight(), paint);

                }
                else canvas.scale(1,1);
                Rect dstRectForRender;
                dstRectForRender = new Rect(0, 0, getWidth(), getHeight());
                canvas.drawBitmap(model.chartImage, null, dstRectForRender, null);

                // **IMPROVISATION**
                // Draw a point instead of cross hairs, could not find a way to scale canvas to
                // hone in on center pixel. It seems to only pan over the image without going out of
                // bounds
                if(c!=null){
                    float normX = ((c.getCenterX())/iModel.viewWidth)*getWidth();
                    float normY = ((c.getCenterY())/iModel.viewHeight)*getHeight();
                    canvas.drawLine(0, normY, getWidth(), normY, paint);
                    canvas.drawLine(normX, 0, normX, getHeight(), paint);

                }
//                canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, myPaint);
//                canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), myPaint);
//                canvas.save();
//                canvas.scale(scaleFactor, scaleFactor);

            }
        } catch (Exception ignored) {
        }

    }



    public void setModel(ChartModel aModel) {
        model = aModel;
    }
    public void setIModel(InteractionModel anIModel){
        iModel = anIModel;
    }

    public void setController() {
    }


    public void modelChanged() {
        this.invalidate();
    }
}

