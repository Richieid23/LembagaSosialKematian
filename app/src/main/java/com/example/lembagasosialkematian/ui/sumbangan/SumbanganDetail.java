package com.example.lembagasosialkematian.ui.sumbangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lembagasosialkematian.R;
import com.example.lembagasosialkematian.ui.peserta.Peserta;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SumbanganDetail extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;
    ListenerRegistration firestoreListener;
    ArrayList<Sumbangan> listsumbangan;
    RecyclerView rvSumbangan;
    String dusun;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sumbangan_detail);

        rvSumbangan = findViewById(R.id.rv_sumbangandetail);
        rvSumbangan.setHasFixedSize(true);
        rvSumbangan.setLayoutManager(new LinearLayoutManager(SumbanganDetail.this));
        getSumbangan();
        firestoreListener = db.collection("Sumbangan")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("error", "Listen failed!", e);
                            return;
                        }

                        listsumbangan = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Sumbangan sumbangan = doc.toObject(Sumbangan.class);
                            sumbangan.setId(doc.getId());
                            listsumbangan.add(sumbangan);
                        }
//                        adapter.notifyDataSetChanged();
                        rvSumbangan.setAdapter(adapter);
                    }
                });
    }

    public void getSumbangan(){
        Bundle bundle = getIntent().getExtras();
        dusun = bundle.getString("Dusun");
        Query query = db.collection("Sumbangan").whereEqualTo("Dusun", dusun);
        final FirestoreRecyclerOptions<Sumbangan> options = new FirestoreRecyclerOptions.Builder<Sumbangan>()
                .setQuery(query, Sumbangan.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Sumbangan, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int i, @NonNull final Sumbangan model) {
                final Sumbangan sumbangan = listsumbangan.get(i);
                holder.tvNama.setText(model.getPenyumbang());
                holder.tvTanggal.setText(model.getTanggal());
                holder.tvJumlah.setText("Rp. " + model.getJumlah());
                holder.tvDusun.setText(model.getDusun());
                holder.btnHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hapusSumbangan(sumbangan.getId());
                    }
                });
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sumbangan_detail, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvSumbangan.setAdapter(adapter);
        adapter.startListening();
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal, tvJumlah, tvDusun;
        Button btnHapus;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_penyumbang);
            tvTanggal = itemView.findViewById(R.id.tv_tanggalsumbangan);
            tvJumlah = itemView.findViewById(R.id.tv_jumlahsumbangan);
            tvDusun = itemView.findViewById(R.id.tv_dusunpenyumbang);
            btnHapus = itemView.findViewById(R.id.btn_hapussumbangan);
        }
    }

    public void hapusSumbangan(String id){
        db.collection("Sumbangan")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SumbanganDetail.this, "Data Peserta Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
