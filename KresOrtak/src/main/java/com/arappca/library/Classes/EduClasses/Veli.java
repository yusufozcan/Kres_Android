package com.arappca.library.Classes.EduClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yusuf on 26.03.2016.
 */
public class Veli {
    private String id;
    private String mail;
    private String username;
    private String parola;
    private String adsoyad;
    private String adres;
    private String telefon;
    private List<Ogrenci> ogrenciler;

    public Veli() {
        this.ogrenciler = new ArrayList<Ogrenci>();

    }

    public Veli(String id, String mail, String username, String parola, String adsoyad, String adres, String telefon, List<Ogrenci> ogrenciler) {
        this.id = id;
        this.mail = mail;
        this.username = username;
        this.parola = parola;
        this.adsoyad = adsoyad;
        this.adres = adres;
        this.telefon = telefon;
        this.ogrenciler = new ArrayList<Ogrenci>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getAdsoyad() {
        return adsoyad;
    }

    public void setAdsoyad(String adsoyad) {
        this.adsoyad = adsoyad;
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

    public List<Ogrenci> getOgrenciler() {
        return ogrenciler;
    }

    public void setOgrenciler(List<Ogrenci> ogrenciler) {
        this.ogrenciler = ogrenciler;
    }

}
