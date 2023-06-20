package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import aiw.mobile.ta_pam.R;

public class ForgotPasswordPage extends AppCompatActivity {

    Button btnSubmit2;
    ImageView iconBack1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        btnSubmit2 = findViewById(R.id.btnSubmit2);
        iconBack1 = findViewById(R.id.ivBack1);

        btnSubmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ForgotPasswordPage.this, OTPPage.class);
                startActivity(intent);
            }
        });

        iconBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordPage.this, SignInPage.class);
                startActivity(intent);
            }
        });
    }
}