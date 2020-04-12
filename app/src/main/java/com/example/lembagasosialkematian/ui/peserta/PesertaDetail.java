package com.example.lembagasosialkematian.ui.peserta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class PesertaDetail extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tvNama, tvNik, tvKk, tvTempat, tvtglLahir, tvKelamin, tvStatus, tvAgama, tvTerdaftar, tvAlamat;
    Button btnHapus;
    String nik;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta_detail);

        tvNama = findViewById(R.id.tv_detail_name);
        tvNik = findViewById(R.id.tv_detail_nik);
        tvKk = findViewById(R.id.tv_detail_kk);
        tvTempat = findViewById(R.id.tv_detail_tempat);
        tvtglLahir = findViewById(R.id.tv_detail_tl);
        tvKelamin = findViewById(R.id.tv_detail_kelamin);
        tvStatus = findViewById(R.id.tv_detail_status);
        tvAgama = findViewById(R.id.tv_detail_agama);
        tvTerdaftar = findViewById(R.id.tv_detail_terdaftar);
        tvAlamat = findViewById(R.id.tv_detail_alamat);
        btnHapus = findViewById(R.id.btn_hapus_peserta);

        btnHapus.setOnClickListener(this);
        getPeserta();
    }

    public void getPeserta(){
        loading.show(PesertaDetail.this,"Mengambil Data", "Tunggu...");
        Bundle bundle = getIntent().getExtras();
        nik = bundle.getString("NIK");
        DocumentReference docref = db.collection("Peserta").document(nik);
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if (doc.exists()){
                    String NIK = doc.getString("NIK");
                    String KK = doc.getString("KK");
                    String Nama = doc.getString("Nama");
                    String Tempat = doc.getString("Tempat");
                    String Tanggal = doc.getString("TglLahir");
                    String Kelamin = doc.getString("Kelamin");
                    String Agama = doc.getString("Agama");
                    String Terdaftar = doc.getString("Terdaftar");
                    String Alamat = doc.getString("Alamat");
                    String Status = doc.getString("Status");

                    Peserta peserta = new Peserta (NIK, KK, Nama, Tempat, Tanggal, Kelamin, Agama, Terdaftar, Alamat, Status);
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

                    loading.dismiss();
                }
                else {
                    Toast.makeText(PesertaDetail.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

