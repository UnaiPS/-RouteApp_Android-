package com.example.routeapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.routeapp_android.control.CallbackReceiver;
import com.example.routeapp_android.control.Client;
import com.example.routeapp_android.model.Route;
import com.example.routeapp_android.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Response;

public class ActivityRoute extends AppCompatActivity implements CallbackReceiver, View.OnClickListener {
    private ListView list;
    private ArrayAdapter<String> adaptador;
    private ArrayList<String> names;
    List<String> listNames;
    LottieAnimationView animation;
    private ImageView image;
    private Client client = new Client();
    private User user;
    private Intent intent2;
    private ArrayList<Route> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        animation = findViewById(R.id.notfound);
        animation.setVisibility(View.GONE);
        image = findViewById(R.id.refresh);
        image.setImageResource(R.drawable.refresh);
        image.setOnClickListener(this);
        intent2 = new Intent(this, RouteInfoActivity.class);


        list = (ListView) findViewById(R.id.lvRoutes);
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("USER");


        try {
            client.findRoutesByAssignedTo(this, user.getId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                RouteInfoActivity.setGotData(false);
                Logger.getAnonymousLogger().severe(position + " position");
                Route route = (Route)routes.get(position);
                intent2.putExtra("ROUTE", route.getId());
                setResult(RESULT_OK, intent2);
                startActivity(intent2);
            }
        });

}

    @Override
    public void onSuccess(Response response) {
        if(response.body() == null ){
            animation.setVisibility(View.VISIBLE);
        }else if(((ArrayList<Route>)response.body()).size() == 0){
            animation.setVisibility(View.VISIBLE);
        }

        routes = new ArrayList<Route>();
        routes = (ArrayList<Route>)response.body();
        listNames = routes.stream().map(u -> u.getName()).collect(Collectors.toList());
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listNames);

        list.setAdapter(adaptador);
        image.clearAnimation();
        image.setImageResource(R.drawable.loadingrefresh);
    }

    @Override
    public void onError(Throwable t) {
            animation.setVisibility(View.VISIBLE);
        Toast.makeText(this, t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        image.clearAnimation();
        image.setImageResource(R.drawable.loadingrefresh);
    }

    @Override
    public void onClick(View view) {
        image.setImageResource(R.drawable.refresh);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setFillAfter(true);
        image.startAnimation(rotation);
        try {
            client.findRoutesByAssignedTo(this, user.getId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}