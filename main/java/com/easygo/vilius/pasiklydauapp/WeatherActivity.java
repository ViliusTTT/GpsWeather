package com.easygo.vilius.pasiklydauapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Klase , kurioje saugoma oru activicio nustatymai, parametrai, laukai
 */
public class WeatherActivity extends AppCompatActivity {
    static TextView placeTextView;      //Vietoves teksto laukas
    static TextView temperatureTextView;//Temperaturos teksto laukas
    static TextView description;        //Oro apibudinimo teksto laukas
    static TextView speed;              //Vejo greicio teksto laukas
    static TextView kryptis;            //Vejo krypties teksto laukas
    static TextView humidity;           //Oro dregnumo teksto laukas
    static TextView pressure;           //Oro slegio teksto laukas
    static TextView name;               //Pavadinimo teksto laukas

    /**
     * onCreate metodas sukuria oro duomenų laukus, juos susieja su kintamaisiais
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        temperatureTextView=(TextView)findViewById(R.id.temperatureTextView);
        description=(TextView)findViewById(R.id.descriptionTextView);
        speed=(TextView)findViewById(R.id.speedTextView);
        kryptis=(TextView)findViewById(R.id.kryptisTextView);
        humidity=(TextView)findViewById(R.id.humidityTextView);
        pressure=(TextView)findViewById(R.id.pressureTextView);
        name=(TextView)findViewById(R.id.nameTextView);
        DownloadTask task=new DownloadTask();
        String latitude=getIntent().getStringExtra("latitude");
        String longitude=getIntent().getStringExtra("longitude");
        task.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "139&appid=f6ba16f78782b91fcbbac8d6e19e2135");



    }

}
