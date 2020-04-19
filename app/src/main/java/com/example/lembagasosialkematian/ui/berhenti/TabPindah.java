package com.example.lembagasosialkematian.ui.berhenti;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TabPindah extends Fragment {
    RecyclerView rvPindah;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;
    ListenerRegistration firestoreListener;
    ArrayList<Pindah> listpindah;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_pindah, container, false);
        rvPindah = root.findViewById(R.id.rv_pindah);
        rvPindah.setHasFixedSize(true);
        rvPindah.setLayoutManager(new LinearLayoutManager(getContext()));
        getPindah();
        return root;
    }

    private void getPindah() {
        firestoreListener = db.collection("Pindah")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("error", "Listen failed!", e);
                            return;
                        }

                        listpindah = new ArrayList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Pindah pindah = doc.toObject(Pindah.class);
                            pindah.setNIK(doc.getId());
                            listpindah.add(pindah);
                        }
                    }
                });
        Query query = db.collection("Pindah");
        final FirestoreRecyclerOptions<Pindah> options = new FirestoreRecyclerOptions.Builder<Pindah>()
                .setQuery(query, Pindah.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Pindah, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int i, @NonNull final Pindah model) {
                final Pindah pindah = listpindah.get(i);
                holder.tvNama.setText(model.getNama());
                holder.tvNik.setText(model.getNIK());
//                holder.btDetail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        detailPeserta(peserta);
//                    }
//                });
                holder.btHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hapusPindah(pindah.getNIK());
                    }
                });
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pindah, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvPindah.setAdapter(adapter);
        adapter.startListening();
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNik;
        Button btDetail, btHapus;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_pindah_name);
            tvNik = itemView.findViewById(R.id.tv_pindah_nik);
//            btDetail = itemView.findViewById(R.id.btn_pindah_detail);
            btHapus = itemView.findViewById(R.id.btn_pindah_hapus);
        }
    }

    private void detailPeserta(Meninggal meninggal) {
//        Intent intent = new Intent(getActivity(), PesertaDetail.class);
//        intent.putExtra("NIK", peserta.getNIK());
//        intent.putExtra("KK", peserta.getKK());
//        startActivity(intent);
    }

    private void hapusPindah(String id) {
        db.collection("Pindah")
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
