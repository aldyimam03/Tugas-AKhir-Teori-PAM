package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import aiw.mobile.ta_pam.R;

public class ResetPasswordPage extends AppCompatActivity {

    ImageView iconBack3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_page);

        iconBack3 = findViewById(R.id.iv_back3);

        iconBack3.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordPage.this, OTPPage.class);
            startActivity(intent);
        });
    }
}