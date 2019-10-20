package com.example.doot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.util.HttpUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static java.net.HttpURLConnection.HTTP_OK;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean MapMethod = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        refreshmap();
    }

    private void refreshmap(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Thread thread32 = new Thread(new Runnable() {
            public void run() {
                try {
                    if(MapMethod)
                    {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                                    public void onSuccess(Location locatio2n) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (locatio2n != null) {
                                            Double x2 = locatio2n.getLongitude();
                                            Double y2 = locatio2n.getLatitude();

                                            sendpost2(x2.toString(),y2.toString());
                                        }
                                    }
                                });

                    }

                        URL url = new URL("http://192.168.137.13:5005/mapgen");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        InputStream is = connection.getInputStream();
                        Bitmap img = BitmapFactory.decodeStream(is);
                        ImageView image = (ImageView) findViewById(R.id.imageView2);
                        image.setImageBitmap(img);

                } catch (Exception e) {
                    Log.e("reee", e.toString());
                }
            }
        });
        thread32.start();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if( id == R.id.action_settings){
            Context context = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose a location option");

           // add a list
            String[] animals = {"Servers Location", "Current Location"};
            builder.setItems(animals, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: // horse
                            MapMethod = false;
                            refreshmap();
                        case 1: // cow
                            MapMethod = true;
                            refreshmap();
                    }
                    refreshmap();
                }
            });

// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    public void doot2(View view) {

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Double x = location.getLongitude();
                            Double y = location.getLatitude();
                            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            sendpost("d",x.toString(),y.toString(),date,"idle");
                        }
                    }
                });

    }

   private void sendpost(final String ID, final String X, final String Y, final String DATE,final String STATUS) {
        Thread thread = new Thread(new Runnable() {

            public void run() {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("ID", ID)
                        .add("X", X)
                        .add("Y", Y)
                        .add("DATE", DATE)
                        .add("STATUS", STATUS)
                        .build();
                Request request = new Request.Builder()
                        .url("http://192.168.137.13:5005/balladd")
                        .post(formBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    Log.i("HAHA",response.toString());
                    refreshmap();

                } catch (Exception ex) {

                    Log.e("DEATH", ex.toString());
                }
            }
        });
        thread.start();
    }
    private void sendpost2(final String X, final String Y) {
        Thread thread = new Thread(new Runnable() {

            public void run() {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("LON", X)
                        .add("LAT", Y)
                        .build();
                Request request = new Request.Builder()
                        .url("http://192.168.137.13:5005/mapgen")
                        .post(formBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    Log.i("HAHA",response.toString());

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),"NETWORK ERROR",Toast.LENGTH_SHORT).show();

                    Log.e("DEATH", ex.toString());
                }
            }
        });
        thread.start();
    }
}
