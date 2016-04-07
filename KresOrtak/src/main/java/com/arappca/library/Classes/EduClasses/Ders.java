package com.arappca.library.Classes.EduClasses;

import android.graphics.Bitmap;

/**
 * Created by Yusuf on 2.04.2016.
 */
public class Ders {
    private String id;
    private String dersadi;
    private String resimadi;
    private boolean isOlumlu;
    private Bitmap resim;
    private Integer Puan;

    public Ders() {

    }

    public Ders(String id, String dersadi, String resimadi, boolean isOlumlu) {
        this.id = id;
        this.dersadi = dersadi;
        this.resimadi = resimadi;
        this.isOlumlu = isOlumlu;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDersadi() {
        return dersadi;
    }

    public void setDersadi(String dersadi) {
        this.dersadi = dersadi;
    }

    public String getResimadi() {
        return resimadi;
    }

    public void setResimadi(String resimadi) {
        this.resimadi = resimadi;
    }

    public boolean isOlumlu() {
        return isOlumlu;
    }

    public void setIsOlumlu(boolean isOlumlu) {
        this.isOlumlu = isOlumlu;
    }

    public Bitmap getResim() {
        return resim;
    }

    public void setResim(Bitmap resim) {
        this.resim = resim;
    }

    public Integer getPuan() {
        return Puan;
    }

    public void setPuan(Integer puan) {
        Puan = puan;
    }
}
