package aiw.mobile.ta_pam.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aiw.mobile.ta_pam.Model.User;
import aiw.mobile.ta_pam.R;

public class EditProfile extends AppCompatActivity {

    private EditText etNewName, etNewUsername;
    private TextView tvUserEmail;
    private Button btnSaveChanges;
    private ImageView ivBack;

    private FirebaseAuth mAuth;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        tvUserEmail = findViewById(R.id.tvUserEmail);
        etNewName = findViewById(R.id.etOldName);
        etNewUsername = findViewById(R.id.etOldUsername);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        ivBack = findViewById(R.id.ivBackToProfile);


        // Mendapatkan intent yang dikirimkan dari ProfilePage
        Intent intent = getIntent();
        if (intent != null) {
            String fullname = intent.getStringExtra("fullname");
            String email = intent.getStringExtra("email");
            String username = intent.getStringExtra("username");

            // Menampilkan data yang diterima pada tampilan EditProfile
            etNewName.setText(fullname);
            tvUserEmail.setText(email);
            etNewUsername.setText(username);

            ivBack.setOnClickListener(view -> {
                Intent intent1 = new Intent(this, ProfilePage.class);
                startActivity(intent1);
            });

            btnSaveChanges.setOnClickListener(view -> {
                // Mengambil data dari EditText
                String newName = etNewName.getText().toString().trim();
                String newUsername = etNewUsername.getText().toString().trim();

                // Membuat objek User dengan data yang diubah
                User updatedUser = new User(newName, newUsername);

                // Mendapatkan user saat ini
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userUid = currentUser.getUid();
                    DatabaseReference userRef = usersRef.child(userUid);

                    // Memperbarui data pengguna di Firebase menggunakan Runnable dan Handler
                    updateProfileInFirebase(userRef, updatedUser);
                }
            });
        }
    }
    private void updateProfileInFirebase(DatabaseReference userRef, User updatedUser) {
        // Menampilkan ProgressDialog atau ProgressBar jika diperlukan

        // Menjalankan kode pembaruan Firebase di thread latar belakang
        new Thread(() -> {
            try {
                // Memperbarui data pengguna di Firebase
                userRef.child("fullName").setValue(updatedUser.getFullname());

                // Mengirim pesan ke Handler untuk menampilkan toast di UI thread
                handler.post(() -> {
                    // Menampilkan Toast di UI thread
                    Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                e.printStackTrace();

                // Mengirim pesan ke Handler untuk menampilkan toast di UI thread
                handler.post(() -> {
                    // Menampilkan Toast di UI thread
                    Toast.makeText(EditProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private Handler handler = new Handler();
}
