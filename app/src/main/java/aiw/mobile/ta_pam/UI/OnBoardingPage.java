package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import aiw.mobile.ta_pam.R;

public class OnBoardingPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button SignIn;
    TextView SignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_page);

        mAuth = FirebaseAuth.getInstance();

        SignIn = findViewById(R.id.btnSignIn0);
        SignUp = findViewById(R.id.tvSignUp);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(OnBoardingPage.this, SignInPage.class);
                    startActivity(intent);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingPage.this, SignUpPage.class);
                startActivity(intent);
            }
        });
    }
}