package com.example.routeapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class RouteInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TableRow row;
    private Button button;
    private TableLayout tableLayout;
    private String text;
    private Button end;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        tableLayout = (TableLayout) findViewById(R.id.destinationsTableLayout);
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
            button.setEnabled(false);

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
            Toast.makeText(this,"You pressed one button of the table",Toast.LENGTH_SHORT).show();
        }
    }
}