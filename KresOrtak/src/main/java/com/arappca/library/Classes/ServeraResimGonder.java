package com.arappca.library.Classes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.arappca.library.Fonksiyonlar;
import com.arappca.library.GenelDegiskenler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Yusuf on 5.04.2016.
 */
public class ServeraResimGonder extends AsyncTask<File, Integer, String> {
    String response = "";
    private File gidecekdosya;
    private Handler responsehandler;
    private Socket ServerBaglantisi;
    private int port;
    private Context context;

    public ServeraResimGonder(Handler handler, Context context, int port) {
        super();
        responsehandler = handler;
        this.context = context;
        this.port = port;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Message msg = new Message();
        msg.obj = s;

        responsehandler.sendMessage(msg);
    }

    @Override
    protected String doInBackground(File... params) {
        if (!Fonksiyonlar.isInternetAvailable(context)) {
            response = "no internet";
            return response;
        }
        this.gidecekdosya = params[0];

        try {
            if (GenelDegiskenler.ServerIP.equals("")) {
                try {
                    InetAddress address = InetAddress.getByName("www.arappca.com");
                    GenelDegiskenler.ServerIP = address.getHostAddress();

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
            Log.d("Resim portu", String.valueOf(this.port));
            Log.d("Dosya ismi", gidecekdosya.getName());
            if (ServerBaglantisi == null || ServerBaglantisi.isClosed()) {
                try {
                    ServerBaglantisi = new Socket(GenelDegiskenler.ServerIP, this.port);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            FileInputStream fileInputStream = new FileInputStream(gidecekdosya);
            byte[] fileBuffer = new byte[(int) fileInputStream.getChannel().size()];
            fileInputStream.read(fileBuffer, 0, (int) fileInputStream.getChannel().size());

            PrintStream stream = new PrintStream(ServerBaglantisi.getOutputStream());
            stream.write(fileBuffer, 0, fileBuffer.length);
            stream.flush();

            fileInputStream.close();


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;

            InputStream inputStream = ServerBaglantisi.getInputStream();


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
        Log.d("RESPONSE", response);
        return response;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


}
