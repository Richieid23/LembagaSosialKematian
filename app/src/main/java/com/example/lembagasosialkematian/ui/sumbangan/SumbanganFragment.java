package com.example.lembagasosialkematian.ui.sumbangan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lembagasosialkematian.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SumbanganFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView listView;
    ArrayAdapter<CharSequence> adapter;
    FloatingActionButton fabTambah;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sumbangan, container, false);
        listView = root.findViewById(R.id.rv_sumbangan);
        adapter = ArrayAdapter.createFromResource(getActivity(),R.array.nama_dusun,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        fabTambah = root.findViewById(R.id.fab_tambahsumbangan);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SumbanganTambah.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String pos = listView.getItemAtPosition(position).toString();
        Intent intent = new Intent(getActivity(), SumbanganDetail.class);
        intent.putExtra("Dusun", pos);
        startActivity(intent);
    }
}
