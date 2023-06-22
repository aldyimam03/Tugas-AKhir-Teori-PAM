package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import aiw.mobile.ta_pam.Model.Destination;
import aiw.mobile.ta_pam.R;

public class DetailDestinationPage extends AppCompatActivity {

    ImageView iv_back;
    ImageView destinationImage;
    Button btn_contactUs;
    TextView tvTitleDetailDestination;
    TextView tvLocationDetailDestination;
    TextView tvDeskripsiDetailDestination;
    TextView btn_download;


    Destination destination;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_destination_page);

        iv_back = findViewById(R.id.ivBack);
        destinationImage = findViewById(R.id.imageView10);
        btn_contactUs = findViewById(R.id.btn_contactUs);
        btn_download = findViewById(R.id.btnDownload);

        destination = getIntent().getParcelableExtra("EXTRA DESTINATION");

        System.out.println(destination.getLokasi());

        tvTitleDetailDestination = findViewById(R.id.tvTitleDetailDestination);
        tvLocationDetailDestination = findViewById(R.id.tvLocationDetailDestination);
        tvDeskripsiDetailDestination = findViewById(R.id.tvDeskripsiDetailDestination);

        tvTitleDetailDestination.setText(destination.getNama());
        tvLocationDetailDestination.setText(destination.getLokasi());
        tvDeskripsiDetailDestination.setText(destination.getDeskripsi());
        Glide.with(getApplicationContext())
                .load(destination.getImage())
                .into(destinationImage);

        iv_back.setOnClickListener(v -> {
            Intent intent = new Intent(DetailDestinationPage.this, HomePage.class);
            startActivity(intent);
        });

        btn_contactUs.setOnClickListener(v -> {
            String phoneNumber = "+6287702519051";
            String message = "Apa benar dengan agen perjalanan Easy - Go ?";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message)));
            startActivity(intent);
        });

    }
}