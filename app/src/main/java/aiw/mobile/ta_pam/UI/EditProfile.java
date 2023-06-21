package aiw.mobile.ta_pam.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
                finish();
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

                    // Memperbarui data pengguna di Firebase
                    userRef.child("fullname").setValue(updatedUser.getFullname());
                    userRef.child("username").setValue(updatedUser.getUsername());

                    // Kembali ke Activity sebelumnya dengan membawa data yang diubah
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newName", newName);
                    resultIntent.putExtra("newUsername", newUsername);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
        }
    }
}
