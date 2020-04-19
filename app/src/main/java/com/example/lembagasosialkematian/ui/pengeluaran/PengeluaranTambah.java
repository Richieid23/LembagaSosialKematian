package com.example.lembagasosialkematian.ui.pengeluaran;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.lembagasosialkematian.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PengeluaranTambah extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog loading;
    EditText etPengeluaran, etTanggal, etjumlah;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran_tambah);

        etPengeluaran = findViewById(R.id.in_pengeluaran);
        etTanggal = findViewById(R.id.in_tanggalpengeluaran);
        etjumlah = findViewById(R.id.in_jumlahpengeluaran);
        btnSimpan = findViewById(R.id.btn_simpanpenegluaran);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(PengeluaranTambah.this,"Menambahkan...","Tunggu...",false,true);
                String pengeluaran = etPengeluaran.getText().toString();
                String tanggal = etTanggal.getText().toString();
                String jumlah = etjumlah.getText().toString();

                if (!isEmpty(pengeluaran)
                        && !isEmpty(tanggal)
                        && !isEmpty(jumlah)){
                    Map<String, Object> map = new HashMap<>();
                    map.put("Pengeluaran", pengeluaran);
                    map.put("Tanggal", tanggal);
                    map.put("Jumlah", jumlah);

                    db.collection("Pengeluaran").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            loading.dismiss();
                            etPengeluaran.setText("");
                            etTanggal.setText("");
                            etjumlah.setText("");
                            Snackbar.make(findViewById(R.id.btn_simpansumbangan), "Pengeluaran berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else
                    Snackbar.make(findViewById(R.id.btn_simpansumbangan), "Data sumbangan tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        etPengeluaran.getWindowToken(), 0);
            }
        });
    }

    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }
}
