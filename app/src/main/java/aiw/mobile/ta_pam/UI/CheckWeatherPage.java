package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import aiw.mobile.ta_pam.R;

public class CheckWeatherPage extends AppCompatActivity {

    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_weather_page);

        iv_back = findViewById(R.id.iv_back_weather);

        iv_back.setOnClickListener(v -> {
            Intent intent = new Intent(CheckWeatherPage.this, HomePage.class);
            startActivity(intent);
        });

    }
}