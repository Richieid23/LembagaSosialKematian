package com.example.lembagasosialkematian.ui.peserta;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lembagasosialkematian.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PesertaAdapter extends FirestoreRecyclerAdapter<Peserta, PesertaAdapter.ViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PesertaAdapter(@NonNull FirestoreRecyclerOptions<Peserta> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Peserta model) {
//        Peserta peserta = +
        holder.tvNama.setText(model.getNama());
        holder.tvNik.setText(model.getNIK());
//                Glide.with(getApplicationContext())
//                        .load(model.getImage())
//                        .into(holder.imageView);

        holder.btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peserta, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNik;
        Button btDetail;
        ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_peserta);
            tvNik = itemView.findViewById(R.id.tv_nik_peserta);
            btDetail = itemView.findViewById(R.id.btn_peserta_detail);
        }
    }
}
