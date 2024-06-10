package ikhwan.hanif.melanichat.utilities;

import android.content.Context;
import android.content.SharedPreferences;

// Kelas untuk mengelola penyimpanan preferensi aplikasi
public class PreferencesManager {
    private final SharedPreferences sharedPreferences;

    // Konstruktor untuk PreferencesManager
    public PreferencesManager(Context context) {
        // Inisialisasi SharedPreferences
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    // Method untuk menyimpan nilai boolean ke preferensi
    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Method untuk mendapatkan nilai boolean dari preferensi
    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    // Method untuk menyimpan nilai string ke preferensi
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Method untuk mendapatkan nilai string dari preferensi
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    // Method untuk menghapus semua data dari preferensi
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
