package com.example.lembagasosialkematian.ui.berhenti;

import android.content.Intent;
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

public class TabMeninggal extends Fragment {
    RecyclerView rvMeninggal;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;
    ListenerRegistration firestoreListener;
    ArrayList<Meninggal> listmeinggal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_meninggal, container, false);
        rvMeninggal = root.findViewById(R.id.rv_meninggal);
        rvMeninggal.setHasFixedSize(true);
        rvMeninggal.setLayoutManager(new LinearLayoutManager(getContext()));
        getMeninggal();
        return root;
    }

    public void getMeninggal(){
        firestoreListener = db.collection("Meninggal")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("error", "Listen failed!", e);
                            return;
                        }

                        listmeinggal = new ArrayList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Meninggal meninggal = doc.toObject(Meninggal.class);
                            meninggal.setNIK(doc.getId());
                            listmeinggal.add(meninggal);
                        }
                    }
                });
        Query query = db.collection("Meninggal");
        final FirestoreRecyclerOptions<Meninggal> options = new FirestoreRecyclerOptions.Builder<Meninggal>()
                .setQuery(query, Meninggal.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Meninggal, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int i, @NonNull final Meninggal model) {
                final Meninggal meninggal = listmeinggal.get(i);
                holder.tvNama.setText(model.getNama());
                holder.tvNik.setText(model.getNIK());
//                holder.btDetail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        detailMeninggal(meninggal);
//                    }
//                });
                holder.btHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hapusMeninggal(meninggal.getNIK());
                    }
                });
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meninggal, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvMeninggal.setAdapter(adapter);
        adapter.startListening();
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNik;
        Button btDetail, btHapus;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_meninggal_name);
            tvNik = itemView.findViewById(R.id.tv_meninggal_nik);
//            btDetail = itemView.findViewById(R.id.btn_meninggal_detail);
            btHapus = itemView.findViewById(R.id.btn_meninggal_hapus);
        }
    }

    private void detailMeninggal(Meninggal meninggal) {
        Intent intent = new Intent(getActivity(), MeninggalDetail.class);
        intent.putExtra("NIK", meninggal.getNIK());
        intent.putExtra("KK", meninggal.getKK());
        startActivity(intent);
    }

    private void hapusMeninggal(String id) {
        db.collection("Meninggal")
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
