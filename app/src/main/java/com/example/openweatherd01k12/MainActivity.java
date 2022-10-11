package com.example.openweatherd01k12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtCity;
    private ImageView imgWeather;
    private TextView tvDescription;

    // API KEY OPEN weather
    private static final String API_KEY = "928133397391e6af373468b74849e7ab";

    // Config volley
    private RequestQueue queue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtCity = findViewById(R.id.edtCity);
        tvDescription = findViewById(R.id.tvWeatherDesciption);
        imgWeather = findViewById(R.id.imgWeather);
        // init volley
        queue = Volley.newRequestQueue(this);
    }

    private String createRequestURL(String city) {
        return String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=vi"
                , city, API_KEY);
    }

    private String createIconURL(String icon){
        return String.format("https://openweathermap.org/img/wn/%s@4x.png",icon);
    }

    public void findCityByName(View view) {
        // Lay du lieu trong Editext
        // Neu trong => Bao loi
        // Nếu có dữ liệu => Tạo URL
        // Request URL

        String city = edtCity.getText().toString().trim();
        if (city.isEmpty()) {
            edtCity.setError("Hãy nhập dữ liệu!");
            return;
        }
        String myURL = createRequestURL(city);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String strIcon = response.getJSONArray("weather").getJSONObject(0)
                                    .getString("icon");


                            String iconURL = createIconURL(strIcon);
                            Glide.with(MainActivity.this).load(iconURL).into(imgWeather);


                            String description= response.getJSONArray("weather").getJSONObject(0)
                                    .getString("description");

                            tvDescription.setText(description);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }
}