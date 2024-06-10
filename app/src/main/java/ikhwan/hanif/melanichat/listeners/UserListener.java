package ikhwan.hanif.melanichat.listeners;

import ikhwan.hanif.melanichat.models.User;

// Antarmuka untuk mendengarkan klik pada daftar pengguna
public interface UserListener {
    void onUserClicked(User user); // Metode yang dipanggil ketika pengguna mengklik pengguna tertentu
}
