package com.arappca.library.Classes.EduClasses;

/**
 * Created by Yusuf on 26.03.2016.
 */
public class Okul {
    private String id;
    private String isim;
    private String adres;
    private String telefon;
    private Yonetici yonetici;

    public Okul() {
    }

    public Okul(String id, String isim, String adres, String telefon, Yonetici yonetici) {
        this.id = id;
        this.isim = isim;
        this.adres = adres;
        this.telefon = telefon;
        this.yonetici = yonetici;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Yonetici getYonetici() {
        return yonetici;
    }

    public void setYonetici(Yonetici yonetici) {
        this.yonetici = yonetici;
    }
}
