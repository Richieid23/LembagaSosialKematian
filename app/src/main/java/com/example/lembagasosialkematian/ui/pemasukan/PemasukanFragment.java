package com.example.lembagasosialkematian.ui.pemasukan;

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

import com.example.lembagasosialkematian.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PemasukanFragment extends Fragment {
    RecyclerView rvPemasukan;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private ListenerRegistration firestoreListener;
    private ArrayList<Pemasukan> listpemasukan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pemasukan, container, false);
        rvPemasukan = root.findViewById(R.id.rv_pemasukan);
        rvPemasukan.setHasFixedSize(true);
        rvPemasukan.setLayoutManager(new LinearLayoutManager(getActivity()));

        getPemasukan();
        return root;
    }

    private void getPemasukan() {
        Query query = db.collection("Sumbangan");
        final FirestoreRecyclerOptions<Pemasukan> options = new FirestoreRecyclerOptions.Builder<Pemasukan>()
                .setQuery(query, Pemasukan.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Pemasukan, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int i, @NonNull final Pemasukan model) {
//                final Pemasukan pemasukan = listpemasukan.get(i);
                holder.tvNama.setText(model.getPenyumbang());
                holder.tvTanggal.setText(model.getTanggal());
                holder.tvJumlah.setText("Rp. " + model.getJumlah());
                holder.tvDusun.setText(model.getDusun());
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pemasukan, parent, false);
                return new DataViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvPemasukan.setAdapter(adapter);
        adapter.startListening();
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal, tvJumlah, tvDusun;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_penyumbang);
            tvTanggal = itemView.findViewById(R.id.tv_tanggalsumbangan);
            tvJumlah = itemView.findViewById(R.id.tv_jumlahsumbangan);
            tvDusun = itemView.findViewById(R.id.tv_dusunpenyumbang);
        }
    }
}
