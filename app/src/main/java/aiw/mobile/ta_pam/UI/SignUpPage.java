package aiw.mobile.ta_pam.UI;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aiw.mobile.ta_pam.Model.User;
import aiw.mobile.ta_pam.R;

public class SignUpPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText et_fullname, et_username, et_email, et_password,et_verifyPassword;
    Button btnRegister;
    ImageView iconBack7;

    DatabaseReference databaseReference;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mAuth = FirebaseAuth.getInstance();

        et_fullname = findViewById(R.id.et_fullname);
        et_email = findViewById(R.id.et_email);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_verifyPassword = findViewById(R.id.et_verifyPassword);

        btnRegister = findViewById(R.id.btnRegister);
        iconBack7 = findViewById(R.id.ivBack7);

        // Mendeklarasikan database refrence
        databaseReference = FirebaseDatabase.getInstance("https://uap-pam-1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        users = this.databaseReference.child("users");;

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                signUp(email, password);
            }
        });

        iconBack7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, OnBoardingPage.class);
                startActivity(intent);
            }
        });
    }

    public void signUp(String email, String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId = user.getUid();
                    String fullname = et_fullname.getText().toString();
                    String username = et_username.getText().toString();
                    String email = et_email.getText().toString();

                    // Simpan data pengguna ke Firebase Realtime Database
                    User newUser = new User(userId, fullname, username, email);
                    users.child(userId).setValue(newUser);

                    Intent intent = new Intent(SignUpPage.this, SignInPage.class);
                    startActivity(intent);
                    Toast.makeText(SignUpPage.this, "Berhasil membuat akun", Toast.LENGTH_SHORT).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpPage.this, "Gagal membuat akun" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//    public void signUp(String email, String password) {
//        if (!validateForm()) {
//            return;
//        }
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success");
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    Intent intent = new Intent(SignUpPage.this, SignInPage.class);
//                    startActivity(intent);
//                    Toast.makeText(SignUpPage.this, "Berhasil membuat akun", Toast.LENGTH_SHORT).show();
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                    Toast.makeText(SignUpPage.this, "Gagal membuat akun" + task.getException().getMessage(),  Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    private boolean validateForm() {
        boolean hasil = true;
        if (TextUtils.isEmpty(et_fullname.getText().toString())) {
            et_fullname.setError("Tidak boleh kosong");
            hasil = false;
        } else {
            et_fullname.setError(null);
        }
        if (TextUtils.isEmpty(et_email.getText().toString())) {
            et_email.setError("Tidak boleh kosong");
            hasil = false;
        } else {
            et_email.setError(null);
        }
        if (TextUtils.isEmpty(et_username.getText().toString())) {
            et_username.setError("Tidak boleh kosong");
            hasil = false;
        } else {
            et_username.setError(null);
        }
        if (TextUtils.isEmpty(et_password.getText().toString())) {
            et_password.setError("Tidak boleh kosong");
            hasil = false;
        } else {
            et_password.setError(null);
        }
        return hasil;
    }
}
