package aiw.mobile.ta_pam.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import aiw.mobile.ta_pam.Model.User;
import aiw.mobile.ta_pam.databinding.ActivityProfilePageBinding;

public class ProfilePage extends AppCompatActivity {

    FirebaseAuth mAuth;

    DatabaseReference databaseReference, users;
    private ActivityProfilePageBinding binding;
    private boolean isZoomed = false;
    private float pivotX;
    private float pivotY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance("https://uap-pam-1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        users = this.databaseReference.child("users");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();

            this.users.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        displayUserInfo(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfilePage.this, "Database Error.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfilePage.this, HomePage.class);
                    startActivity(intent);
                }
            });
        }

        // setOnClickListener untuk zoom gambar
        binding.ivMyImage.setOnClickListener(v -> toggleZoom());

        // setOnClickListener untuk kembali ke halaman home page
        binding.ivBackProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfilePage.this, HomePage.class);
            startActivity(intent);
        });

        // setOnClickListener untuk berpindah ke halaman upload
        binding.btnUploadPage.setOnClickListener(view -> {
            Intent intent = new Intent(ProfilePage.this, Upload.class);
            startActivity(intent);
        });

        binding.btnEdit.setOnClickListener(view -> {
            // Membuat intent untuk EditProfile
            Intent intent = new Intent(ProfilePage.this, EditProfile.class);

            // Mendapatkan data pengguna dari tampilan
            String fullname = binding.tvFullName.getText().toString();
            String email = binding.tvEmail.getText().toString();
            String username = binding.tvUsername.getText().toString();

            // Menambahkan data pengguna ke intent
            intent.putExtra("fullname", fullname);
            intent.putExtra("email", email);
            intent.putExtra("username", username);

            // Memulai aktivitas EditProfile dengan intent yang sudah diisi
            startActivity(intent);
        });


    }

    private void displayUserInfo(User user) {
        // Menampilkan data User pada tampilan ProfilePage
        binding.tvFullName.setText(user.getFullname());
        binding.tvEmail.setText(user.getEmail());
        binding.tvUsername.setText(user.getUsername());
    }

    public void toggleZoom() {
        ImageView ivMyImage = binding.ivMyImage;
        float scaleFactor = 1.5f;
        if (isZoomed) {
            // Reset zoom
            ScaleAnimation scaleAnimation = new ScaleAnimation(scaleFactor, 1.0f, scaleFactor, 1.0f, pivotX, pivotY);
            scaleAnimation.setDuration(100);
            scaleAnimation.setFillAfter(true);
            ivMyImage.startAnimation(scaleAnimation);
            isZoomed = false;
        } else {
            // Saat zoom in
            float viewWidth = ivMyImage.getWidth();
            float viewHeight = ivMyImage.getHeight();
            pivotX = viewWidth / 2;
            pivotY = viewHeight / 2;
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, scaleFactor, 1.0f, scaleFactor, pivotX, pivotY);
            scaleAnimation.setDuration(100);
            scaleAnimation.setFillAfter(true);
            ivMyImage.startAnimation(scaleAnimation);
            isZoomed = true;
        }
    }
}
