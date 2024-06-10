package ikhwan.hanif.melanichat.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ikhwan.hanif.melanichat.R;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView; // ImageView untuk menampilkan gambar logo aplikasi
    TextView textView; // TextView untuk menampilkan teks deskripsi aplikasi
    LinearLayout linearLayout; // LinearLayout untuk mengelompokkan elemen UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Mengatur layout tampilan splash

        RelativeLayout layout = findViewById(R.id.splash); // Menginisialisasi layout utama
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground(); // Mengambil animasi latar belakang dari layout
        animationDrawable.setEnterFadeDuration(2000); // Mengatur durasi efek masuk
        animationDrawable.setExitFadeDuration(2000); // Mengatur durasi efek keluar
        animationDrawable.start(); // Memulai animasi latar belakang

        imageView = findViewById(R.id.imageView); // Mengambil ImageView dari layout
        textView = findViewById(R.id.textView); // Mengambil TextView dari layout
        linearLayout = findViewById(R.id.linearLayout); // Mengambil LinearLayout dari layout

        Animation animUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_up); // Membuat animasi naik
        Animation animDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_down); // Membuat animasi turun

        imageView.startAnimation(animDown); // Memulai animasi turun untuk ImageView
        textView.startAnimation(animDown); // Memulai animasi turun untuk TextView
        linearLayout.startAnimation(animUp); // Memulai animasi naik untuk LinearLayout

        new Handler().postDelayed(new Runnable() { // Menunda pemanggilan dengan menggunakan Handler
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SignInActivity.class)); // Memulai aktivitas SignInActivity setelah jeda 3 detik
                finish(); // Menutup aktivitas splash
            }
        }, 3000); // Jeda selama 3 detik sebelum memulai aktivitas SignInActivity
    }
}
