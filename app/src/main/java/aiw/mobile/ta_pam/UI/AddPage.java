package aiw.mobile.ta_pam.UI;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import aiw.mobile.ta_pam.Model.Destination;
import aiw.mobile.ta_pam.R;

public class AddPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private ActivityResultLauncher<String> selectImage;
    ImageView ivBackAdd;
    EditText addNama1, addLokasi1, addDeskripsi1, edtImageName;
    Button btnAddDestination, btnUpload, btnSaveDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);

        addNama1 = findViewById(R.id.editNama1);
        addLokasi1 = findViewById(R.id.editLokasi1);
        addDeskripsi1 = findViewById(R.id.editDeskripsi1);
        btnAddDestination = findViewById(R.id.btSaveDestiantion);
        ivBackAdd = findViewById(R.id.ivBackAdd);
        btnUpload = findViewById(R.id.btnUploadImage);
        edtImageName = findViewById(R.id.edtImageName);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://uap-pam-1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference();

        setupUpload();

        btnAddDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        ivBackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPage.this, HomePage.class);
                startActivity(intent);
            }
        });
    }

    private void setupUpload() {
        selectImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageUri = result;
                            edtImageName.setText(imageUri.toString().substring(imageUri.toString().lastIndexOf("/") + 1));
                        }
                    }
                });

        btnUpload.setOnClickListener(v -> {
            selectImage.launch("image/*");
        });
    }

    private void uploadImage() {
        if (imageUri != null) {
            ProgressDialog progressDialog;
            StorageReference storageReference;

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading file...");
            progressDialog.show();

            // Format tanggal dan nama file gambar
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
            Date now = new Date();
            final String fileName = "IMG_" + addNama1.getText().toString() + "_" + formatter.format(now);

            // Mereferensikan tempat gambar disimpan
            storageReference = FirebaseStorage.getInstance().getReference("destination/" + fileName);

            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Dapatkan URL unduh gambar dari Firebase Storage
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    // Update foto profil pengguna di Firebase Realtime Database
                                    progressDialog.dismiss();
                                    Toast.makeText(AddPage.this, "Image successfully uploaded!", Toast.LENGTH_SHORT).show();

                                    String nama = addNama1.getText().toString();
                                    String lokasi = addLokasi1.getText().toString();
                                    String deskripsi = addDeskripsi1.getText().toString();
                                    Destination baru = new Destination(nama, lokasi, deskripsi, downloadUri.toString());

                                    databaseReference.child("destination").child(mAuth.getUid()).push().setValue(baru).addOnSuccessListener(AddPage.this, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(AddPage.this, "Berhasil menambahkan data", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddPage.this, HomePage.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(AddPage.this, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddPage.this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(AddPage.this, "Image upload failed, please try again.", Toast.LENGTH_SHORT).show();
                        Log.e("Firebase Storage", "Error: " + e.getMessage());
                    });
        } else {
            Toast.makeText(AddPage.this, "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }
}