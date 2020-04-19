package com.example.lembagasosialkematian.ui.peserta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lembagasosialkematian.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PesertaDetail extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration firestoreListener;
    TextView tvNama, tvNik, tvKk, tvTempat, tvtglLahir, tvKelamin, tvStatus, tvAgama, tvTerdaftar, tvAlamat;
    Button btnHapus;
    String nik, Kk;
    RecyclerView rvAnggota;
    private ArrayList<Peserta> listPeserta;
    private FirestoreRecyclerAdapter adapter;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta_detail);

        rvAnggota = findViewById(R.id.rv_anggota);
//        rvAnggota.setHasFixedSize(true);
//        rvAnggota.setLayoutManager(new LinearLayoutManager(PesertaDetail.this));

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

        Bundle bundle = getIntent().getExtras();
        nik = bundle.getString("NIK");
        Kk = bundle.getString("KK");

        getPeserta(nik);

        //Listener untuk ambil data anggota keluarga dari firestore
//        getAnggota(Kk);
//        firestoreListener = db.collection("Peserta").whereArrayContains("NIK", Kk)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.e("error", "Listen failed!", e);
//                            return;
//                        }
//
//                        listPeserta = new ArrayList<>();
//
//                        for (DocumentSnapshot doc : documentSnapshots) {
//                            Peserta peserta = doc.toObject(Peserta.class);
//                            peserta.setNIK(doc.getId());
//                            listPeserta.add(peserta);
//                        }
//
//                        adapter.notifyDataSetChanged();
//                        rvAnggota.setAdapter(adapter);
//                    }
//                });
    }

    public void getPeserta(String nik){
        DocumentReference docref = db.collection("Peserta").document(nik);
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

                    btnHapus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent inten = new Intent(PesertaDetail.this, PesertaUpdate.class);
                            inten.putExtra("NIK", NIK);
                            inten.putExtra("KK", KK);
                            inten.putExtra("Nama", Nama);
                            inten.putExtra("Tempat", Tempat);
                            inten.putExtra("Lahir", Tanggal);
                            inten.putExtra("Kelammin", Kelamin);
                            inten.putExtra("Agama", Agama);
                            inten.putExtra("Terdaftar", Terdaftar);
                            inten.putExtra("Alamat", Alamat);
                            inten.putExtra("Status", Status);
                            startActivity(inten);
                        }
                    });
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

    //Adapter Firestore recyclerview untuk mengambil data anggota keluarga dengan query nomer KK
//    public void getAnggota(String kk){
//        Query query = db.collection("Peserta").whereArrayContains("KK", kk);
//        final FirestoreRecyclerOptions<Peserta> options = new FirestoreRecyclerOptions.Builder<Peserta>()
//                .setQuery(query, Peserta.class)
//                .build();
//
//        adapter = new FirestoreRecyclerAdapter<Peserta, ViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull final ViewHolder holder, int i, @NonNull final Peserta model) {
//                final Peserta peserta = listPeserta.get(i);
//                holder.tvNama.setText(model.getNama());
//                holder.tvNik.setText(model.getNIK());
//                holder.btDetail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        detailPeserta(peserta);
//                    }
//                });
//                holder.btHapus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        hapusPeserta(peserta.getNIK());
//                    }
//                });
//            }
//            @NonNull
//            @Override
//            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anggota, parent, false);
//                return new ViewHolder(view);
//            }
//
//            @Override
//            public void onError(FirebaseFirestoreException e) {
//                Log.e("error", e.getMessage());
//            }
//        };
//        adapter.notifyDataSetChanged();
//        rvAnggota.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//    private class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tvNama, tvNik;
//        Button btDetail, btHapus;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvNama = itemView.findViewById(R.id.tv_nama_anggota);
//            tvNik = itemView.findViewById(R.id.tv_nik_anggota);
//            btDetail = itemView.findViewById(R.id.btn_anggota_detail);
//            btHapus = itemView.findViewById(R.id.btn_anggota_hapus);
//        }
//    }
}

