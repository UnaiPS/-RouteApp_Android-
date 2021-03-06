package com.example.routeapp_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.routeapp_android.control.CallbackReceiver;
import com.example.routeapp_android.control.Client;
import com.example.routeapp_android.model.Coordinate;
import com.example.routeapp_android.model.Coordinate_Route;
import com.example.routeapp_android.model.Direction;
import com.example.routeapp_android.model.Route;
import com.example.routeapp_android.model.Type;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.logging.Logger;

import retrofit2.Response;

/**
 * This class is going to display all the info about the route to be done and can modify some route
 * params
 * @Author Unai Pérez Sánchez
 */
public class RouteInfoActivity extends AppCompatActivity implements View.OnClickListener, CallbackReceiver {

    private TableRow row;
    private Button button;
    private TableLayout tableLayout;
    private String text;
    private Button end;
    private Button start;
    private ImageView image;
    private static Route route;
    private Route routeCopy = new Route();
    private static ArrayList <Direction> directions;
    private TextView name;
    private TextView createdBy;
    private TextView estimatedTime;
    private TextView totalDistance;
    private TextView origin;
    private Client client = new Client();
    private Long idroute;
    private boolean gotData = false;


    //Crear coordenadas a partir de GPS del dispositivo
    private FusedLocationProviderClient fusedLocationClient;
    private static Coordinate_Route temporalCoordRoute = new Coordinate_Route();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);

        Intent intent = getIntent();
        idroute = (Long)intent.getSerializableExtra("ROUTE");

        tableLayout = (TableLayout) findViewById(R.id.destinationsTableLayout);
        image = (ImageView) findViewById(R.id.routeImage);
        name = (TextView) findViewById(R.id.routeNameText);
        createdBy = (TextView) findViewById(R.id.createdByText);
        estimatedTime = (TextView) findViewById(R.id.estimatedTimeText);
        totalDistance = (TextView) findViewById(R.id.totalDistanceText);
        origin = (TextView) findViewById(R.id.originText);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (!gotData) {
            try {
                client.findRouteById(this, idroute.toString());
                gotData = true;
            } catch (Exception e) {

            }
        } else {
            onActivityShowing();
        }

    }

    public static void setGotData(boolean gotData) {
        //RouteInfoActivity.gotData = gotData;
    }

    /**
     * This method is going to be run when a button is pressed
     * @param v
     */
    @Override
    public void onClick(View v) {
        Button buttonDisable;
        if (v.getId()== end.getId()){
            Toast.makeText(this,"Pressed end button",Toast.LENGTH_SHORT).show();

            buttonDisable = (Button)findViewById(R.id.btnEnd);
            boolean isAllCoordsVisited = true;
            for(Coordinate_Route coordinate_route: routeCopy.getCoordinates()){
                if(coordinate_route.getVisited()==null && coordinate_route.getOrder()!=1){
                    isAllCoordsVisited=false;
                    break;
                }
            }
            if(isAllCoordsVisited){
                route.setCoordinates(null);
                route.setEnded(true);
                try{
                    buttonDisable.setEnabled(false);
                    client.editRoute(this,route);
                }catch (Exception e){
                    buttonDisable.setEnabled(true);
                    route.setEnded(false);
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"All destinations must be visited",Toast.LENGTH_LONG).show();
            }
        }else if(v.getId()==start.getId()){
            Toast.makeText(this,"Pressed start button",Toast.LENGTH_SHORT).show();
            route.setCoordinates(null);
            route.setStarted(true);
            buttonDisable=(Button)findViewById(R.id.btnStart);
            try {
                buttonDisable.setEnabled(false);
                client.editRoute(this, route);
            }catch (Exception e){
                buttonDisable.setEnabled(true);
                route.setStarted(false);
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"You pressed the button of the row with the id"+v.getId(),Toast.LENGTH_SHORT).show();
            buttonDisable=(Button)findViewById(v.getId());
            buttonDisable.setEnabled(false);
            Logger.getAnonymousLogger().severe("Button id is: "+buttonDisable.getId());

            for(Coordinate_Route coor_rout: routeCopy.getCoordinates()){
                if(coor_rout.getCoordinate().getId() == v.getId()){
                    temporalCoordRoute=coor_rout;
                }
            }
            Logger.getAnonymousLogger().severe(temporalCoordRoute.toString());
            if(temporalCoordRoute != null){
                Logger.getAnonymousLogger().severe("Entra en el if");
                CallbackReceiver callback = this;
                Context context = this;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Logger.getAnonymousLogger().severe("No permission");
                    Toast.makeText(this,"Cannot complete the request, please allow location permissons",Toast.LENGTH_LONG).show();
                }else{
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    Logger.getAnonymousLogger().severe("Ha llegado al principio");
                                    if (location != null) {
                                        Logger.getAnonymousLogger().severe("Location no es null");
                                        // Logic to handle location object
                                        Coordinate coordinate = new Coordinate();
                                        coordinate.setLatitude(location.getLatitude());
                                        coordinate.setLongitude(location.getLongitude());
                                        coordinate.setType(Type.GPS);
                                        try{
                                            client.markDestinationAsVisited(callback,coordinate,routeCopy.getId(),temporalCoordRoute.getCoordinate().getId());
                                        }catch (Exception e){

                                        }

                                    }else{
                                        Logger.getAnonymousLogger().severe("Location es null");
                                        Toast.makeText(context,"Please, enable your GPS and try again",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }else{
                Toast.makeText(this,"Something went wrong, try again later",Toast.LENGTH_LONG).show();
                buttonDisable.setEnabled(true);
            }

        }
    }

    /**
     * This method is going to be executed when the response of the server goes fine
     * @param response A response object
     */
    @Override
    public void onSuccess(Response response) {
        try{
            if(response.body().getClass().equals(Route.class)){
                Logger.getAnonymousLogger().severe("Va a coger la ruta");
                route = ((Route)response.body());
                Logger.getAnonymousLogger().severe("Ha cogido la ruta: "+route.getName());
                routeCopy.setId(route.getId());
                routeCopy.setEnded(route.getEnded());
                routeCopy.setStarted(route.getStarted());
                routeCopy.setCoordinates(route.getCoordinates());
                routeCopy.setAssignedTo(route.getAssignedTo());
                routeCopy.setCreatedBy(route.getCreatedBy());
                routeCopy.setEstimatedTime(route.getEstimatedTime());
                routeCopy.setMode(route.getMode());
                routeCopy.setName(route.getName());
                routeCopy.setTotalDistance(route.getTotalDistance());
                routeCopy.setTrafficMode(route.getTrafficMode());
                routeCopy.setTransportMode(route.getTransportMode());
                client.findDirectionsByRoute(this,route.getId().toString());


            }else if (response.body()==null){
                Logger.getAnonymousLogger().severe("Aqui deberian de ir los botones start y end");
            }else if(response.body().getClass().equals(Long.class)){
                Logger.getAnonymousLogger().severe("The GPS coords have been saved correctly, the data will be reloaded");
                Bundle tempBundle = new Bundle();
                onCreate(tempBundle);
            }else{
                Logger.getAnonymousLogger().severe("Va a coger las direcciones");
                directions = (ArrayList<Direction>)response.body();
                Logger.getAnonymousLogger().severe("Hay "+directions.size()+" direcciones");
                onActivityShowing();
            }
        }catch (Exception e){

        }

    }

    /**
     * This method is going to be executed when the server cannot resolve the request
     * @param t A Throwable object
     */
    @Override
    public void onError(Throwable t) {
        Logger.getAnonymousLogger().severe("Ha habido un problema con la solicitud: "+t.getMessage());
    }

    /**
     * This method runs when the activity is showing, AKA when onCreate() is executed
     */
    private void onActivityShowing(){
        Logger.getAnonymousLogger().severe("Ha llegado aqui"+route.getName());
        Logger.getAnonymousLogger().severe("Drawing the map");
        drawMap();
        Logger.getAnonymousLogger().severe(route.getCoordinates().toString());
        Logger.getAnonymousLogger().severe("The size is :"+route.getCoordinates().size());
        end = (Button) findViewById(R.id.btnEnd);
        end.setOnClickListener(this);
        if(route.getEnded()){
            end.setEnabled(false);
        }
        start = (Button) findViewById(R.id.btnStart);
        start.setOnClickListener(this);
        if(route.getStarted()){
            start.setEnabled(false);
        }
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

            if(!coordinate_route.getCoordinate().getType().equals(Type.ORIGIN)){
                row = new TableRow(this);
                //row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
                row.setBackgroundColor(Color.parseColor("#403b37"));
                button = new Button(this);
                button.setText("Set GPS coords");
                button.setId(coordinate_route.getCoordinate().getId().intValue());
                button.setOnClickListener(this);
                button.setGravity(Gravity.CENTER_HORIZONTAL);
                button.setBackgroundColor(Color.parseColor("#332f2c"));
                button.setTextColor(Color.parseColor("#ffffff"));

                if(coordinate_route.getVisited() != null){
                    button.setEnabled(false);
                }else{
                    button.setEnabled(true);
                }
                TextView tv = new TextView(this);
                tv.setText(text);
                tv.setTextColor(Color.parseColor("#ffffff"));
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setMaxWidth(500);

                row.addView(tv);
                row.addView(button);

                tableLayout.addView(row);
            }

        }
        tableLayout.setColumnStretchable(0,true);
        //tableLayout.setStretchAllColumns(true);
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);

    }

    /**
     * This method draws the map in the image view depending on the coordinates
     */
    private void drawMap () {
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
        String imageUrl = "https://image.maps.api.here.com/mia/1.6/routing?app_id=" + getResources().getString(R.string.hereApiId) + "&app_code=" + getResources().getString(R.string.hereApiCode) + "&e=Q&" + coords + "lc=1652B4&lw=6&t=0&w=400&h=400";
        Picasso.get().load(imageUrl).into(image);
    }
}