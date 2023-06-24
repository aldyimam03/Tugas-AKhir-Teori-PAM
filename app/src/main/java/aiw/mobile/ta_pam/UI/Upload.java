package aiw.mobile.ta_pam.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import aiw.mobile.ta_pam.Model.User;
import aiw.mobile.ta_pam.databinding.ActivityUploadBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Upload extends AppCompatActivity {

    private ActivityUploadBinding binding;
    private Uri imageUri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private DatabaseReference usersRef;

    private Handler handler;

    private ActivityResultLauncher<String> selectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inisialisasi Handler
        handler = new Handler(Looper.getMainLooper());

        // setOnClickListener untuk button back
        binding.ivBackUpload.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfilePage.class);
            startActivity(intent);
        });

        // setOnClickListener untuk button select image
        binding.btnSelectImage.setOnClickListener(v -> selectImage.launch("image/*"));

        // setOnClickListener untuk button upload image
        binding.btnUploadImage.setOnClickListener(v -> uploadImage());

        // Inisialisasi ActivityResultLauncher
        selectImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageUri = result;
                            binding.ivDefaultImg.setImageURI(imageUri);
                        }
                    }
                });

        // Initialize Firebase references
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://uap-pam-1-default-rtdb.asia-southeast1.firebasedatabase.app/");
            usersRef = database.getReference("users").child(userUid);
        }
    }

    // Method untuk mengupload gambar ke Firebase Storage dan mengupdate foto profil pengguna
    private void uploadImage() {
        if (imageUri != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading file...");
            progressDialog.show();

            // Dapatkan username pengguna dari intent
            String usernameUser = getIntent().getStringExtra("username");

            // Format tanggal dan nama file gambar
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
            Date now = new Date();
            final String fileName = "IMG_" + usernameUser + "_" + formatter.format(now);

            // Mereferensikan tempat gambar disimpan
            storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

            // Mengupload gambar ke Firebase Storage
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Dapatkan URL unduh gambar dari Firebase Storage
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    // Update foto profil pengguna di Firebase Realtime Database
                                    updateUserProfilePicture(downloadUri.toString());
                                    progressDialog.dismiss();
                                    Toast.makeText(Upload.this, "Image successfully uploaded!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Upload.this, ProfilePage.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Upload.this, "Image upload failed, please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("Firebase Storage", "Error: " + e.getMessage());
                        }
                    });
        } else {
            // Menampilkan pesan jika belum memilih foto
            Toast.makeText(Upload.this, "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method untuk mengupdate foto profil pengguna di Firebase Realtime Database
    private void updateUserProfilePicture(String imageUrl) {
        usersRef.child("profilePicture").setValue(imageUrl);
    }
}