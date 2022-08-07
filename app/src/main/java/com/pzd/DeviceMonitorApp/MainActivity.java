package com.pzd.DeviceMonitorApp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView txtIMEI, txtInternet, txtCharging, txtBattery, txtLocation, txtTimeStamp ;
    Button displayButton;
    String latitude = "0.0", longitude = "0.0";
    boolean chargingB;
    int level;
    Handler handler;
    private FusedLocationProviderClient fusedLocationClient;



    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            txtBattery.setText(" Battery: " + level);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status ==
                    BatteryManager.BATTERY_STATUS_FULL;
            if (isCharging) {
                chargingB = true;
            }
            else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                chargingB = false;
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.P)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtIMEI = findViewById(R.id.myIMEI);
        txtInternet= findViewById(R.id.myInternet);
        txtCharging = findViewById(R.id.myCharging);
        txtBattery = findViewById(R.id.myBattery);
        txtLocation = findViewById(R.id.myLocation);
        displayButton = findViewById(R.id.displayBtn);
        txtTimeStamp = findViewById(R.id.myTimeStamp);

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss\ndd-MM-yyyy");
        String dateTimeStamp = localDateTime.format(dateTimeFormatter);
        txtTimeStamp.setText(dateTimeStamp);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        myLocation();

        handler = new Handler();
        runnable.run();

        String sIMEI = txtIMEI.getText().toString();
        String sInternet = txtIMEI.getText().toString();
        String sCharging = txtIMEI.getText().toString();
        String sBattery = txtIMEI.getText().toString();
        String sLocation = txtIMEI.getText().toString();
        String sTimeStamp = txtTimeStamp.getText().toString();
        postData(sIMEI,sInternet,sCharging,sBattery,sLocation,sTimeStamp);

        //---------------------------------------------------------------------------------------------------------------------------------//
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(broadcastReceiver,iFilter);

        int level1 = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        txtBattery.setText(" Battery: "+level1+"%");
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        boolean chargingA = false;
        if (isCharging) {
            chargingA = true;
        }
        else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            chargingA = false;
        }

        txtIMEI.setText(" IMEI: "+getIMEIDeviceId(this));
        txtInternet.setText(" Internet-Connected: "+String.valueOf(isInternetConnected()));
        txtCharging.setText(" Charging: "+ chargingA);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = Double.toString(location.getLatitude());
                            longitude = Double.toString(location.getLongitude());
                            txtLocation.setText(" Location: "+latitude+","+longitude);
                        }
                    }
                });
//-----------------------------------------------------------------------------------------------------------------------------------------//
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtIMEI.setText(" IMEI: "+getIMEIDeviceId(MainActivity.this));
                txtInternet.setText(" Internet-Connected: "+String.valueOf(isInternetConnected()));
                txtCharging.setText(" Charging: "+ chargingB);
                txtBattery.setText(" Battery: "+level+"%");
                txtLocation.setText(" Location: "+latitude+","+longitude);

                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss\ndd-MM-yyyy");
                String dateTimeStamp = localDateTime.format(dateTimeFormatter);
                txtTimeStamp.setText(dateTimeStamp);

                //----------------------------------------------------------------------------------------------------------------------//

                postData(sIMEI,sInternet,sCharging,sBattery,sLocation,sTimeStamp);


            }
        });
        //---------------------------------------------------------------------------------------------------------------//
    }

    private final Runnable runnable = new Runnable()
    {
        public void run()
        {
            Toast.makeText(MainActivity.this,"Updated!", Toast.LENGTH_SHORT).show();
            txtIMEI.setText(" IMEI: "+getIMEIDeviceId(MainActivity.this));
            txtInternet.setText(" Internet-Connected: "+String.valueOf(isInternetConnected()));
            txtCharging.setText(" Charging: "+ chargingB);
            txtBattery.setText(" Battery: "+level+"%");
            txtLocation.setText(" Location: "+latitude+","+longitude);

            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss\ndd-MM-yyyy");
            String dateTimeStamp = localDateTime.format(dateTimeFormatter);
            txtTimeStamp.setText(dateTimeStamp);

            MainActivity.this.handler.postDelayed(runnable,300000);
        }

    };

    public static String getIMEIDeviceId(Context context) {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getImei() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                } else {
                    deviceId = mTelephony.getImei();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }

    boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isConnected())
                return true;
            else
                return false;
        } else
            return false;
    }

    private void myLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = Double.toString(location.getLatitude());
                            longitude = Double.toString(location.getLongitude());
                        }
                    }
                });
    }

    private void postData(String deviceIMEI, String internetConnected, String batteryCharging, String battery, String location, String timeStamp){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://143.244.138.96:2110/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myApi api = retrofit.create(myApi.class);

        Model model = new Model(deviceIMEI,internetConnected, batteryCharging, battery, location,timeStamp);

        Call<Model> modelCall = api.createPost(model);

        modelCall.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {

                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Error found is : "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}