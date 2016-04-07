package com.arappca.library.Classes.EduClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusuf on 1.04.2016.
 */
public class Ogretmen {
    private String id;
    private String adsoyad;
    private String mail;
    private String username;
    private List<Sinif> siniflar;

    public Ogretmen() {
        this.siniflar = new ArrayList<Sinif>();
    }

    public Ogretmen(String id, String adsoyad, String mail, String username, List<Sinif> siniflar) {
        this.id = id;
        this.adsoyad = adsoyad;
        this.mail = mail;
        this.username = username;
        this.siniflar = siniflar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdsoyad() {
        return adsoyad;
    }

    public void setAdsoyad(String adsoyad) {
        this.adsoyad = adsoyad;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Sinif> getSiniflar() {
        return siniflar;
    }

    public void setSiniflar(List<Sinif> siniflar) {
        this.siniflar = siniflar;
    }
}
