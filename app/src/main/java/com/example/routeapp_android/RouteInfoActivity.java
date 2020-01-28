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
    private ArrayList <Direction> directions;
    private TextView name;
    private TextView createdBy;
    private TextView estimatedTime;
    private TextView totalDistance;
    private TextView origin;
    private Client client = new Client();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        tableLayout = (TableLayout) findViewById(R.id.destinationsTableLayout);
        image = (ImageView)findViewById(R.id.routeImage);
        name = (TextView)findViewById(R.id.routeNameText);
        createdBy = (TextView)findViewById(R.id.createdByText);
        estimatedTime = (TextView)findViewById(R.id.estimatedTimeText);
        totalDistance = (TextView)findViewById(R.id.totalDistanceText);
        origin = (TextView)findViewById(R.id.originText);
        try{
            client.findRouteById(this,"7");
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
            Toast.makeText(this,"You pressed the button of the row with the id"+v.getId(),Toast.LENGTH_SHORT).show();
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
            directions = (ArrayList<Direction>)response.body();
            //directions.addAll((ArrayList<Direction>)response.body());
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
        Logger.getAnonymousLogger().severe("Drawing the map");
        drawMap();
        Logger.getAnonymousLogger().severe(route.getCoordinates().toString());
        Logger.getAnonymousLogger().severe("The size is :"+route.getCoordinates().size());
        end = (Button) findViewById(R.id.btnEnd);
        end.setOnClickListener(this);
        start = (Button) findViewById(R.id.btnStart);
        start.setOnClickListener(this);
        name.setText(route.getName());
        createdBy.setText(route.getCreatedBy().toString());
        int time = route.getEstimatedTime()/60;
        estimatedTime.setText(time+" min");
        totalDistance.setText(route.getTotalDistance()+" m");
        for (Coordinate_Route coordinate_route: route.getCoordinates()){

            if(coordinate_route.getCoordinate().getType().equals(Type.ORIGIN)) {
                for(Direction direction: directions) {
                    if (direction.getCoordinate().equals(coordinate_route.getCoordinate())) {
                        origin.setText(direction.getName());
                    }
                }
            }else{
                for(Direction direction: directions) {
                    if (direction.getCoordinate().equals(coordinate_route.getCoordinate())) {
                        text = direction.getName();
                    }
                }
            }
            row = new TableRow(this);
            //row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            row.setBackgroundColor(Color.parseColor("#DAE8FC"));
            if(!coordinate_route.getCoordinate().getType().equals(Type.ORIGIN)){
                button = new Button(this);
                button.setText("Set GPS coords");
                button.setId(coordinate_route.getCoordinate().getId().intValue());
                button.setOnClickListener(this);
                button.setGravity(Gravity.CENTER_HORIZONTAL);

                if(coordinate_route.getVisited() != null){
                    button.setEnabled(false);
                }else{
                    button.setEnabled(true);
                }

            }


            TextView tv = new TextView(this);

            tv.setText(text);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setMaxWidth(500);
            //tv.setMinHeight(200);

            row.addView(tv);
            row.addView(button);



            tableLayout.addView(row);
        }
        tableLayout.setColumnStretchable(0,true);
        //tableLayout.setStretchAllColumns(true);
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);

    }

    private void drawMap (){
        String coords = "";
        for (Coordinate_Route coordinate : route.getCoordinates()) {
            coords += "waypoint" + (coordinate.getOrder()-1) + "=" + coordinate.getCoordinate().getLatitude()+","+coordinate.getCoordinate().getLongitude() + "&";
            coords += "poix" + (coordinate.getOrder()-1) + "=" + coordinate.getCoordinate().getLatitude()+","+coordinate.getCoordinate().getLongitude() + ";";
            if (coordinate.getCoordinate().getType().equals(Type.ORIGIN)) {
                coords += "red;";
            } else if (coordinate.getVisited() == null) {
                coords += "blue;";
            } else {
                coords += "green;";
            }
            coords += "white;14;"+ coordinate.getOrder() +"&";
        }
        String imageUrl = "https://image.maps.api.here.com/mia/1.6/routing?app_id=w4M9GIVbS5uVCLiCyGKV&app_code=JOPGDZHGQJ7FpUVmbfm4KA&e=Q&" + coords + "lc=1652B4&lw=6&t=0&w=400&h=400";
        Picasso.get().load(imageUrl).into(image);
    }
}