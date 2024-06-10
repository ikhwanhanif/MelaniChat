package ikhwan.hanif.melanichat.models;

import java.util.Date;

// Representasi data pesan obrolan
public class ChatMessage {
    public String senderId, receiverId, message, dateTime; // ID pengirim, ID penerima, pesan, tanggal dan waktu
    public Date dateObject; // Objek tanggal
    public String conversionId, conversionName, conversionImage; // ID obrolan, nama obrolan, gambar obrolan
}
