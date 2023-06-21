package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import aiw.mobile.ta_pam.R;

public class CheckWeatherPage extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private TextView tvCoordinate, tvTemperature, tvCondition, tvWindSpeed,tvCity;
    private ImageView imageCondition, ivBackWeather;
    private RequestQueue requestQueue;

    String Location_Provider = LocationManager.GPS_PROVIDER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_weather_page);

        tvCity = findViewById(R.id.text_city);
        tvCoordinate = findViewById(R.id.text_coordinate);
        tvTemperature = findViewById(R.id.text_temperature);
        tvCondition = findViewById(R.id.text_condition);
        tvWindSpeed = findViewById(R.id.text_windspeed);
        imageCondition = findViewById(R.id.image_condition);
        ivBackWeather = findViewById(R.id.iv_back_weather);

        ivBackWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (savedInstanceState == null) {
            this.requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    "https://api.open-meteo.com/v1/forecast?latitude=-7.98&longitude=112.63&daily=weathercode&current_weather=true&timezone=auto",
                    this,
                    this);

            this.requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            this.tvCoordinate.setText(response.getString("latitude") + ", " + response.getString("longitude"));
            this.tvTemperature.setText(String.valueOf(response.getJSONObject("current_weather").getDouble("temperature")));
            this.tvWindSpeed.setText(String.valueOf(response.getJSONObject("current_weather").getDouble("windspeed")));
            changeConditionImage(imageCondition, tvCondition, response.getJSONObject("current_weather").getInt("weathercode"));
        } catch (JSONException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        this.tvCoordinate.setText("Rusak");
    }

    public void changeConditionImage(ImageView imageCondition, TextView tvCondition, int conditionCode) {
        switch (conditionCode) {
            case 0 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Clear Sky");
                break;
            }
            case 1 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Mainly clear");
                break;
            }
            case 2 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Partly Cloudy");
                break;
            }
            case 3 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Overcast");
                break;
            }
            case 45 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Fog");
                break;
            }
            case 48 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Depositing Rime Fog");
                break;
            }
            case 51 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Light Drizzle");
                break;
            }
            case 53 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Moderate Drizzle");
                break;
            }
            case 55 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Intense Drizzle");
                break;
            }
            case 56 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Light Freezing Drizzle");
                break;
            }
            case 57 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Moderate Freezing Drizzle");
                break;
            }
            case 61 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Slight Rain");
                break;
            }
            case 63 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Moderate Rain");
                break;
            }
            case 65 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Intense Rain");
                break;
            }
            case 66 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Light Freezing Rain");
                break;
            }
            case 67 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Heavy Freezing Rain");
                break;
            }
            case 71 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Slight Snowfall");
                break;
            }
            case 73 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Moderate Snowfall");
                break;
            }
            case 75 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Intense Snowfall");
                break;
            }
            case 77 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Snow Grains");
                break;
            }
            case 80 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Slight Rain Shower");
                break;
            }
            case 81 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Moderate Rain Shower");
                break;
            }
            case 82 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Violent Rain Shower");
                break;
            }
            case 85 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Slight Snow Shower");
                break;
            }
            case 86 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Heavy Snow Shower");
                break;
            }
            case 95 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Thunderstorm");
                break;
            }
            case 96 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Thunderstorm with Slight Hail");
                break;
            }
            case 99 : {
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText("Thunderstorm with Heavy Hail");
                break;
            }
            default:
                imageCondition.setImageResource(R.drawable.icon_sun);
                tvCondition.setText(String.valueOf(conditionCode));
                break;
        }
    }
}