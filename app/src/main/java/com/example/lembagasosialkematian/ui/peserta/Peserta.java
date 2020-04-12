package com.example.lembagasosialkematian.ui.peserta;

public class Peserta {
    private String NIK;
    private String KK;
    private String Nama;
    private String Tempat;
    private String TglLahir;
    private String Kelamin;
    private String Agama;
    private String Status;
    private String Terdaftar;
    private String Alamat;
    private int Photo;

    public Peserta(){}

    public  Peserta (String nik, String kk, String nama, String tempat, String tgl, String kelamin, String agama, String terdaftar, String alamat, String status) {
        this.NIK = nik;
        this.KK = kk;
        this.Nama = nama;
        this.Tempat = tempat;
        this.TglLahir = tgl;
        this.Kelamin = kelamin;
        this.Agama = agama;
        this.Status = status;
        this.Terdaftar = terdaftar;
        this.Alamat = alamat;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public String getKK() {
        return KK;
    }

    public void setKK(String KK) {
        this.KK = KK;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getTempat() {
        return Tempat;
    }

    public void setTempat(String tempat) {
        Tempat = tempat;
    }

    public String getTglLahir() {
        return TglLahir;
    }

    public void setTglLahir(String tglLahir) {
        TglLahir = tglLahir;
    }

    public String getKelamin() {
        return Kelamin;
    }

    public void setKelamin(String kelamin) {
        Kelamin = kelamin;
    }

    public String getAgama() {
        return Agama;
    }

    public void setAgama(String agama) {
        Agama = agama;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTerdaftar() {
        return Terdaftar;
    }

    public void setTerdaftar(String terdaftar) {
        Terdaftar = terdaftar;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public int getPhoto() {
        return Photo;
    }

    public void setPhoto(int photo) {
        Photo = photo;
    }
}
