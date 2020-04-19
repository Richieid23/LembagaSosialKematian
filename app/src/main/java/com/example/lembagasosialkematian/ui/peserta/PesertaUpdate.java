package com.example.lembagasosialkematian.ui.peserta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.lembagasosialkematian.R;
import com.example.lembagasosialkematian.ui.berhenti.TabMeninggal;
import com.example.lembagasosialkematian.ui.berhenti.TabPindah;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PesertaUpdate extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog loading;
    private EditText etNoKK, etNik, etNama, etTmpLhr,etTglLhr, etAgama, etTerdaftar;
    private Button btUpdate;
    private RadioGroup rgKelamin, rgStatus, rgBerhenti;
    private RadioButton rbLaki, rbPerempuan, rbKk, rbAnggota, rbMeninggal, rbPindah, rbUndurdiri;
    private TextView tvDusun;
    Spinner spDusun;
    String nik = "";
    String status;


    String[] dusun = {"Subahnala 1", "Subahnala 2", "Sandik",
            "Dumpu", "Selojan", "Penyengak", "Peresak Daye",
            "Peresak Lauk", "Bujak Daye", "Boak", "Batu Lajan",
            "Aik Gering", "Dasan Aman", "Pajangan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta_update);

        etNoKK = findViewById(R.id.ed_kk);
        etNik = findViewById(R.id.ed_nik);
        etNama = findViewById(R.id.ed_nama);
        etTmpLhr = findViewById(R.id.ed_tempat);
        etTglLhr = findViewById(R.id.ed_tl);
        rbLaki = findViewById(R.id.ed_lk);
        rbPerempuan = findViewById(R.id.ed_pr);
        etAgama = findViewById(R.id.ed_agama);
        rgStatus = findViewById(R.id.ed_status);
        etTerdaftar = findViewById(R.id.ed_terdaftar);
        rgKelamin = findViewById(R.id.ed_kelamin);
        btUpdate = findViewById(R.id.btn_update_peserta);
        spDusun = findViewById(R.id.sp_edDusun);
        tvDusun = findViewById(R.id.ed_dusun);
        rbKk = findViewById(R.id.rb_edkepala);
        rbAnggota = findViewById(R.id.rb_edanggota);
        rbMeninggal = findViewById(R.id.rb_meninggal);
        rbPindah = findViewById(R.id.rb_pindah);
        rbUndurdiri = findViewById(R.id.rb_undurdiri);
        rgBerhenti = findViewById(R.id.ed_berhenti);

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nik = bundle.getString("NIK");
            etNik.setText(bundle.getString("NIK"));
            etNoKK.setText(bundle.getString("KK"));
            etNama.setText(bundle.getString("Nama"));
            etTmpLhr.setText(bundle.getString("Tempat"));
            etTglLhr.setText(bundle.getString("Lahir"));
            etAgama.setText(bundle.getString("Agama"));
            etTerdaftar.setText(bundle.getString("Terdaftar"));
            tvDusun.setText(bundle.getString("Alamat"));
        }



        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pilihberhenti = rgBerhenti.getCheckedRadioButtonId();
                if (pilihberhenti == rbMeninggal.getId()){
                    setMeninggal();
                }
                else if(pilihberhenti == rbPindah.getId()){
                    setPindah();
                }
                else if (pilihberhenti == rbUndurdiri.getId()){
                    setUndurdiri();
                }
                else {
                    updatePeserta();
                }
            }
        });
    }


    public void updatePeserta(){
        loading = ProgressDialog.show(PesertaUpdate.this,"Memperbarui...","Tunggu...",false,false);
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

        int pilihKelamin = rgKelamin.getCheckedRadioButtonId();

        if (pilihKelamin == rbLaki.getId()){
            kelamin = laki;
        } else {
            kelamin = per;
        }

        int pilihstatus = rgStatus.getCheckedRadioButtonId();
        if (pilihstatus == rbAnggota.getId()){
            status = anggota;
        } else {
            status = kk;
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
            final Map<String, Object> peserta = new HashMap<>();
            peserta.put("KK", nomerKK);
            peserta.put("NIK", nomerNik);
            peserta.put("Search", nomerNik);
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
                    Snackbar.make(findViewById(R.id.btn_update_peserta), "Data berhasil diupdate", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(PesertaUpdate.this, PesertaDetail.class);
                    intent.putExtra("NIK", Objects.requireNonNull(peserta.get("NIK")).toString());
                    startActivity(intent);
                }
            });
