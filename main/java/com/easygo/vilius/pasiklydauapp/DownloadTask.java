package com.easygo.vilius.pasiklydauapp;

import android.os.AsyncTask;
import com.easygo.vilius.pasiklydauapp.WeatherActivity;
import com.google.android.gms.awareness.state.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Klase oru servizo duomenu parsiuntimui ir apdorojimui
 */
public class DownloadTask extends AsyncTask<String,Void,String> {

    /**
     * Metodas prisijungti prie api
     * @param urls - naudojamo servizo url adresas
     * @return result- grazina is servizo gauta rezultata arba null, jei kazkas nepavyko
     */
    @Override
    protected String doInBackground(String... urls) {
        String result="";
        URL url;
        HttpURLConnection urlConnection=null;

        try {
            url=new URL(urls[0]);
            urlConnection=(HttpURLConnection)url.openConnection();
            InputStream in=urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data=reader.read();
            while(data!=-1)
            {
                char current=(char)data;
                result +=current;

                data=reader.read();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodas kuriame skaidome JSON duomenis ir atsirenkame reikalingus
     * @param result - is servizo gautas rezultatas, kuri apdoroja JSONobject
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray jArr = jsonObject.getJSONArray("weather");
            JSONObject JSONWeather = jArr.getJSONObject(0);
            JSONObject weatherDatas=new JSONObject(jsonObject.getString("main"));
            String weather=(String) JSONWeather.getString("description");
            JSONObject wind=new JSONObject(jsonObject.getString("wind"));
            double speed=Double.parseDouble(wind.getString("speed"));
            double direction=Double.parseDouble(wind.getString("deg"));
            double tempInt=Double.parseDouble(weatherDatas.getString("temp"));
            double humidity=Double.parseDouble(weatherDatas.getString("humidity"));
            double pressure=Double.parseDouble(weatherDatas.getString("pressure"));
            double tempIn=(tempInt*1.8-459.67);
             tempIn=(tempIn-32)*5/9;
            DecimalFormat df = new DecimalFormat("#.##");
            String dx=df.format(tempIn);

            WeatherActivity.temperatureTextView.setText(String.valueOf(dx)+ " celsius");
            String placeName= (String) jsonObject.get("name");
            WeatherActivity.name.setText(placeName);
            WeatherActivity.description.setText(weather);
            WeatherActivity.kryptis.setText(String.valueOf(direction)+" degrees");
            WeatherActivity.humidity.setText(String.valueOf(humidity)+ " %");
            WeatherActivity.pressure.setText(String.valueOf(pressure)+" hPa");
            WeatherActivity.speed.setText(String.valueOf(speed+" m/s"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
