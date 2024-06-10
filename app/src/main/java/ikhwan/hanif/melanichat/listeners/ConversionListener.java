package ikhwan.hanif.melanichat.listeners;

import ikhwan.hanif.melanichat.models.User;

// Antarmuka untuk mendengarkan klik pada daftar percakapan terbaru
public interface ConversionListener {
    void onConversionClicked(User user); // Metode yang dipanggil ketika pengguna mengklik percakapan
}
