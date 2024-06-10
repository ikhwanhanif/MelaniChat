package ikhwan.hanif.melanichat.utilities;

// Kelas yang berisi konstanta-konstanta yang digunakan dalam aplikasi
public class Constants {

    // Nama-nama koleksi dalam database Firestore
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";

    // Nama-nama field yang digunakan dalam database Firestore
    public static final String KEY_NAME = "name"; // Nama pengguna
    public static final String KEY_EMAIL = "email"; // Email pengguna
    public static final String KEY_PASSWORD = "password"; // Kata sandi pengguna
    public static final String KEY_IMAGE = "image"; // Gambar profil pengguna
    public static final String KEY_IS_SIGNED_IN = "isSignedIn"; // Status masuk pengguna
    public static final String KEY_USER_ID = "userId"; // ID pengguna
    public static final String KEY_FCM_TOKEN = "fcmToken"; // Token FCM
    public static final String KEY_SENDER_ID = "senderId"; // ID pengirim pesan
    public static final String KEY_RECEIVER_ID = "receiverId"; // ID penerima pesan
    public static final String KEY_MESSAGE = "message"; // Isi pesan
    public static final String KEY_TIMESTAMP = "timestamp"; // Timestamp pesan
    public static final String KEY_SENDER_NAME = "senderName"; // Nama pengirim pesan
    public static final String KEY_RECEIVER_NAME = "receiverName"; // Nama penerima pesan
    public static final String KEY_SENDER_IMAGE = "senderImage"; // Gambar profil pengirim pesan
    public static final String KEY_RECEIVER_IMAGE = "receiverImage"; // Gambar profil penerima pesan
    public static final String KEY_LAST_MESSAGE = "lastMessage"; // Pesan terakhir dalam percakapan
    public static final String KEY_AVAILABILITY = "availability"; // Ketersediaan pengguna

    // Nama-nama field lainnya
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference"; // Nama penyimpanan preferensi
    public static final String KEY_USER = "user"; // Data pengguna
}
