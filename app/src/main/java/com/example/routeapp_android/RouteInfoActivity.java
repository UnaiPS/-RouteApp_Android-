package com.example.routeapp_android;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
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

import com.example.routeapp_android.control.CallbackReceiver;
import com.example.routeapp_android.control.Client;
import com.example.routeapp_android.model.Coordinate_Route;
import com.example.routeapp_android.model.Direction;
import com.example.routeapp_android.model.Route;
import com.example.routeapp_android.model.Type;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Response;

public class RouteInfoActivity extends AppCompatActivity implements View.OnClickListener, CallbackReceiver {

    private TableRow row;
    private Button button;
    private TableLayout tableLayout;
    private String text;
    private Button end;
    private Button start;
    private ImageView image;
    private Route route;
    private List<Direction> directions;
    private Client client = new Client();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        tableLayout = (TableLayout) findViewById(R.id.destinationsTableLayout);
        image = (ImageView)findViewById(R.id.routeImage);
        try{
            client.findRouteById(this,"1");
        }catch(Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        Button buttonDisable;
        if (v.getId()== end.getId()){
            Toast.makeText(this,"Pressed end button",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==start.getId()){
            Toast.makeText(this,"Pressed start button",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"You pressed the button of the row"+v.getId(),Toast.LENGTH_SHORT).show();
            buttonDisable=(Button)findViewById(v.getId());
            buttonDisable.setEnabled(false);
        }
    }

    @Override
    public void onSuccess(Response response) {
        if(response.body().getClass().equals(Route.class)){
            Logger.getAnonymousLogger().severe("Va a coger la ruta");
            route = ((Route)response.body());
            Logger.getAnonymousLogger().severe("Ha cogido la ruta: "+route.getName());
            try{
                client.findDirectionsByRoute(this,route.getId().toString());
            }catch (Exception e){

            }

        }else{
            Logger.getAnonymousLogger().severe("Va a coger las direcciones");
            directions.addAll((ArrayList<Direction>)response.body());
            Logger.getAnonymousLogger().severe("Hay "+directions.size()+" direcciones");
            onActivityShowing();
        }

    }

    @Override
    public void onError(Throwable t) {
        Logger.getAnonymousLogger().severe("Ha dado un error fatal :v"+t.getMessage());
    }

    private void onActivityShowing(){
        Logger.getAnonymousLogger().severe("Ha llegado aqui"+route.getName());
        String imageUrl = "https://image.maps.api.here.com/mia/1.6/routing?app_id=w4M9GIVbS5uVCLiCyGKV&app_code=JOPGDZHGQJ7FpUVmbfm4KA&waypoint0=43.3168342,-2.982215&waypoint1=43.3073,-2.974&w=400&h=400";
        Picasso.get().load(imageUrl).into(image);
        end = (Button) findViewById(R.id.btnEnd);
        end.setOnClickListener(this);
        start = (Button) findViewById(R.id.btnStart);
        start.setOnClickListener(this);
        for (Coordinate_Route coordinate_route: route.getCoordinates()){
            row = new TableRow(this);
            //row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundColor(Color.parseColor("#DAE8FC"));
            if(!coordinate_route.getCoordinate().getType().equals(Type.ORIGIN)){
                button = new Button(this);
                button.setText("Set GPS coords");
                button.setId(coordinate_route.getCoordinate().getId().intValue());
                button.setOnClickListener(this);
                button.setGravity(Gravity.CENTER_HORIZONTAL);

                if(coordinate_route.getVisited() == null){
                    button.setEnabled(false);
                }

            }

            int indexOfCoord = 0;

            if(directions.contains(coordinate_route.getCoordinate())){
                indexOfCoord = directions.indexOf(coordinate_route.getCoordinate());
            }
            if(indexOfCoord!=0){
                text = directions.get(indexOfCoord).getName();
            }else{
                text = "There is no name";
            }

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
}