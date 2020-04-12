package com.example.lembagasosialkematian.ui.peserta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.lembagasosialkematian.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class PesertaTambah extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog loading;
    private EditText etNoKK, etNik, etNama, etTmpLhr,etTglLhr, etAgama, etTerdaftar;
    private Button btSimpan;
    private RadioGroup rgKelamin, rgStatus;
    private RadioButton rbLaki, rbPerempuan, rbKk, rbAnggota;
    private TextView tvDusun;
    Spinner spDusun;
    int dsn;

    String[] dusun = {"Subahnala 1", "Subahnala 2", "Sandik",
            "Dumpu", "Selojan", "Penyengak", "Peresak Daye",
            "Peresak Lauk", "Bujak Daye", "Boak", "Batu Lajan",
            "Aik Gering", "Dasan Aman", "Pajangan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta_tambah);

        etNoKK = findViewById(R.id.in_kk);
        etNik = findViewById(R.id.in_nik);
        etNama = findViewById(R.id.in_nama);
        etTmpLhr = findViewById(R.id.in_tempat);
        etTglLhr = findViewById(R.id.in_tl);
        rbLaki = findViewById(R.id.lk);
        rbPerempuan = findViewById(R.id.pr);
        etAgama = findViewById(R.id.in_agama);
        rgStatus = findViewById(R.id.in_status);
        etTerdaftar = findViewById(R.id.in_terdaftar);
        rgKelamin = findViewById(R.id.in_kelamin);
        btSimpan = findViewById(R.id.btn_simpan_peserta);
        spDusun = findViewById(R.id.sp_Dusun);
        tvDusun = findViewById(R.id.tv_dusun);
        rbKk = findViewById(R.id.rb_kepala);
        rbAnggota = findViewById(R.id.rb_anggota);

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

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(PesertaTambah.this,"Menambahkan...","Tunggu...",false,true);
                String nomerKK = etNoKK.getText().toString();
                String nomerNik = etNik.getText().toString();
                String nama = etNama.getText().toString();
                String tempallhr = etTmpLhr.getText().toString();
                String tl = etTglLhr.getText().toString();
                String laki = rbLaki.getText().toString();
                String per = rbPerempuan.getText().toString();
                String agama = etAgama.getText().toString();
                String kk = rbKk.getText().toString();
                String anggota = rbAnggota.getText().toString();
                String terdaftar = etTerdaftar.getText().toString();
                String Dusun = tvDusun.getText().toString();
                String kelamin;
                String status;

                int pilihKelamin = rgKelamin.getCheckedRadioButtonId();
                int pilihStatus = rgStatus.getCheckedRadioButtonId();

                if (pilihKelamin == rbLaki.getId()){
                    kelamin = laki;
                } else {
                    kelamin = per;
                }

                if(pilihStatus == rbKk.getId()){
                    status = kk;
                } else {
                    status = anggota;
                }

                if(!isEmpty(nomerKK)
                        && !isEmpty(nomerNik)
                        && !isEmpty(nama)
                        && !isEmpty(tempallhr)
                        && !isEmpty(tl)
                        && !isEmpty(kelamin)
                        && !isEmpty(status)
                        && !isEmpty(agama)
                        && !isEmpty(terdaftar)
                        && !isEmpty(Dusun)) {
                    Map<String, Object> peserta = new HashMap<>();
                    peserta.put("KK", nomerKK);
                    peserta.put("NIK", nomerNik);
                    peserta.put("Nama", nama);
                    peserta.put("Tempat", tempallhr);
                    peserta.put("TglLahir", tl);
                    peserta.put("Kelamin", kelamin);
                    peserta.put("Status", status);
                    peserta.put("Agama", agama);
                    peserta.put("Terdaftar", terdaftar);
                    peserta.put("Alamat", Dusun);

                    db.collection("Peserta").document(nomerNik).set(peserta).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loading.dismiss();
                            etNoKK.setText("");
                            etNik.setText("");
                            etNama.setText("");
                            etTmpLhr.setText("");
                            etTglLhr.setText("");
                            etAgama.setText("");
                            etTerdaftar.setText("");
                            Snackbar.make(findViewById(R.id.btn_simpan_peserta), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
                else
                    Snackbar.make(findViewById(R.id.btn_simpan_peserta), "Data peserta tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        etNoKK.getWindowToken(), 0);
            }
        });

    }

    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }

}
