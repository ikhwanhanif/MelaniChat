package ikhwan.hanif.melanichat.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ikhwan.hanif.melanichat.databinding.ItemContainerSentMessageBinding;
import ikhwan.hanif.melanichat.databinding.ItemReceivedMessageBinding;
import ikhwan.hanif.melanichat.models.ChatMessage;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ChatMessage> chatMessages; // Daftar pesan obrolan
    private final Bitmap receiverProfileImage; // Gambar profil penerima
    private final String senderId; // ID pengirim

    private static final int VIEW_TYPE_SENT = 1; // Tipe tampilan untuk pesan yang dikirim
    private static final int VIEW_TYPE_RECEIVED = 2; // Tipe tampilan untuk pesan yang diterima

    // Konstruktor untuk ChatAdapter
    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages; // Inisialisasi daftar pesan obrolan
        this.receiverProfileImage = receiverProfileImage; // Inisialisasi gambar profil penerima
        this.senderId = senderId; // Inisialisasi ID pengirim
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menentukan tampilan ViewHolder berdasarkan tipe pesan
        if (viewType == VIEW_TYPE_SENT) { // Jika pesan dikirim
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else { // Jika pesan diterima
            return new ReceivedMessageViewHolder(
                    ItemReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Mengikat data pesan ke ViewHolder sesuai dengan tipe pesan
        if (getItemViewType(position) == VIEW_TYPE_SENT) { // Jika pesan dikirim
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position)); // Mengatur data untuk ViewHolder pesan yang dikirim
        } else { // Jika pesan diterima
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage); // Mengatur data untuk ViewHolder pesan yang diterima
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size(); // Mengembalikan jumlah total pesan
    }

    @Override
    public int getItemViewType(int position) {
        // Mendapatkan tipe tampilan berdasarkan ID pengirim pesan
        if (chatMessages.get(position).senderId.equals(senderId)) { // Jika pengirim adalah pengguna saat ini
            return VIEW_TYPE_SENT; // Mengembalikan tipe tampilan untuk pesan yang dikirim
        } else { // Jika pengirim bukan pengguna saat ini
            return VIEW_TYPE_RECEIVED; // Mengembalikan tipe tampilan untuk pesan yang diterima
        }
    }

    // ViewHolder untuk pesan yang dikirim
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding; // Binding untuk tampilan pesan yang dikirim

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot()); // Mendapatkan root view dari binding
            binding = itemContainerSentMessageBinding; // Inisialisasi binding
        }

        // Mengatur data pesan yang dikirim ke tampilan
        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message); // Menetapkan teks pesan
            binding.textDateTime.setText(chatMessage.dateTime); // Menetapkan teks waktu
        }
    }

    // ViewHolder untuk pesan yang diterima
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemReceivedMessageBinding binding; // Binding untuk tampilan pesan yang diterima

        ReceivedMessageViewHolder(ItemReceivedMessageBinding itemReceivedMessageBinding) {
            super(itemReceivedMessageBinding.getRoot()); // Mendapatkan root view dari binding
            binding = itemReceivedMessageBinding; // Inisialisasi binding
        }

        // Mengatur data pesan yang diterima ke tampilan
        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.message); // Menetapkan teks pesan
            binding.textDateTime.setText(chatMessage.dateTime); // Menetapkan teks waktu
            binding.imageProfile.setImageBitmap(receiverProfileImage); // Menetapkan gambar profil penerima
        }
    }
}
