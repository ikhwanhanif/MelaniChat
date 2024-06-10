// Deklarasi package dan activity serta import library yang dibutuhkan
package ikhwan.hanif.melanichat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import ikhwan.hanif.melanichat.R;
import ikhwan.hanif.melanichat.utilities.Constants;

// Deklarasi kelas utama
public class ResetPasswordActivity extends AppCompatActivity {

    // Deklarasi elemen UI dan variabel terkait
    private EditText inputEmail, inputNewPassword, inputConfirmPassword;
    private Button buttonVerifyEmail, buttonSubmitNewPassword, buttonCancel, buttonCancel2;
    private FirebaseFirestore database;
    private String userId;
    private TextView textView;
    ProgressBar progressBar;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;

    // Metode onCreate() untuk inisialisasi aktivitas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        progressBar = findViewById(R.id.progressBar);

        // Mendapatkan layout dan menerapkan animasi latar belakang
        RelativeLayout layout = findViewById(R.id.resetPassword);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        // Pengaturan tata letak elemen UI dan penanganan klik tombol
        textView = findViewById(R.id.textView);
        buttonCancel2 = findViewById(R.id.buttonCancel2);
        linearLayout4 = findViewById(R.id.buttonLayout2);
        buttonCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this, SignInActivity.class));
                finish();
            }
        });

        // Inisialisasi elemen UI dan database
        linearLayout1 = findViewById(R.id.passwordLayout);
        linearLayout2 = findViewById(R.id.confirmPasswordLayout);
        linearLayout3 = findViewById(R.id.buttonLayout);
        inputEmail = findViewById(R.id.emailET);
        inputNewPassword = findViewById(R.id.inputNewPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonVerifyEmail = findViewById(R.id.buttonEmailVerify);
        buttonSubmitNewPassword = findViewById(R.id.buttonSubmitNewPassword);
        buttonCancel = findViewById(R.id.buttonCancel);
        database = FirebaseFirestore.getInstance();

        // Penanganan klik tombol "Verify Email" dan "Submit New Password"
        buttonVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();

                // Validasi input email
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();

                }
                else {
                    linearLayout4.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    verifyEmail();
                }
            }
        });

        buttonSubmitNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = inputNewPassword.getText().toString().trim();
                String confirmPassword = inputConfirmPassword.getText().toString().trim();

                // Validasi input kata sandi baru dan konfirmasi kata sandi
                if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();

                }

                else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                }

                // Memastikan bahwa ID pengguna telah ditemukan sebelum mengatur ulang kata sandi
                else if (userId == null) {
                    Toast.makeText(ResetPasswordActivity.this, "User ID is not found, please verify your email first", Toast.LENGTH_SHORT).show();
                }
                else{
                    showResetPasswordConfirmationDialog();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this, SignInActivity.class));
                finish();
            }
        });
    }

    // Metode untuk memeriksa apakah email yang dimasukkan oleh pengguna telah diverifikasi
    private void verifyEmail() {
        final String email = inputEmail.getText().toString().trim();


        // Mencari email pengguna di database Firestore
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            userId = document.getId();

                            // Menampilkan elemen UI terkait reset password
                            linearLayout1.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.VISIBLE);
                            linearLayout3.setVisibility(View.VISIBLE);
                            inputEmail.setVisibility(View.GONE);
                            textView.setText("Reset Password");
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(ResetPasswordActivity.this, "Email verified, you can now reset your password", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            linearLayout4.setVisibility(View.VISIBLE);
                            Toast.makeText(ResetPasswordActivity.this, "Please enter your correct email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Metode untuk mereset kata sandi pengguna
    private void resetPassword() {
        String newPassword = inputNewPassword.getText().toString().trim();

        // Mengirimkan pembaruan kata sandi ke Firebase Firestore
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .update(Constants.KEY_PASSWORD, newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Metode untuk menampilkan dialog konfirmasi reset kata sandi
    private void showResetPasswordConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Are you sure you want to reset your password?")
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetPassword();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
