package com.example.lembagasosialkematian.ui.sumbangan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lembagasosialkematian.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SumbanganTambah extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog loading;
    EditText etNama, etTanggal, etSumbangan;
    TextView tvDusun;
    Button btnSimpan;
    Spinner spDusun;

    String[] dusun = {"Subahnala 1", "Subahnala 2", "Sandik",
            "Dumpu", "Selojan", "Penyengak", "Peresak Daye",
            "Peresak Lauk", "Bujak Daye", "Boak", "Batu Lajan",
            "Aik Gering", "Dasan Aman", "Pajangan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sumbangan_tambah);

        etNama = findViewById(R.id.in_penyumbang);
        etTanggal = findViewById(R.id.in_tanggal);
        etSumbangan = findViewById(R.id.in_jumlah);
        btnSimpan = findViewById(R.id.btn_simpansumbangan);
        tvDusun = findViewById(R.id.tv_dusunsumbangan);
        spDusun = findViewById(R.id.sp_Dusunsumbangan);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dusun);
        aa.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spDusun.setAdapter(aa);
        spDusun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvDusun.setText(dusun[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvDusun.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(SumbanganTambah.this,"Menambahkan...","Tunggu...",false,true);
                String nama = etNama.getText().toString();
                String tanggal = etTanggal.getText().toString();
                String jumlah = etSumbangan.getText().toString();
                String Dusun = tvDusun.getText().toString();

                if (!isEmpty(nama)
                        && !isEmpty(tanggal)
                        && !isEmpty(jumlah)
                        && !isEmpty(Dusun)){
                    Map<String, Object> sumbangan = new HashMap<>();
                    sumbangan.put("Penyumbang", nama);
                    sumbangan.put("Tanggal", tanggal);
                    sumbangan.put("Jumlah", jumlah);
                    sumbangan.put("Dusun", Dusun);

                    db.collection("Sumbangan").add(sumbangan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            loading.dismiss();
                            etNama.setText("");
                            etSumbangan.setText("");
                            etTanggal.setText("");
                            Snackbar.make(findViewById(R.id.btn_simpansumbangan), "Sumbangan berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else
                    Snackbar.make(findViewById(R.id.btn_simpansumbangan), "Data sumbangan tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        etNama.getWindowToken(), 0);
            }
        });
    }

    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }
}
