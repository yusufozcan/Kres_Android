package com.arappca.library.Classes.EduClasses;

/**
 * Created by Yusuf on 26.03.2016.
 */
public class Sinif {
    private int id;
    private String isim;
    private Okul okul;

    public Sinif() {
    }

    public Sinif(int id, String isim, Okul okul) {
        this.id = id;
        this.isim = isim;
        this.okul = okul;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public Okul getOkul() {
        return okul;
    }

    public void setOkul(Okul okul) {
        this.okul = okul;
    }
}
