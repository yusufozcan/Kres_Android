package com.arappca.library;


import android.app.ProgressDialog;

import java.net.Socket;

/**
 * Created by Yusuf on 25.03.2016.
 */
public class GenelDegiskenler {
    public static String ServerIP = "192.168.8.103";
    public static int ServerPort = 1646;
    public static Socket ServerBaglantisi = null;
    public static ProgressDialog progressDialog;

    //public static Ogretmen aktifOgretmen=new Ogretmen();
    public enum Responses {
        GİRİŞ_BAŞARILI,
        HATALI_LOGİN,
        ÖĞRENCİ_LİSTESİ,
        DERS_LİSTESİ,
        NOT_VERİLDİ,
        ÖĞRENCİ_NOTLARI,
        MESAJ_ALINDI,
        RESİMLERİ_GÖNDER,
        RESİM_BEKLENİYOR,
        RESİM_ALINDI,
    }

    public enum Extras {
        SinifID,
        OgrenciTCno
    }

    public enum Istekler {
        VELİ_GİRİŞİ,
        ÖĞRETMEN_GİRİŞİ,
        SINIFTAKİ_ÖĞRENCİLERİ_İSTE,
        ÖĞRETMEN_DERS_LİSTESİ_İSTE,
        RESİM_İSTE,
        NOT_VER,
        ÖĞRENCİ_NOTLARINI_İSTE,
        ÖĞRETMEN_MESAJ_GÖNDER,
        RESİM_İÇİN_HAZIRLIK_YAP,
    }
}
