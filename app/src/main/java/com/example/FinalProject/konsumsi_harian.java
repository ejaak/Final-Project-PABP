package com.example.FinalProject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.FinalProject.Makanan;
import com.example.FinalProject.AdapterKonsumsi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class konsumsi_harian extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterKonsumsi adapterKonsumsi;
    private List<Makanan> makananList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konsumsi_harian);

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.konsumsi_hari_ini);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi AdapterKonsumsi dengan list kosong
        adapterKonsumsi = new AdapterKonsumsi(new ArrayList<>());
        recyclerView.setAdapter(adapterKonsumsi);

        // Inisialisasi daftar makanan
        makananList = new ArrayList<>();

        // Mendapatkan selected day dari Intent
        String selectedDay = getIntent().getStringExtra("DAY_OF_WEEK");

        // Mendapatkan referensi database untuk selected day
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Hari").child(selectedDay).child("Makanan");

        // Mendapatkan data dari Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                makananList.clear(); // Hapus data lama sebelum menambahkan yang baru

                // Iterasi setiap child di bawah "Makanan"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Makanan makanan = snapshot.getValue(Makanan.class);
                    makananList.add(makanan);
                }

                // Set data ke dalam adapter dan memberitahu bahwa data telah berubah
                adapterKonsumsi.setData(makananList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
