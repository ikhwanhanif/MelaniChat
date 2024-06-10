package ikhwan.hanif.melanichat.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;

import ikhwan.hanif.melanichat.R;
import ikhwan.hanif.melanichat.utilities.Constants;
import ikhwan.hanif.melanichat.utilities.PreferencesManager;

public class SignInActivity extends AppCompatActivity {

    TextView SignUpText, forgotPassword;
    Button loginBtn;
    LinearLayout ganti_bahasa;

    TextInputEditText email, password;
    ProgressBar progressBar;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        preferencesManager = new PreferencesManager(getApplicationContext());

        // Pengaturan onClickListener untuk lupa kata sandi
        forgotPassword = findViewById(R.id.textForgotPass);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
                finish();
            }
        });

        // Cek apakah pengguna sudah masuk sebelumnya, jika ya langsung alihkan ke MainActivity
        if (preferencesManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Animasi latar belakang
        RelativeLayout layout = findViewById(R.id.signin);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        // Inisialisasi elemen UI dan variabel terkait
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passET);
        progressBar = findViewById(R.id.progressBar);

        // Memuat pengaturan bahasa yang disimpan
        loadLocale();
        ganti_bahasa = findViewById(R.id.gantiBahasa);
        ganti_bahasa.setOnClickListener(view -> {
            showBarGantiBahasa();
        });

        // Pengaturan onClickListener untuk SignUpText
        SignUpText = findViewById(R.id.textSignUp);
        SignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

        // Pengaturan onClickListener untuk tombol masuk
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memeriksa validitas rincian masuk sebelum mencoba masuk
                if (isValidSignInDetails()){
                    signIn();
                }
            }
        });
    }

    // Metode untuk melakukan proses masuk
    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, email.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, password.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size()>0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferencesManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferencesManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferencesManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferencesManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loading(false);
                        showToast(getString(R.string.tidak_dapat_masuk));
                    }
                });
    }

    // Metode untuk menampilkan atau menyembunyikan ProgressBar
    private void loading(Boolean isLoading){
        if (isLoading){
            loginBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    // Metode untuk mengatur pengaturan bahasa aplikasi
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // Metode untuk memuat pengaturan bahasa yang disimpan
    private void loadLocale() {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);
    }

    // Metode untuk menampilkan dialog pilihan bahasa
    private void showBarGantiBahasa() {
        final String[] listItems = {"Indonesia", "English", "عربي", "France"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SignInActivity.this);
        mBuilder.setTitle(getString(R.string.pilih_bahasa));
        mBuilder.setSingleChoiceItems(listItems, -1, (dialogInterface, i) -> {
            if (i == 0){
                setLocale("in");
                recreate();
                startActivity(new Intent(SignInActivity.this, SplashActivity.class));
                finish();
            }
            if (i == 1){
                setLocale("en");
                recreate();
                startActivity(new Intent(SignInActivity.this, SplashActivity.class));
                finish();
            }
            if (i == 2){
                setLocale("ar");
                recreate();
                startActivity(new Intent(SignInActivity.this, SplashActivity.class));
                finish();
            }
            if (i == 3){
                setLocale("fr");
                recreate();
                startActivity(new Intent(SignInActivity.this, SplashActivity.class));
                finish();
            }
            dialogInterface.dismiss();
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    // Metode untuk menampilkan pesan toast
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Metode untuk memeriksa validitas rincian masuk
    private Boolean isValidSignInDetails(){
        if (email.getText().toString().trim().isEmpty()){
            showToast(getString(R.string.enter_email));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            showToast(getString(R.string.enter_valid_email));
            return false;
        } else if (password.getText().toString().trim().isEmpty()){
            showToast(getString(R.string.enter_password));
            return false;
        } else {
            return true;
        }
    }
}

