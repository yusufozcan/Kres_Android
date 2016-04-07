package com.arappca.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;

/**
 * Created by Yusuf on 27.03.2016.
 */
public class Fonksiyonlar {

    //MESAJ GÖNDER AL


    //İNTERNET BAĞLANTISINI TEST ET
    public static boolean isInternetAvailable(Context context) {

        try {
            InetAddress ipAddr = InetAddress.getByName("www.arappca.com");

            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isInternetAvailable(String msg, Context con) {
        if (msg.equals("no internet")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
            alertDialogBuilder.setMessage("Mevcut bir internet bağlantısı yok");

            alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return false;
        }
        return true;
    }

    public static void mesajGoster(Context con, String mesaj) {
        final Context context = con;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(mesaj);

        alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void resimKaydet(Bitmap resim, File kayityeri, String resimadi) {

        File dest = new File(kayityeri, resimadi);


        try {
            FileOutputStream out = new FileOutputStream(dest);
            resim.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap hafizadanResimYukle(String resimyeri, int SampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = SampleSize;
        final Bitmap b = BitmapFactory.decodeFile(resimyeri, options);
        return b;
    }
}
