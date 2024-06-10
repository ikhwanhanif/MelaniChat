package ikhwan.hanif.melanichat.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

// Layanan FirebaseMessagingService untuk menangani pesan Firebase Cloud Messaging (FCM)
public class MessagingService extends FirebaseMessagingService {

    // Metode untuk menangani pembuatan token baru
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token); // Memanggil metode induk
        // Menampilkan token baru di log
        Log.d("FCM Token", token);
    }

    // Metode untuk menangani penerimaan pesan
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message); // Memanggil metode induk
        // Menampilkan pesan yang diterima di log
        Log.d("FCM Message", "Received message: " + message.getData());
    }
}
