package com.androidp.wheatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private RelativeLayout rlayout;
    private ProgressBar pbar;
    private TextView cityNameiv;
    private TextView temp;
    private TextView condi;
    private EditText editCity;
    private ImageView IVsearch,ivicon,backIV;
    private RecyclerView wheather;
    private ArrayList<WeatherModal> weatherModalArrayList;
    private weatherAdapter adapter;
    private String cityName;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        rlayout = findViewById(R.id.rlayout);
        pbar = findViewById(R.id.pbar);
        cityNameiv = findViewById(R.id.cityName);
        temp = findViewById(R.id.temp);
        condi = findViewById(R.id.condi);
        editCity = findViewById(R.id.editCity);
        ivicon = findViewById(R.id.ivicon);
        IVsearch = findViewById(R.id.IVsearch);
        wheather = findViewById(R.id.wheather);
        backIV = findViewById(R.id.backIV);


        weatherModalArrayList = new ArrayList<>();
        adapter = new weatherAdapter(this,weatherModalArrayList);
        wheather.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity2.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }


        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(), location.getLatitude());
        getweatherInfo(cityName);

        IVsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editCity.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity2.this,"Please Enter City Name",Toast.LENGTH_SHORT).show();
                }
                else {
                    cityNameiv.setText(cityName);
                    getweatherInfo(city);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Allow Permissions",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude){
        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);

            for (Address adr : addresses){
                if(adr!= null){
                    String city = adr.getLocality();
                    if(city!= null && !city.equals("")){
                        cityName = city;
                    }
                    else {
                        Log.d("TAG","User City not found");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }


    private void getweatherInfo(String cityName){
        String url="https://api.weatherapi.com/v1/forecast.json?key=deadad1688ef46f8929170126213010&q="+ cityName+"&days=1&aqi=no&alerts=no";

        //cityNameiv.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pbar.setVisibility(View.GONE);
                rlayout.setVisibility(View.VISIBLE);
                //weatherModalArrayList.clear();

             try {
                 String temperature = response.getJSONObject("current").getString("temp_c");
                 temp.setText(temperature + " c");

                 int isDay = response.getJSONObject("current").getInt("is_day");
                 String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                 String condiicon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                 Picasso.get().load("https:".concat(condiicon)).into(ivicon);

                 condi.setText(condition);
                 if(isDay==1){
                     Picasso.get().load("https://wallpaperaccess.com/full/929229.jpg").into(backIV);
                 }
                 else {
                     Picasso.get().load("https://wallpaperaccess.com/full/929229.jpg").into(backIV);
                 }

                 JSONObject forecastObj = response.getJSONObject("forecast");
                 JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);

                 JSONArray hourarray = forecast0.getJSONArray("hour");

                 for(int i=0; i < hourarray.length();i++){
                     JSONObject hourobj = hourarray.getJSONObject(i);
                     String time = hourobj.getString("time");
                     String temp1 = hourobj.getString("temp_c");
                     String img = hourobj.getJSONObject("time").getString("icon");
                     String wind = hourobj.getString("wind_kph");
                     weatherModalArrayList.add(new WeatherModal(time,temp1,img,wind));
                 }
                 adapter.notifyDataSetChanged();
             } catch (JSONException e) {
                 e.printStackTrace();
             }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity2.this,"Please enter valid city Name",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}