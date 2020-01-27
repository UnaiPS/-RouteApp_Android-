package com.example.routeapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class RouteInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TableRow row;
    private Button button;
    private TableLayout tableLayout;
    private String text;
    private Button end;
    private Button start;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        tableLayout = (TableLayout) findViewById(R.id.destinationsTableLayout);
        image = (ImageView)findViewById(R.id.routeImage);
        String imageUrl = "https://image.maps.api.here.com/mia/1.6/routing?app_id=w4M9GIVbS5uVCLiCyGKV&app_code=JOPGDZHGQJ7FpUVmbfm4KA&waypoint0=43.3168342,-2.982215&waypoint1=43.3073,-2.974&w=400&h=400";
        Picasso.get().load(imageUrl).into(image);
        end = (Button) findViewById(R.id.btnEnd);
        end.setOnClickListener(this);
        start = (Button) findViewById(R.id.btnStart);
        start.setOnClickListener(this);
        for (int i = 1; i<10; i++){
            row = new TableRow(this);
            //row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundColor(Color.parseColor("#DAE8FC"));
            button = new Button(this);
            button.setText("GPS");
            button.setId(i);
            button.setOnClickListener(this);
            button.setGravity(Gravity.CENTER_HORIZONTAL);
            //button.setEnabled(false);

           // button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            text = "Sample dataaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "+i;
            TextView tv = new TextView(this);

            tv.setText(text);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setMaxWidth(50);
            tv.setMinHeight(200);

            row.addView(tv);
            row.addView(button);



            tableLayout.addView(row);
        }
        tableLayout.setColumnStretchable(0,true);
        //tableLayout.setStretchAllColumns(true);
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== end.getId()){
            Toast.makeText(this,"Pressed end button",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==start.getId()){
            Toast.makeText(this,"Pressed start button",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"You pressed the button of the table"+v.getId(),Toast.LENGTH_SHORT).show();
        }
    }
}