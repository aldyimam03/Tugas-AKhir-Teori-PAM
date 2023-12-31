package aiw.mobile.ta_pam.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import aiw.mobile.ta_pam.R;

public class HomePage extends AppCompatActivity {

    ImageView ivProfile, ivSetting;
    TextView seeAll, textViewLocation, tvUsername;
    Button checkWeather, checkLocation, btnAdd;
    private Geocoder geocoder;
    FirebaseAuth mAuth;
    FusedLocationProviderClient locationProviderClient;

    private FragmentManager fm;
    private DestinationListFragment destinationListFragment;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ivProfile = findViewById(R.id.ivMyPicture);
        ivSetting = findViewById(R.id.iv_setting);
        seeAll = findViewById(R.id.tvSeeAll);
        btnAdd = findViewById(R.id.btnAdd);
        textViewLocation = findViewById(R.id.tvLocation);
        checkWeather = findViewById(R.id.btnCheckWeather);
        checkLocation = findViewById(R.id.btnCheckLocation);
        tvUsername = findViewById(R.id.tvMyUsername);

        mAuth = FirebaseAuth.getInstance();
        geocoder = new Geocoder(this, Locale.getDefault());
        locationProviderClient = LocationServices.getFusedLocationProviderClient(HomePage.this);
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        checkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, AddPage.class);
                startActivity(intent);
            }
        });

        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ProfilePage.class);
            startActivity(intent);
        });

        ivSetting.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, SettingPage.class);
            startActivity(intent);
        });

        seeAll.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, SeeAll.class);
            startActivity(intent);
        });

        checkWeather.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, CheckWeatherPage.class);
            startActivity(intent);
        });

        this.fm = getSupportFragmentManager();
        this.destinationListFragment = new DestinationListFragment();

        fm.beginTransaction()
                .add(R.id.frameRecyclerView, destinationListFragment,"FDestination")
                .commit();


        // Dummy
//        destinationArrayList = new ArrayList<>();
//
//        Destination destination1 = new Destination("Pantai", "Dingin", "Malang");
//        destinationArrayList.add(destination1);
//        Destination destination2 = new Destination("Gunung", "Tinggi", "Malang");
//        destinationArrayList.add(destination2);
//        Destination destination3 = new Destination("Air Terjun", "Dalam", "Malang");
//        destinationArrayList.add(destination3);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();
            //tvUsername.setText(currentUser.getEmail());
            usersRef.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String profilePicture = dataSnapshot.child("profilePicture").getValue(String.class);
                        if (profilePicture != null && !profilePicture.isEmpty()) {
                            Glide.with(HomePage.this)
                                    .load(profilePicture)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(ivProfile);
                        }

                        String username = dataSnapshot.child("username").getValue(String.class);
                        if (username != null && !username.isEmpty()) {
                            tvUsername.setText(username);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HomePage.this, "Database Error.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        DestinationListFragment dlf = (DestinationListFragment) getSupportFragmentManager().findFragmentByTag("FDestination");
        dlf.getAllData();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Izin lokasi tidak diaktifkan!", Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getLocation();
                }
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Meminta izin lokasi
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            }
        } else {
            // Mendapatkan lokasi
            locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            String addressString = address.getAddressLine(0);
                            textViewLocation.setText(addressString);
                        } else {
                            textViewLocation.setText("Alamat tidak ditemukan");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lokasi tidak aktif!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}