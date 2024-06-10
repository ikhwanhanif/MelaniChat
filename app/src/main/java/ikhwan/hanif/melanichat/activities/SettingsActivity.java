package ikhwan.hanif.melanichat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.makeramen.roundedimageview.RoundedImageView;

import ikhwan.hanif.melanichat.R;
import ikhwan.hanif.melanichat.utilities.Constants;
import ikhwan.hanif.melanichat.utilities.PreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    ImageView imageViewDeleteAccount, back;
    RoundedImageView imageProfile;
    TextView textViewDeleteAccount, textName;
    private PreferencesManager preferencesManager;
    private FirebaseFirestore database;
    LinearLayout linearLayoutDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RelativeLayout layout = findViewById(R.id.settings);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        // Inisialisasi elemen UI dan variabel terkait
        imageProfile = findViewById(R.id.imageProfile);
        textName = findViewById(R.id.textName);

        back = findViewById(R.id.backImageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });

        linearLayoutDeleteAccount = findViewById(R.id.deleteAkunLn);
        imageViewDeleteAccount = findViewById(R.id.deleteAkunIV);
        textViewDeleteAccount = findViewById(R.id.deleteAkunTV);
        preferencesManager = new PreferencesManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();

        // Penanganan klik untuk menghapus akun
        View.OnClickListener deleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        };

        imageViewDeleteAccount.setOnClickListener(deleteClickListener);
        textViewDeleteAccount.setOnClickListener(deleteClickListener);
        linearLayoutDeleteAccount.setOnClickListener(deleteClickListener);

        loadUserDetails();
    }

    // Metode untuk menampilkan dialog konfirmasi penghapusan akun
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Metode untuk menghapus akun pengguna
    private void deleteAccount() {
        String userId = preferencesManager.getString(Constants.KEY_USER_ID);
        if (userId != null) {
            // Menghapus data pengguna dari Firestore
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .document(userId)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Membersihkan data lokal
                                preferencesManager.clear();

                                // Mengalihkan ke SignInActivity
                                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText(SettingsActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingsActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Menghapus semua obrolan dan percakapan
            deleteUserChats(userId);
            deleteUserConversations(userId);
        }
    }

    // Metode untuk menghapus obrolan pengguna
    private void deleteUserChats(String userId) {
        // Menghapus obrolan pengguna sebagai pengirim
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                documentSnapshot.getReference().delete();
                            }
                        }
                    }
                });

        // Menghapus obrolan pengguna sebagai penerima
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                documentSnapshot.getReference().delete();
                            }
                        }
                    }
                });
    }

    // Metode untuk menghapus percakapan pengguna
    private void deleteUserConversations(String userId) {
        // Menghapus percakapan pengguna sebagai pengirim
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                documentSnapshot.getReference().delete();
                            }
                        }
                    }
                });

        // Menghapus percakapan pengguna sebagai penerima
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                documentSnapshot.getReference().delete();
                            }
                        }
                    }
                });
    }

    // Metode untuk memuat detail pengguna
    private void loadUserDetails() {
        // Memuat nama pengguna dan gambar profil dari penyimpanan lokal
        textName.setText(preferencesManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferencesManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageProfile.setImageBitmap(bitmap);
    }
}

