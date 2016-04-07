package com.arappca.library.Classes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.arappca.library.Fonksiyonlar;
import com.arappca.library.GenelDegiskenler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Yusuf on 25.03.2016.
 */
public class ServerdanMesajAl extends AsyncTask<String, Integer, String> {
    String response = "";
    private String gidecekmesaj;
    private Handler responsehandler;
    private Socket ServerBaglantisi;
    private Context context;

    public ServerdanMesajAl(Handler handler, Context context) {
        super();
        responsehandler = handler;
        this.context = context;
        this.ServerBaglantisi = GenelDegiskenler.ServerBaglantisi;


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Message msg = new Message();
        msg.obj = s;

        responsehandler.sendMessage(msg);
    }

    @Override
    protected String doInBackground(String... params) {
        if (!Fonksiyonlar.isInternetAvailable(context)) {
            response = "no internet";
            return response;
        }

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
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();

        } catch (NullPointerException e) {
            e.printStackTrace();
            response = "NullPointerException: " + e.toString();
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
        return response;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


}
