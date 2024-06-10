package ikhwan.hanif.melanichat.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ikhwan.hanif.melanichat.databinding.ItemContainerUserBinding;
import ikhwan.hanif.melanichat.listeners.UserListener;
import ikhwan.hanif.melanichat.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users; // Daftar pengguna
    private final UserListener userListener; // Listener untuk meng-handle klik pada item pengguna

    // Konstruktor untuk UserAdapter
    public UserAdapter(List<User> users, UserListener userListener) {
        this.users = users; // Inisialisasi daftar pengguna
        this.userListener = userListener; // Inisialisasi listener untuk meng-handle klik pada item pengguna
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menginflate tampilan item user dari layout XML menggunakan data binding
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        // Mengembalikan ViewHolder baru dengan tampilan item user yang diinflate
        return new UserViewHolder(itemContainerUserBinding, userListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        holder.setUserData(users.get(position)); // Mengatur data pengguna pada ViewHolder
    }

    @Override
    public int getItemCount() {
        return users.size(); // Mengembalikan jumlah total pengguna dalam daftar
    }

    // ViewHolder untuk item pengguna
    static class UserViewHolder extends RecyclerView.ViewHolder {

        ItemContainerUserBinding binding; // Binding untuk tampilan item pengguna
        UserListener userListener; // Listener untuk meng-handle klik pada item pengguna

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding, UserListener userListener) {
            super(itemContainerUserBinding.getRoot()); // Mendapatkan root view dari binding
            this.binding = itemContainerUserBinding; // Inisialisasi binding
            this.userListener = userListener; // Inisialisasi listener
        }

        // Mengatur data pengguna pada tampilan item pengguna
        void setUserData(User user) {
            binding.textName.setText(user.name); // Menetapkan nama pengguna
            binding.textEmail.setText(user.email); // Menetapkan email pengguna
            Bitmap userImage = getUserImage(user.image); // Mendapatkan gambar pengguna dari string yang dienkripsi
            if (userImage != null) {
                binding.imageProfile.setImageBitmap(userImage); // Menetapkan gambar profil pengguna jika tidak null
            } else {
                Toast.makeText(binding.getRoot().getContext(), "null", Toast.LENGTH_SHORT).show(); // Menampilkan pesan jika gambar pengguna null
            }
            // Menetapkan onClickListener untuk item pengguna untuk menangani klik
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }

        // Metode untuk mendapatkan gambar pengguna dari string yang dienkripsi
        private Bitmap getUserImage(String encodedImage) {
            if (encodedImage == null || encodedImage.isEmpty()) {
                return null;
            }
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT); // Mendekode string menjadi byte array
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length); // Mengubah byte array menjadi gambar Bitmap
        }
    }
}
