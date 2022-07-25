/*
Tyler Vogel
11161080
tjv255
03/13/20
CMPT381 A3
 */
package com.example.chartextract;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ChartView chartView;
    ChartModel model;
    ChartController controller;
    AxisView axisView;
    DetailView detailView;
    InteractionModel iModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout root = findViewById(R.id.root);
        LinearLayout topLayout = findViewById(R.id.topLayout);

        //MVC
        model = new ChartModel();
        controller = new ChartController();
        iModel = new InteractionModel();
        chartView = new ChartView(this);
        detailView = new DetailView(this);
        axisView = new AxisView(this);

        // Add views to layouts
        topLayout.addView(detailView, new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,.5f));
        topLayout.addView(axisView, new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,.5f));
        root.addView(chartView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0,.8f));


        // Connect MVC
        chartView.setModel(model);
        chartView.setController(controller);
        chartView.setIModel(iModel);

        detailView.setModel(model);
        detailView.setIModel(iModel);

        axisView.setModel(model);
        axisView.setIModel(iModel);

        controller.setModel(model);
        controller.setIModel(iModel);

        model.addSubscriber(chartView);
        model.addSubscriber(detailView);
        iModel.addSubscriber(chartView);
        iModel.addSubscriber(axisView);
        iModel.addSubscriber(detailView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.choose_chart:
                chooseChart();
                return true;
            case R.id.screenshot:
                takeScreenshot();
                return true;
            case R.id.export_data:
                // exportData()
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu, menu);
//    }
//
//
////document/raw:/storage/emulated/0/Downloads/sample-chart.png
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData(); //The uri with the locations of the image
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            model.setChartImage(bitmap);
        }
        if (requestCode == 234 && resultCode == RESULT_OK) {
            Uri newFile = data.getData();
            try {
                ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(newFile, "w");
                FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
                Bitmap screen = Bitmap.createBitmap(chartView.getWidth(), chartView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screen);
                chartView.draw(canvas);
                screen.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void chooseChart(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a chart"), 123);
    }
    public void takeScreenshot(){
        Intent intent2 = new Intent(Intent.ACTION_CREATE_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("image/png")
                .putExtra(Intent.EXTRA_TITLE, "screenshot.png");
        startActivityForResult(intent2, 234);
    }
}
