package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import aiw.mobile.ta_pam.R;

public class ContactPage extends AppCompatActivity {

    ImageView ivBack, ivFacebook, ivTwitter, ivInstagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        ivBack = findViewById(R.id.iv_back_contact);
        ivFacebook = findViewById(R.id.ivGambarDestinasi);
        ivTwitter = findViewById(R.id.iv_twitter);
        ivInstagram = findViewById(R.id.iv_instagram);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactPage.this, SettingPage.class);
                startActivity(intent);
            }
        });

        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebook();
            }
        });

        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitter();
            }
        });

        ivInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagram();
            }
        });
    }

    private void openFacebook() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTwitter() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openInstagram() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}