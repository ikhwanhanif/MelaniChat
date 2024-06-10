package ikhwan.hanif.melanichat.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ikhwan.hanif.melanichat.R;
import ikhwan.hanif.melanichat.adapters.UserAdapter;
import ikhwan.hanif.melanichat.listeners.UserListener;
import ikhwan.hanif.melanichat.models.User;
import ikhwan.hanif.melanichat.utilities.Constants;
import ikhwan.hanif.melanichat.utilities.PreferencesManager;

public class UsersActivity extends BaseActivity implements UserListener {

    ProgressBar progressBar; // ProgressBar untuk menampilkan indikator loading
    PreferencesManager preferencesManager; // PreferencesManager untuk mengelola preferensi pengguna
    TextView textErrorMessage; // TextView untuk menampilkan pesan kesalahan
    RecyclerView userRecyclerView; // RecyclerView untuk menampilkan daftar pengguna
    AppCompatImageView imageBack; // ImageView untuk tombol kembali

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users); // Mengatur layout tampilan pengguna

        preferencesManager = new PreferencesManager(getApplicationContext()); // Menginisialisasi PreferencesManager
        RelativeLayout layout = findViewById(R.id.users); // Mengambil layout utama
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground(); // Mengambil animasi latar belakang dari layout
        animationDrawable.setEnterFadeDuration(2000); // Mengatur durasi efek masuk
        animationDrawable.setExitFadeDuration(2000); // Mengatur durasi efek keluar
        animationDrawable.start(); // Memulai animasi latar belakang

        imageBack = findViewById(R.id.imageBack); // Mengambil ImageView untuk tombol kembali dari layout
        imageBack.setOnClickListener(new View.OnClickListener() { // Menambahkan OnClickListener untuk tombol kembali
            @Override
            public void onClick(View v) {
                onBackPressed(); // Menjalankan metode onBackPressed saat tombol kembali ditekan
            }
        });
        userRecyclerView = findViewById(R.id.usersRecyclerView); // Mengambil RecyclerView untuk daftar pengguna dari layout
        textErrorMessage = findViewById(R.id.textErrorMessage); // Mengambil TextView untuk pesan kesalahan dari layout
        progressBar = findViewById(R.id.progressBar); // Mengambil ProgressBar untuk indikator loading dari layout

        getUsers(); // Memuat daftar pengguna
    }

    private void getUsers() {
        loading(true); // Menampilkan indikator loading

        FirebaseFirestore database = FirebaseFirestore.getInstance(); // Mendapatkan instance Firestore
        database.collection(Constants.KEY_COLLECTION_USERS) // Mengambil koleksi pengguna dari Firestore
                .get()
                .addOnCompleteListener(task -> { // Menambahkan listener saat tugas berhasil atau gagal
                    loading(false); // Menyembunyikan indikator loading

                    String currentUserId = preferencesManager.getString(Constants.KEY_USER_ID); // Mendapatkan ID pengguna saat ini dari preferensi

                    if (task.isSuccessful() && task.getResult() != null) { // Memeriksa apakah tugas berhasil dan hasilnya tidak null
                        List<User> users = new ArrayList<>(); // Membuat daftar kosong untuk menyimpan pengguna

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) { // Loop melalui setiap dokumen hasil
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) { // Melewati pengguna saat ini
                                continue;
                            }
                            // Membuat objek User dari dokumen Firestore
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();

                            users.add(user); // Menambahkan pengguna ke daftar
                        }

                        if (users.size() > 0) { // Memeriksa apakah daftar pengguna tidak kosong
                            UserAdapter userAdapter = new UserAdapter(users, this); // Membuat adapter untuk RecyclerView
                            userRecyclerView.setAdapter(userAdapter); // Mengatur adapter ke RecyclerView
                            userRecyclerView.setVisibility(View.VISIBLE); // Menampilkan RecyclerView
                        } else {
                            showErrorMessage(); // Menampilkan pesan kesalahan jika daftar pengguna kosong
                        }
                    } else {
                        showErrorMessage(); // Menampilkan pesan kesalahan jika tugas gagal
                    }
                });
    }

    private void showErrorMessage() {
        textErrorMessage.setText(String.format("%s", getString(R.string.no_user_available))); // Menetapkan pesan kesalahan
        textErrorMessage.setVisibility(View.VISIBLE); // Menampilkan TextView dengan pesan kesalahan
    }

    private void loading(Boolean isLoading) {
        if (isLoading) { // Jika isLoading adalah true
            progressBar.setVisibility(View.VISIBLE); // Menampilkan ProgressBar
        } else {
            progressBar.setVisibility(View.INVISIBLE); // Menyembunyikan ProgressBar
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class); // Membuat intent untuk membuka ChatActivity
        intent.putExtra(Constants.KEY_USER, user); // Menambahkan data pengguna ke intent
        startActivity(intent); // Memulai aktivitas ChatActivity
        finish(); // Menutup aktivitas pengguna
    }
}
