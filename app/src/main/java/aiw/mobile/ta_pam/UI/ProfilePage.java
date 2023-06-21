package aiw.mobile.ta_pam.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    private static final int REQUEST_CODE_EDIT_PROFILE = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    private ActivityProfilePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();

            usersRef.child(userUid).addValueEventListener(new ValueEventListener() {
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

        // setOnClickListener untuk pindah ke halaman home
        binding.ivBackProfile.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomePage.class);
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
            String profilePicture = ""; // Mengambil URL foto profil dari Firebase Realtime Database

            // Menambahkan data pengguna ke intent
            intent.putExtra("fullname", fullname);
            intent.putExtra("email", email);
            intent.putExtra("username", username);
            intent.putExtra("profilePicture", profilePicture);

            // Memulai aktivitas EditProfile dengan intent yang sudah diisi
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == RESULT_OK) {
            // Perbarui data profil jika ada data yang diubah
            String newName = data.getStringExtra("newName");
            String newUsername = data.getStringExtra("newUsername");
            String newProfilePicture = data.getStringExtra("newProfilePicture");

            binding.tvFullName.setText(newName);
            binding.tvUsername.setText(newUsername);

            if (newProfilePicture != null && !newProfilePicture.isEmpty()) {
                // Memuat foto profil baru menggunakan Glide
                Glide.with(this)
                        .load(newProfilePicture)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.ivMyImage);
            }

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }



    private void displayUserInfo(User user) {
        // Menampilkan data User pada tampilan ProfilePage
        binding.tvFullName.setText(user.getFullname());
        binding.tvEmail.setText(user.getEmail());
        binding.tvUsername.setText(user.getUsername());

        // Memuat foto profil menggunakan Glide
        Glide.with(this)
                .load(user.getProfilePicture())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivMyImage);
    }
}
