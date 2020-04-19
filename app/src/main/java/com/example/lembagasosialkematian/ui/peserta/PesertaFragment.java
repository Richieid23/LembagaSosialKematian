package com.example.lembagasosialkematian.ui.peserta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class PesertaFragment extends Fragment {
    FloatingActionButton fabTambah;
    RecyclerView rvPeserta;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private ListenerRegistration firestoreListener;
    private ArrayList<Peserta> listPeserta;
    SearchView sc;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_peserta, container, false);

        fabTambah = root.findViewById(R.id.fab_tambahPeserta);
        rvPeserta = root.findViewById(R.id.rv_peserta);
        rvPeserta.setHasFixedSize(true);
        rvPeserta.setLayoutManager(new LinearLayoutManager(getActivity()));
        sc = root.findViewById(R.id.action_search);

        sc.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        getPeserta();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(getActivity(), PesertaTambah.class);
                startActivity(tambah);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firestoreListener.remove();
    }

    public void getPeserta(){
        firestoreListener = db.collection("Peserta")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("error", "Listen failed!", e);
                            return;
                        }

                        listPeserta = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Peserta peserta = doc.toObject(Peserta.class);
                            peserta.setNIK(doc.getId());
                            listPeserta.add(peserta);
                        }
//                        adapter.notifyDataSetChanged();
//                        rvPeserta.setAdapter(adapter);
                    }
                });
        Query query = db.collection("Peserta");
        final FirestoreRecyclerOptions<Peserta> options = new FirestoreRecyclerOptions.Builder<Peserta>()
                .setQuery(query, Peserta.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Peserta, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int i, @NonNull final Peserta model) {
                final Peserta peserta = listPeserta.get(i);
                holder.tvNama.setText(model.getNama());
                holder.tvNik.setText(model.getNIK());
                holder.btDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailPeserta(peserta);
                    }
                });
                holder.btHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hapusPeserta(peserta.getNIK());
                    }
                });
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peserta, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvPeserta.setAdapter(adapter);
        adapter.startListening();
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNik;
        Button btDetail, btHapus;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_peserta);
            tvNik = itemView.findViewById(R.id.tv_nik_peserta);
            btDetail = itemView.findViewById(R.id.btn_peserta_detail);
            btHapus = itemView.findViewById(R.id.btn_peserta_hapus);
        }
    }

    private void detailPeserta(Peserta peserta) {
        Intent intent = new Intent(getActivity(), PesertaDetail.class);
        intent.putExtra("NIK", peserta.getNIK());
        intent.putExtra("KK", peserta.getKK());
        startActivity(intent);
    }

    private void hapusPeserta(String id) {
        db.collection("Peserta")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Data Peserta Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchData(String query) {
        Query searchquery = db.collection("Peserta").whereEqualTo("Search", query.toLowerCase());
        final FirestoreRecyclerOptions<Peserta> options = new FirestoreRecyclerOptions.Builder<Peserta>()
                .setQuery(searchquery, Peserta.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Peserta, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int i, @NonNull final Peserta model) {
                final Peserta peserta = listPeserta.get(i);
                holder.tvNama.setText(model.getNama());
                holder.tvNik.setText(model.getNIK());
                holder.btDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailPeserta(peserta);
                    }
                });
                holder.btHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hapusPeserta(peserta.getNIK());
                    }
                });
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peserta, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvPeserta.setAdapter(adapter);
        adapter.startListening();
    }

}
