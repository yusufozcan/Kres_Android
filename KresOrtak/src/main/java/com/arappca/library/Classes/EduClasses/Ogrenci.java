package com.arappca.library.Classes.EduClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusuf on 26.03.2016.
 */

public class Ogrenci {
    private String id;
    private String tcno;
    private String adsoyad;
    private String anneadi;
    private String babaadi;
    private String ekbilgi;
    private int sonpuan;
    private List<Sinif> siniflar;

    public Ogrenci() {
        this.siniflar = new ArrayList<Sinif>();

    }

    public Ogrenci(String id, String tcno, String adsoyad, String anneadi, String babaadi, String ekbilgi, int sonpuan) {
        this.id = id;
        this.tcno = tcno;
        this.adsoyad = adsoyad;
        this.anneadi = anneadi;
        this.babaadi = babaadi;
        this.ekbilgi = ekbilgi;
        this.sonpuan = sonpuan;
        this.siniflar = new ArrayList<Sinif>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTcno() {
        return tcno;
    }

    public void setTcno(String tcno) {
        this.tcno = tcno;
    }

    public String getAdsoyad() {
        return adsoyad;
    }

    public void setAdsoyad(String adsoyad) {
        this.adsoyad = adsoyad;
    }

    public String getAnneadi() {
        return anneadi;
    }

    public void setAnneadi(String anneadi) {
        this.anneadi = anneadi;
    }

    public String getBabaadi() {
        return babaadi;
    }

    public void setBabaadi(String babaadi) {
        this.babaadi = babaadi;
    }

    public String getEkbilgi() {
        return ekbilgi;
    }

    public void setEkbilgi(String ekbilgi) {
        this.ekbilgi = ekbilgi;
    }

    public int getSonpuan() {
        return sonpuan;
    }

    public void setSonpuan(int sonpuan) {
        this.sonpuan = sonpuan;
    }

    public List<Sinif> getSiniflar() {
        return siniflar;
    }

    public void setSiniflar(List<Sinif> siniflar) {
        this.siniflar = siniflar;
    }
}
