package aiw.mobile.ta_pam.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class EditPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    ImageView btnBackEditPage;
    Button btnEditPage;
    EditText editNama1, editLokasi1, editDeskripsi1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://uap-pam-1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        editNama1 = findViewById(R.id.editNama1);
        editLokasi1 = findViewById(R.id.editLokasi1);
        editDeskripsi1 = findViewById(R.id.editDeskripsi1);
        btnEditPage = findViewById(R.id.btSaveDestiantion);
        btnBackEditPage = findViewById(R.id.btnBackEditPage1);

        Destination destination = getIntent().getParcelableExtra("EXTRA DESTINATION");

        editNama1.setText(destination.getNama());
        editLokasi1.setText(destination.getLokasi());
        editDeskripsi1.setText(destination.getDeskripsi());

        btnEditPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = editNama1.getText().toString();
                String lokasi = editLokasi1.getText().toString();
                String deskripsi = editDeskripsi1.getText().toString();
                //TODO: NEED TO BE UPDATE

                databaseReference.child("destination").child(mAuth.getUid()).child(destination.getKey()).setValue(new Destination(nama, lokasi, deskripsi, "")).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditPage.this, "Berhasil Update Destinasi", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditPage.this, HomePage.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPage.this, "Gagal Melakukan Update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnBackEditPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPage.this, HomePage.class);
                startActivity(intent);
            }
        });
    }
}