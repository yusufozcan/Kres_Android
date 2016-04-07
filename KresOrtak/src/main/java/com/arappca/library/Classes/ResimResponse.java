package com.arappca.library.Classes;

import android.graphics.Bitmap;

/**
 * Created by Yusuf on 2.04.2016.
 */
public class ResimResponse {
    private Bitmap resim;
    private String resimadi;

    public ResimResponse(Bitmap resim, String resimadi) {
        this.resim = resim;
        this.resimadi = resimadi;
    }

    public Bitmap getResim() {
        return resim;
    }

    public void setResim(Bitmap resim) {
        this.resim = resim;
    }

    public String getResimadi() {
        return resimadi;
    }

    public void setResimadi(String resimadi) {
        this.resimadi = resimadi;
    }
}
