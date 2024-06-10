package ikhwan.hanif.melanichat.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ikhwan.hanif.melanichat.databinding.ItemContainerRecentConversionBinding;
import ikhwan.hanif.melanichat.listeners.ConversionListener;
import ikhwan.hanif.melanichat.models.ChatMessage;
import ikhwan.hanif.melanichat.models.User;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {
    private final List<ChatMessage> chatMessages; // Daftar pesan obrolan terbaru
    private final ConversionListener conversionListener; // Listener untuk meng-handle klik pada item obrolan

    // Konstruktor untuk RecentConversationsAdapter
    public RecentConversationsAdapter(List<ChatMessage> chatMessages, ConversionListener conversionListener) {
        this.chatMessages = chatMessages; // Inisialisasi daftar pesan obrolan terbaru
        this.conversionListener = conversionListener; // Inisialisasi listener untuk meng-handle klik pada item obrolan
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationsAdapter.ConversionViewHolder holder, int position) {
        holder.setData(chatMessages.get(position)); // Mengatur data untuk ViewHolder obrolan terbaru
    }

    @Override
    public int getItemCount() {
        return chatMessages.size(); // Mengembalikan jumlah total pesan obrolan terbaru
    }

    // ViewHolder untuk item obrolan terbaru
    class ConversionViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversionBinding binding; // Binding untuk tampilan item obrolan terbaru

        ConversionViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot()); // Mendapatkan root view dari binding
            binding = itemContainerRecentConversionBinding; // Inisialisasi binding
        }

        // Mengatur data untuk tampilan item obrolan terbaru
        void setData(ChatMessage chatMessage) {
            binding.imageProfile.setImageBitmap(getConversionImage(chatMessage.conversionImage)); // Menetapkan gambar profil obrolan
            binding.textName.setText(chatMessage.conversionName); // Menetapkan nama obrolan
            binding.textRecentMessage.setText(chatMessage.message); // Menetapkan pesan terbaru
            binding.getRoot().setOnClickListener(v -> {
                // Menyiapkan objek User dari data ChatMessage untuk memberikan informasi ke listener saat item di-klik
                User user = new User();
                user.id = chatMessage.conversionId; // Menetapkan ID pengguna obrolan
                user.name = chatMessage.conversionName; // Menetapkan nama pengguna obrolan
                user.image = chatMessage.conversionImage; // Menetapkan gambar profil pengguna obrolan
                conversionListener.onConversionClicked(user); // Memanggil metode onConversionClicked pada listener
            });
        }
    }

    // Metode untuk mendapatkan gambar obrolan dari string yang dienkripsi
    private Bitmap getConversionImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT); // Mendekode string menjadi byte array
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length); // Mengubah byte array menjadi gambar Bitmap
    }
}
