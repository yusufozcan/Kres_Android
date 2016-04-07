package com.arappca.library.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;

import com.arappca.library.GenelDegiskenler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.arappca.library.Fonksiyonlar.resimKaydet;

/**
 * Created by Yusuf on 25.03.2016.
 */
public class ServerdanResimAl extends AsyncTask<String, Integer, Bitmap> {
    Bitmap resim;
    private String gidecekmesaj;
    private Handler responsehandler;
    private Socket ServerBaglantisi;
    private Context context;
    private String resimismi;

    public ServerdanResimAl(Handler handler, Context context, String resimismi) {
        super();
        responsehandler = handler;
        this.context = context;
        this.ServerBaglantisi = GenelDegiskenler.ServerBaglantisi;
        this.resimismi = resimismi;


    }

    @Override
    protected void onPostExecute(Bitmap s) {
        super.onPostExecute(s);
        // Message msg = new Message();

        resimKaydet(resim, this.context.getFilesDir(), this.resimismi);

        ResimResponse resimResponse = new ResimResponse(s, resimismi);

        //msg.obj = resimResponse;

        //responsehandler.sendMessage(msg);
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        gidecekmesaj = params[0];

        try {

            if (GenelDegiskenler.ServerIP.equals("")) {
                try {
                    InetAddress address = InetAddress.getByName("www.arappca.com");
                    GenelDegiskenler.ServerIP = address.getHostAddress();

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
            if (ServerBaglantisi == null || ServerBaglantisi.isClosed()) {
                try {
                    ServerBaglantisi = new Socket(GenelDegiskenler.ServerIP, GenelDegiskenler.ServerPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            PrintStream stream = new PrintStream(ServerBaglantisi.getOutputStream());
            stream.println(gidecekmesaj);
            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = ServerBaglantisi.getInputStream();

    /*
     * notice:
     * inputStream.read() will block if no data return
     */
          /*  while ((bytesRead = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, bytesRead);

            }*/
            resim = BitmapFactory.decodeStream(inputStream);

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();


        } catch (NullPointerException e) {
            e.printStackTrace();

        } finally {
           /* if(GenelDegiskenler.ServerBaglantisi != null){
                try {
                    GenelDegiskenler.ServerBaglantisi.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*/
        }
        return resim;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


}
