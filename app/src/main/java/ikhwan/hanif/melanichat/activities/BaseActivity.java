package ikhwan.hanif.melanichat.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ikhwan.hanif.melanichat.utilities.Constants;
import ikhwan.hanif.melanichat.utilities.PreferencesManager;

/**
 * BaseActivity adalah kelas dasar yang digunakan untuk aktivitas dalam aplikasi.
 * Kelas ini memperbarui status ketersediaan pengguna di Firestore saat aktivitas dimulai, di-pause, atau dilanjutkan.
 */
public class BaseActivity extends AppCompatActivity {
    private DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Membuat instance dari PreferencesManager untuk mengambil data yang disimpan di SharedPreferences
        PreferencesManager preferencesManager = new PreferencesManager(getApplicationContext());
        // Menginisialisasi FirebaseFirestore untuk berinteraksi dengan Firestore database
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // Mengambil referensi dokumen pengguna dari koleksi pengguna di Firestore
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferencesManager.getString(Constants.KEY_USER_ID));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Mengupdate status ketersediaan pengguna menjadi tidak tersedia (0) ketika aktivitas di-pause
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Mengupdate status ketersediaan pengguna menjadi tersedia (1) ketika aktivitas dilanjutkan
        documentReference.update(Constants.KEY_AVAILABILITY, 1);
    }
}
