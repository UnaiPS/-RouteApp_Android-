/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.routeapp_android.control;

import java.util.List;

import com.example.routeapp_android.model.Coordinate;
import com.example.routeapp_android.model.Coordinate_Route;
import com.example.routeapp_android.model.Direction;
import com.example.routeapp_android.model.FullRoute;
import com.example.routeapp_android.model.Route;
import com.example.routeapp_android.model.Session;
import com.example.routeapp_android.model.User;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 *
 * @author Jon Calvo Gaminde
 */
public interface ClientService {

    //Route Client

    @Headers("Accept: application/json")
    @POST("routeappjpa.route/{code}")
    Call<Void> createRoute(@Body FullRoute fullRoute, @Path("code") String code);

    @Headers("Accept: application/json")
    @PUT("routeappjpa.route/{code}")
    Call<Void> editRoute(@Body Route route, @Path("code") String code);

    @Headers("Accept: application/json")
    @GET("routeappjpa.route/{code}/{id}")
    Call<Route> findRoute(@Path("id") String routeId, @Path("code") String code);

    @Headers("Accept: application/json")
    @GET("routeappjpa.route/{code}")
    Call<List<Route>> findAllRoutes(@Path("code") String code);

    @Headers("Accept: application/json")
    @GET("routeappjpa.route/assignedTo/{code}/{id}")
    Call<List<Route>> findRoutesByAssignedTo(@Path("id") String userId, @Path("code") String code);

    @Headers("Accept: application/json")
    @DELETE("routeappjpa.route/{code}/{id}")
    Call<Void> removeRoute(@Path("id") String routeId, @Path("code") String code);

    //Coordinate Client

    @Headers("Accept: application/json")
    @GET("routeappjpa.coordinate/direction/type/{code}/{type}")
    Call<List<Direction>> findDirectionsByType(@Path("type") String type, @Path("code") String code);

    @Headers("Accept: application/json")
    @GET("routeappjpa.coordinate/direction/route/{code}/{route}")
    Call<List<Direction>> findDirectionsByRoute(@Path("route") String routeId, @Path("code") String code);

    @Headers("Accept: application/json")
    @PUT("routeappjpa.coordinate/direction/visited/{code}/{routeId}/{coordinateId}")
    Call<Long> markDestinationAsVisited(@Path("routeId") Long routeId, @Path("coordinateId") Long coordinateId, @Body Coordinate gps, @Path("code") String code);

    //User Client

    @Headers("Accept: application/json")
    @POST("routeappjpa.user")
    Call<Void> createUser(@Body User user);

    @Headers("Accept: application/json")
    @PUT("routeappjpa.user/{code}")
    Call<Void> editUser(@Body User user, @Path("code") String code);

    @Headers("Accept: application/json")
    @GET("routeappjpa.user/{code}/{id}")
    Call<User> findUser(@Path("id") String userId, @Path("code") String code);

    @Headers("Accept: application/json")
    @GET("routeappjpa.user/{code}")
    Call<List<User>> findAllUsers(@Path("code") String code);

    @Headers("Accept: application/json")
    @GET("routeappjpa.user/{code}/{login}")
    Call<User> findUserByLogin(@Path("login") String userLogin, @Path("code") String code);

    @Headers("Accept: application/json")
    @GET("routeappjpa.user/{code}/{privilege}")
    Call<List<User>> findUsersByPrivilege(@Path("privilege") String privilege, @Path("code") String code);

    @Headers("Accept: application/json")
    @DELETE("routeappjpa.user/{code}/{id}")
    Call<Void> removeUser(@Path("id") String userId, @Path("code") String code);

    @Headers("Accept: application/json")
    @POST("routeappjpa.user/login")
    Call<Session> login(@Body User user);

    @Headers("Accept: application/json")
    @GET("routeappjpa.user/forgottenpasswd/{email}/{login}")
    Call<Void> forgottenPassword(@Path("login") String login, @Path("email") String email);

    @Headers("Accept: application/json")
    @POST("routeappjpa.user/email/{code}")
    Call<String> emailConfirmation(@Body User user, @Path("code") String code);
}
