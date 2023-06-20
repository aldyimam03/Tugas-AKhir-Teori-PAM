package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import aiw.mobile.ta_pam.R;

public class OTPPage extends AppCompatActivity {

    ImageView iconBack2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otppage);

        iconBack2 = findViewById(R.id.ivBack2);

        iconBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OTPPage.this, ForgotPasswordPage.class);
                startActivity(intent);
            }
        });
    }
}