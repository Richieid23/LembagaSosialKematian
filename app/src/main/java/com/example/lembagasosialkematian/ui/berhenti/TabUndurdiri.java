package com.example.lembagasosialkematian.ui.berhenti;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TabUndurdiri extends Fragment {
    RecyclerView rvUndurdiri;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;
    ListenerRegistration firestoreListener;
    ArrayList<Undurdiri> listundurdiri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_undurdiri, container, false);
        rvUndurdiri = root.findViewById(R.id.rv_undurdiri);
        rvUndurdiri.setHasFixedSize(true);
        rvUndurdiri.setLayoutManager(new LinearLayoutManager(getContext()));
        getUndurdiri();
        return root;
    }

    private void getUndurdiri() {
        firestoreListener = db.collection("Undurdiri")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("error", "Listen failed!", e);
                            return;
                        }

                        listundurdiri = new ArrayList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Undurdiri undurdiri = doc.toObject(Undurdiri.class);
                            undurdiri.setNIK(doc.getId());
                            listundurdiri.add(undurdiri);
                        }
                    }
                });
        Query query = db.collection("Undurdiri");
        final FirestoreRecyclerOptions<Undurdiri> options = new FirestoreRecyclerOptions.Builder<Undurdiri>()
                .setQuery(query, Undurdiri.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Undurdiri, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int i, @NonNull final Undurdiri model) {
                final Undurdiri undurdiri = listundurdiri.get(i);
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
                        hapusUndurdiri(undurdiri.getNIK());
                    }
                });
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_undurdiri, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvUndurdiri.setAdapter(adapter);
        adapter.startListening();
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNik;
        Button btDetail, btHapus;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_undurdiri_name);
            tvNik = itemView.findViewById(R.id.tv_undurdiri_nik);
//            btDetail = itemView.findViewById(R.id.btn_undurdiri_detail);
            btHapus = itemView.findViewById(R.id.btn_undurdiri_hapus);
        }
    }

    private void hapusUndurdiri(String id) {
        db.collection("Undurdiri")
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
