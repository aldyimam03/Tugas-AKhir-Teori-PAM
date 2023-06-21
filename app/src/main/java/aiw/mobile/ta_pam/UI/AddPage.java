package aiw.mobile.ta_pam.UI;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import aiw.mobile.ta_pam.Model.Destination;
import aiw.mobile.ta_pam.R;

public class AddPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private ActivityResultLauncher<String> selectImage;
    ImageView ivBackAdd;
    EditText addNama1, addLokasi1, addDeskripsi1;
    Button btnAddDestination, btnUpload;

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

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://uap-pam-1-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference();

        btnAddDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = addNama1.getText().toString();
                String lokasi = addLokasi1.getText().toString();
                String deskripsi = addDeskripsi1.getText().toString();
                Destination baru = new Destination(nama, lokasi, deskripsi);

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
                        }
                    }
                });

        btnUpload.setOnClickListener(v -> {
            selectImage.launch("image/*");
        });
    }
}