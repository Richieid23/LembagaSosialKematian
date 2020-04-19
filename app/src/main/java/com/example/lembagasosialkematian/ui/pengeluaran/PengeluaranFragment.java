package com.example.lembagasosialkematian.ui.pengeluaran;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PengeluaranFragment extends Fragment {
    FloatingActionButton fabTambah;
    RecyclerView rvPengeluaran;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private ListenerRegistration firestoreListener;
    private ArrayList<Pengeluaran> listpengeluaran;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pengeluaran, container, false);

        fabTambah = root.findViewById(R.id.fab_tambahPengeluaran);
        rvPengeluaran = root.findViewById(R.id.rv_pengeluaran);
        rvPengeluaran.setHasFixedSize(true);
        rvPengeluaran.setLayoutManager(new LinearLayoutManager(getActivity()));

        getPengeluaran();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PengeluaranTambah.class);
                startActivity(intent);
            }
        });
    }

    private void getPengeluaran() {
        firestoreListener = db.collection("Pengeluaran")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("error", "Listen failed!", e);
                            return;
                        }

                        listpengeluaran = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Pengeluaran pengeluaran = doc.toObject(Pengeluaran.class);
                            pengeluaran.setId(doc.getId());
                            listpengeluaran.add(pengeluaran);
                        }
//                        adapter.notifyDataSetChanged();
//                        rvPeserta.setAdapter(adapter);
                    }
                });
        Query query = db.collection("Pengeluaran");
        final FirestoreRecyclerOptions<Pengeluaran> options = new FirestoreRecyclerOptions.Builder<Pengeluaran>()
                .setQuery(query, Pengeluaran.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Pengeluaran, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int i, @NonNull final Pengeluaran model) {
                final Pengeluaran pengeluaran = listpengeluaran.get(i);
                holder.tvPengeluaran.setText(model.getPengeluaran());
                holder.tvTanggal.setText(model.getTanggal());
                holder.tvJumlah.setText("Rp. " + model.getJumlah());
                holder.btHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hapusPengeluaran(pengeluaran.getId());
                    }
                });
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengeluaran, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvPengeluaran.setAdapter(adapter);
        adapter.startListening();
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvPengeluaran, tvJumlah, tvTanggal;
        Button btDetail, btHapus;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJumlah = itemView.findViewById(R.id.tv_jumlahpengeluaran);
            tvPengeluaran = itemView.findViewById(R.id.tv_pengeluaran);
            tvTanggal = itemView.findViewById(R.id.tv_tanggalpengeluaran);
            btHapus = itemView.findViewById(R.id.btn_pengeluaran_hapus);
        }
    }

    private void detailPeserta(Pengeluaran pengeluaran) {
//        Intent intent = new Intent(getActivity(), PesertaDetail.class);
//        intent.putExtra("NIK", peserta.getNIK());
//        intent.putExtra("KK", peserta.getKK());
//        startActivity(intent);
    }

    private void hapusPengeluaran(String id) {
        db.collection("Pengeluaran")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Data Peserta Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
