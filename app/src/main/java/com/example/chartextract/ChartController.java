/*
Tyler Vogel
11161080
tjv255
03/13/20
CMPT381 A3
 */
package com.example.chartextract;

import android.view.MotionEvent;
import android.view.View;

public class ChartController implements View.OnTouchListener {
    ChartModel model;
    InteractionModel iModel;


    private enum State {READY,CREATE, RUBBER_BAND, DRAGGING};
    private State currentState = State.READY;

    // (minX,minY) = top left point, (maxX,maxY) = bottom right point
    private float prevNormX, prevNormY;
    private boolean chartSet;
    Circle temp;


    public ChartController(){
        chartSet = false;
        temp = null;
    }


    public boolean onTouch(View v, MotionEvent event) {
        if(chartSet) {
            float normX = event.getX();
            float normY = event.getY();
            float normDX = normX - prevNormX;
            float normDY = normY - prevNormY;
            prevNormX = normX;
            prevNormY = normY;

            switch (currentState) {
                case READY:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Context: on background when points don't exist yet
//                            if (iModel.handles.isEmpty()) {
//                                // Side effects:
//                                // none
//                                // Move to new state:
//                                currentState = State.CREATE_OR_RUBBER_BAND;
                            if (iModel.checkHandlesHit(normX, normY)) {
                                // Context: on handle
                                // Side Effects: grab handle
                                Circle c = iModel.find(normX, normY);
                                iModel.setSelected(c);
                                currentState = State.RUBBER_BAND;
                            } else if(iModel.checkPointsHit(normX, normY)) {
                                // Context: on point
                                // Side Effects: grab point
                                Circle c = iModel.find(normX, normY);
                                iModel.setSelected(c);
                                currentState = State.DRAGGING;
                            }
                            else{
                                // Context: on background
                                // SideEffect: Move to new state
                                iModel.setSelected( new Circle(normX, normY, 15 ));
                                iModel.selectedX = (normX);
                                iModel.selectedY = (normY);
                                currentState = State.CREATE;
                            }
                            break;
                    }
                    break;
                case CREATE:
                    switch (event.getAction()) {
                        // Event: touch up
                        case MotionEvent.ACTION_UP:
                            //context: none
                            //Side Effects: create a point
                            iModel.setSelected(null);
                            iModel.createPoint(normX, normY);
                            currentState = State.READY;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //context: none
                            //SideEffects: Either create an elastic band
                            // and move to new state, or none.
                            iModel.movePoint(iModel.getSelectedCircle(), normDX, normDY);
                            currentState = State.CREATE;
                            break;
                    }
                    break;
                case RUBBER_BAND:
                    switch(event.getAction()){
                        case MotionEvent.ACTION_MOVE:
                            // Context: While rubber band is selected
                            // SideEffect: Move rubber band
                            if (normX >  20 && normX < iModel.viewWidth - 20 && normY > 20 && normY < iModel.viewHeight - 20) {
                                iModel.moveHandle(iModel.selectedCircle, normDX, normDY);
                            }
                            currentState = State.RUBBER_BAND;
                            break;
                        case MotionEvent.ACTION_UP:
                            // Context: while rubber band is selected
                            // SideEffect: Let go of rubber band
                            iModel.setSelected(null);
                            currentState = State.READY;
                            break;

                    }
                    break;
                case DRAGGING:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            // Context: while a graph point is selected
                            // SideEffect: Move selected graph point
                            if (normX > 20 && normX < iModel.viewWidth - 20 && normY > 20 && normY < iModel.viewHeight + 20) {
                                iModel.movePoint(iModel.selectedCircle, normDX, normDY);
                            }
                            currentState = State.DRAGGING;
                            break;
                        case MotionEvent.ACTION_UP:
                            // Context: while a graph point is selected
                            // SE: Let go of selected graph point
                            iModel.setSelected(null);
                            currentState = State.READY;
                            break;
                    }
                    break;
            }
        }
        return true;
    }
    public void setModel(ChartModel aModel){ model = aModel; }
    public void setIModel(InteractionModel anIModel){ iModel = anIModel; }

    public void setChartSet(boolean b){
        chartSet = b;
    }


}
