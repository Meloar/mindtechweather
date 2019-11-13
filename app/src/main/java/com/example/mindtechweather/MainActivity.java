package com.example.mindtechweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Spinner Spinner_city;
    private TextView TextView_main, TextView_des, TextView_temp, TextView_hum;

    //Cities name
    String [] cities = {"Eger", "Budapest", "London", "New York", "Vienna", "Paris", "Rome", "Tokyo", "Shanghai", "Los Angeles", "Delhi", "Moscow", "Rio De Janeiro", "Osaka", "Hong Kong", "Chicago", "Debrecen",
                        "Pécs", "Lagos", "Balaton", "Kecskemét", "Krakkó", "Lima", "Berlin", "Veszprém", "Szeged", "Miskolc", "Győr", "Sopron"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //Connecting the City array to the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,cities);
        Spinner_city.setAdapter(adapter);


        //Spinner select event function
        Spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                api_key(Spinner_city.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Initialization
    public void init()
    {
        Spinner_city = (Spinner) findViewById(R.id.city);
        TextView_main = (TextView) findViewById(R.id.main);
        TextView_des = (TextView) findViewById(R.id.description);
        TextView_temp = (TextView) findViewById(R.id.temperature);
        TextView_hum = (TextView) findViewById(R.id.humidity);

    }

    //Getting the weather information by OpenWeatherMap API
    private void api_key(String city) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=7ee2d0dbe498fedfa35b5e85f4115aef&units=metric").get().build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {


                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    String responseData = response.body().string();
                    try {

                        JSONObject json = new JSONObject(responseData);
                        JSONArray array = json.getJSONArray("weather");
                        JSONObject object = array.getJSONObject(0);

                        String main = object.getString("main");
                        String description = object.getString("description");

                        JSONObject templ = json.getJSONObject("main");
                        Double Temperature = templ.getDouble("temp");
                        String temp = Math.round(Temperature) + "°C";
                        Double Humidity = templ.getDouble("humidity");
                        String hum = Math.round(Humidity) + "%";

                        setText(TextView_main,main);
                        setText(TextView_des,description);
                        setText(TextView_temp,temp);
                        setText(TextView_hum,hum);

                    }
                    catch (JSONException e){

                        e.printStackTrace();

                    }

                }
            });
        }
        catch (IOException e){

            e.printStackTrace();

        }

    }

    //Function for updating the weather data on UI
    private void setText(final TextView text,final String value){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                text.setText(value);

            }
        });

    }

}
