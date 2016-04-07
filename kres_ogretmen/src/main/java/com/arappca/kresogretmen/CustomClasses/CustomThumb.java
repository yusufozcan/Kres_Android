package com.arappca.kresogretmen.CustomClasses;

import com.arappca.kresogretmen.Activities.MesajGonder;

/**
 * Created by Yusuf on 5.04.2016.
 */
public class CustomThumb {
    private String Resimyeri;
    private boolean Secili;

    public CustomThumb(String resimyeri, boolean secili) {
        Resimyeri = resimyeri;
        Secili = secili;

    }

    public String getResimyeri() {
        return Resimyeri;
    }

    public void setResimyeri(String resimyeri) {
        Resimyeri = resimyeri;
    }

    public boolean isSecili() {
        return Secili;
    }

    public void setSecili(boolean secili) {
        Secili = secili;
        if (!secili) {
            MesajGonder.GonderilecekResimler.remove(getResimyeri());
        } else {
            if (!MesajGonder.GonderilecekResimler.contains(getResimyeri())) {
                MesajGonder.GonderilecekResimler.add(getResimyeri());
            }
        }

    }
}