//
        }
        else {
            Snackbar.make(findViewById(R.id.btn_update_peserta), "Data peserta tidak boleh kosong", Snackbar.LENGTH_LONG).show();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etNoKK.getWindowToken(), 0);
        }
    }

    public void setMeninggal(){
        loading = ProgressDialog.show(PesertaUpdate.this,"Mengubah Status...","Tunggu...",false,true);
        String nomerKK = etNoKK.getText().toString();
        String nomerNik = etNik.getText().toString();
        String nama = etNama.getText().toString();
        String tempallhr = etTmpLhr.getText().toString();
        String tl = etTglLhr.getText().toString();
        String laki = rbLaki.getText().toString();
        String per = rbPerempuan.getText().toString();
        String agama = etAgama.getText().toString();
        String terdaftar = etTerdaftar.getText().toString();
        String Dusun = tvDusun.getText().toString();
        String kelamin;
        status = "Meninggal";

        int pilihKelamin = rgKelamin.getCheckedRadioButtonId();

        if (pilihKelamin == rbLaki.getId()){
            kelamin = laki;
        } else {
            kelamin = per;
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
            final Map<String, Object> peserta = new HashMap<>();
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
            db.collection("Meninggal").document(nomerNik).set(peserta).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    db.collection("Peserta")
                            .document(nik)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PesertaUpdate.this, "Status berhasil diubah", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Intent inten = new Intent(PesertaUpdate.this, PesertaDetail.class);
                    startActivity(inten);
                }
            });
        }
        else {
            Snackbar.make(findViewById(R.id.btn_update_peserta), "Data peserta tidak boleh kosong", Snackbar.LENGTH_LONG).show();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etNoKK.getWindowToken(), 0);
        }
    }

    public void setPindah(){
        loading = ProgressDialog.show(PesertaUpdate.this,"Mengubah Status...","Tunggu...",false,true);
        String nomerKK = etNoKK.getText().toString();
        String nomerNik = etNik.getText().toString();
        String nama = etNama.getText().toString();
        String tempallhr = etTmpLhr.getText().toString();
        String tl = etTglLhr.getText().toString();
        String laki = rbLaki.getText().toString();
        String per = rbPerempuan.getText().toString();
        String agama = etAgama.getText().toString();
        String terdaftar = etTerdaftar.getText().toString();
        String Dusun = tvDusun.getText().toString();
        String kelamin;
        status = "Pindah";

        int pilihKelamin = rgKelamin.getCheckedRadioButtonId();

        if (pilihKelamin == rbLaki.getId()){
            kelamin = laki;
        } else {
            kelamin = per;
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
            final Map<String, Object> peserta = new HashMap<>();
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
            db.collection("Pindah").document(nomerNik).set(peserta).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    db.collection("Peserta")
                            .document(nik)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PesertaUpdate.this, "Status berhasil diubah", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Intent inten = new Intent(PesertaUpdate.this, PesertaDetail.class);
                    startActivity(inten);
                }
            });
        }
        else {
            Snackbar.make(findViewById(R.id.btn_update_peserta), "Data peserta tidak boleh kosong", Snackbar.LENGTH_LONG).show();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etNoKK.getWindowToken(), 0);
        }
    }

    private void setUndurdiri() {
        loading = ProgressDialog.show(PesertaUpdate.this,"Mengubah Status...","Tunggu...",false,true);
        String nomerKK = etNoKK.getText().toString();
        String nomerNik = etNik.getText().toString();
        String nama = etNama.getText().toString();
        String tempallhr = etTmpLhr.getText().toString();
        String tl = etTglLhr.getText().toString();
        String laki = rbLaki.getText().toString();
        String per = rbPerempuan.getText().toString();
        String agama = etAgama.getText().toString();
        String terdaftar = etTerdaftar.getText().toString();
        String Dusun = tvDusun.getText().toString();
        String kelamin;
        status = "Undur Diri";

        int pilihKelamin = rgKelamin.getCheckedRadioButtonId();

        if (pilihKelamin == rbLaki.getId()){
            kelamin = laki;
        } else {
            kelamin = per;
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
            final Map<String, Object> peserta = new HashMap<>();
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
            db.collection("Undurdiri").document(nomerNik).set(peserta).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    db.collection("Peserta")
                            .document(nik)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PesertaUpdate.this, "Status berhasil diubah", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Intent inten = new Intent(PesertaUpdate.this, PesertaDetail.class);
                    startActivity(inten);
                }
            });
        }
        else {
            Snackbar.make(findViewById(R.id.btn_update_peserta), "Data peserta tidak boleh kosong", Snackbar.LENGTH_LONG).show();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etNoKK.getWindowToken(), 0);
        }
    }

    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }
}
