package com.example.lembagasosialkematian.ui.berhenti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lembagasosialkematian.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class MeninggalDetail extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration firestoreListener;
    TextView tvNama, tvNik, tvKk, tvTempat, tvtglLahir, tvKelamin, tvStatus, tvAgama, tvTerdaftar, tvAlamat;
    String nik, Kk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meninggal_detail);

        tvNama = findViewById(R.id.tv_detailmeninggal_name);
        tvNik = findViewById(R.id.tv_detailmeninggal_nik);
        tvKk = findViewById(R.id.tv_detailmeninggal_kk);
        tvTempat = findViewById(R.id.tv_detailmeninggal_tempat);
        tvtglLahir = findViewById(R.id.tv_detailmeninggal_tl);
        tvKelamin = findViewById(R.id.tv_detailmeninggal_kelamin);
        tvStatus = findViewById(R.id.tv_detailmeninggal_status);
        tvAgama = findViewById(R.id.tv_detailmeninggal_agama);
        tvTerdaftar = findViewById(R.id.tv_detailmeninggal_terdaftar);
        tvAlamat = findViewById(R.id.tv_detailmeninggal_alamat);

        Bundle bundle = getIntent().getExtras();
        nik = bundle.getString("NIK");
        Kk = bundle.getString("KK");

        getMeninggal(nik);
    }

    private void getMeninggal(String nik) {
        DocumentReference docref = db.collection("Meninggal").document(nik);
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot doc) {
                if (doc.exists()){
                    final String NIK = doc.getString("NIK");
                    final String KK = doc.getString("KK");
                    final String Nama = doc.getString("Nama");
                    final String Tempat = doc.getString("Tempat");
                    final String Tanggal = doc.getString("TglLahir");
                    final String Kelamin = doc.getString("Kelamin");
                    final String Agama = doc.getString("Agama");
                    final String Terdaftar = doc.getString("Terdaftar");
                    final String Alamat = doc.getString("Alamat");
                    final String Status = doc.getString("Status");

                    Meninggal peserta = new Meninggal (NIK, KK, Nama, Tempat, Tanggal, Kelamin, Agama, Terdaftar, Alamat, Status);
                    tvNik.setText(peserta.getNIK());
                    tvKk.setText(peserta.getKK());
                    tvNama.setText(peserta.getNama());
                    tvTempat.setText(peserta.getTempat());
                    tvtglLahir.setText(peserta.getTglLahir());
                    tvKelamin.setText(peserta.getKelamin());
                    tvAgama.setText(peserta.getAgama());
                    tvTerdaftar.setText(peserta.getTerdaftar());
                    tvAlamat.setText(peserta.getAlamat());
                    tvStatus.setText(peserta.getStatus());

                }
                else {
                    Toast.makeText(MeninggalDetail.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { }
        });
    }
}
