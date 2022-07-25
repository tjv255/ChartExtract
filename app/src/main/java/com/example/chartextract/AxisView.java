/*
Tyler Vogel
11161080
tjv255
03/13/20
CMPT381 A3
 */
package com.example.chartextract;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.example.chartextract.ChartModel;
import com.example.chartextract.ChartModelListener;

public class AxisView extends LinearLayout implements ChartModelListener {
    //int minY, maxY, minX, maxX;

    LinearLayout yInfo;
    LinearLayout xInfo;
    TextView locationContainer;
    ChartModel model;
    InteractionModel iModel;

    EditText minY;
    EditText minX;
    EditText maxY;
    EditText maxX;


    public AxisView(Context context) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);

        //Add Y Fields
        yInfo = new LinearLayout(context);
        yInfo.setOrientation(LinearLayout.HORIZONTAL);
        yInfo.setBackgroundColor(Color.LTGRAY);
        TextView yAxis;

        yAxis = new TextView(context);
        yAxis.setText("Y Axis");
        minY = new EditText(context);
        minY.setHint("Min");
        minY.setText("0");
        minY.addTextChangedListener(textWatcher);
        minY.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        TextView yTo = new TextView(context);
        yTo.setText("to");
        maxY = new EditText(context);
        maxY.setHint("Max");
        maxY.setText("100");
        maxY.addTextChangedListener(textWatcher);
        maxY.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        yInfo.addView(yAxis, new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,.20f ));
        yInfo.addView(minY,new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,.30f ));
        yInfo.addView(yTo,new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,.10f ));
        yInfo.addView(maxY,new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,.30f ));
        this.addView(yInfo, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,.36f));


        //Add X Fields
        xInfo = new LinearLayout(context);
        xInfo.setOrientation(LinearLayout.HORIZONTAL);
        xInfo.setBackgroundColor(Color.LTGRAY);
        TextView xAxis;

        xAxis = new TextView(context);
        xAxis.setText("X Axis");
        minX = new EditText(context);
        minX.setHint("Min");
        minX.setText("0");
        minX.addTextChangedListener(textWatcher);
        minX.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        TextView xTo = new TextView(context);
        xTo.setText("to");
        maxX = new EditText(context);
        maxX.setHint("Max");
        maxX.setText("100");
        maxX.addTextChangedListener(textWatcher);
        maxX.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        xInfo.addView(xAxis, new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,.20f ));
        xInfo.addView(minX,new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,.30f ));
        xInfo.addView(xTo,new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,.10f ));
        xInfo.addView(maxX,new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,.30f ));
        this.addView(xInfo, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,.36f));


        // Add Location Label
        locationContainer = new TextView(context);
        locationContainer.setText("0,0");

        locationContainer.setBackgroundColor(Color.GRAY);
        locationContainer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26 );
        locationContainer.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.addView(locationContainer, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,.27f));


    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                iModel.setMaxX(Integer.parseInt(maxX.getText().toString()));
                iModel.setMaxY(Integer.parseInt(maxY.getText().toString()));
                iModel.setMinX(Integer.parseInt(minX.getText().toString()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                iModel.setMinY(Integer.parseInt(minY.getText().toString()));

            } catch ( Exception ignored ) { }

        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    public void setModel(ChartModel aModel){
        model = aModel;
    }
    public void setIModel(InteractionModel anIModel) {
        iModel = anIModel;
        iModel.setMinY(0);
        iModel.setMaxY(100);
        iModel.setMinX(0);
        iModel.setMaxX(100);
    }

    @Override
    public void modelChanged() {
        try {
            if (iModel.getSelectedX() > 10000 || iModel.getSelectedX() < -10000 || iModel.getSelectedY() > 10000 || iModel.getSelectedY() < -10000) {
                locationContainer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                locationContainer.setText((int) iModel.getSelectedX() + "," + (int) iModel.getSelectedY());
            } else {
                locationContainer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
                locationContainer.setText(iModel.getSelectedX() + "," + iModel.getSelectedY());
            }
        } catch (Exception ignored) { }
    }
}
