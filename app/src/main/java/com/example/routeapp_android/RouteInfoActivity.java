package com.example.routeapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RouteInfoActivity extends AppCompatActivity {

    private TableRow row;
    private Button button;
    private LinearLayout linearLayout;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        linearLayout = (LinearLayout)findViewById(R.id.destinationsLinearLayout);
        for (int i = 1; i<10; i++){
            row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            button = new Button(this);
            button.setText("GPS");
            button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            text = "Sample data "+i;
            TextView tv = new TextView(this);
            tv.setText(text);

            row.addView(button);
            row.addView(tv);

            linearLayout.addView(row);
        }
    }
}