package com.arappca.kresogretmen.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arappca.kresogretmen.CustomClasses.CustomThumb;
import com.arappca.kresogretmen.Genel.OgretmenDegiskenler;
import com.arappca.kresogretmen.ListAdapters.ThumbAdapter;
import com.arappca.kresogretmen.R;
import com.arappca.library.Classes.EduClasses.Ogrenci;
import com.arappca.library.Classes.EduClasses.Sinif;
import com.arappca.library.Classes.ServeraResimGonder;
import com.arappca.library.Classes.ServerdanMesajAl;
import com.arappca.library.Fonksiyonlar;
import com.arappca.library.GenelDegiskenler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.arappca.library.Fonksiyonlar.isInternetAvailable;


public class MesajGonder extends AppCompatActivity {
    public static boolean sinifmesaji;
    public static boolean tumsiniflara;
    public static Sinif sinif;
    public static Ogrenci ogrenci;
    public static List<File> cekilenresimler = new ArrayList<File>();
    public static List<String> GonderilecekResimler = new ArrayList<String>();
    public static List<File> GonderilecekDosyalar = new ArrayList<File>();
    ArrayList<CustomThumb> resimyerleri = new ArrayList<CustomThumb>();
    Context context;
    TextView lblMesajHedefi;
    EditText txtMesaj;
    ImageView imgResimEkle;
    ImageView imgGonder;
    //   HorizontalScrollView svResimkaynagi;
    LinearLayout lnResimkaynagi;
    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (!isInternetAvailable(msg.obj.toString(), MesajGonder.this)) {
                GenelDegiskenler.progressDialog.dismiss();
                return;
            }

            int cevap = -1;
            String inputJSONString = ""; // Your string JSON here

            Pattern reg = Pattern.compile("Sonuç=([0-9]*):(.*)");
            Matcher m = reg.matcher(msg.obj.toString());
            while (m.find()) {
                cevap = Integer.parseInt(m.group(1));
                inputJSONString = m.group(2);
            }
            if (cevap == -1) {
                Fonksiyonlar.mesajGoster(MesajGonder.this, "Sunucuya bağlanırken bir hata oluştu");
                GenelDegiskenler.progressDialog.dismiss();
                return;
            }
            GenelDegiskenler.Responses responses = GenelDegiskenler.Responses.values()[cevap];
            switch (responses) {
                case MESAJ_ALINDI:
                    GenelDegiskenler.progressDialog.dismiss();
                    Log.d("DURUM", "Mesaj Gönderildi");
                    break;

                case RESİMLERİ_GÖNDER:
                    String mesajid = inputJSONString;
                    ResimleriGonder(mesajid, GonderilecekResimler);
                    break;
                case RESİM_BEKLENİYOR:
                    Log.d("RESİM BEKLENİYOR", inputJSONString);

                    String[] pars = inputJSONString.split(":");
                    int port = Integer.valueOf(pars[0]);
                    String dosyaadi = pars[1];
                    for (int i = 0; i < GonderilecekDosyalar.size(); i++) {
                        if (GonderilecekDosyalar.get(i).getName().equals(dosyaadi)) {
                            Log.d("Dosya gönder", GonderilecekDosyalar.get(i).getName());
                            Log.d("Port", String.valueOf(port));

                            new ServeraResimGonder(myHandler, context, port).execute(GonderilecekDosyalar.get(i));

                        }

                    }
                    break;
                case RESİM_ALINDI:
                    Log.d("DOSYA ALINDI", inputJSONString);
                    break;
            }
        }
    };
    private Activity myActivity;
    private View lytYatay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj_gonder);
        context = MesajGonder.this;
        myActivity = this;
        //region KONTROLLERİ ATA
        lblMesajHedefi = (TextView) findViewById(R.id.lblMesajHedefi);
        txtMesaj = (EditText) findViewById(R.id.txtMesaj);
        imgResimEkle = (ImageView) findViewById(R.id.imgResimSec);
        imgGonder = (ImageView) findViewById(R.id.imgGonder);
        // svResimkaynagi = (HorizontalScrollView) findViewById(R.id.svEklenenResimler);
        lnResimkaynagi = (LinearLayout) findViewById(R.id.lnEklenenResimler);
        //endregion


        LayoutInflater layoutInflater = getLayoutInflater();
        lytYatay = layoutInflater.inflate(R.layout.resimler_fragment_layout, null);
        ImageButton btnFoto = (ImageButton) lytYatay.findViewById(R.id.btnFoto);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FotoCek.cekilenresimler = cekilenresimler;
                startActivity(new Intent(MesajGonder.this, FotoCek.class));
            }
        });

        //region DELEGATES
        // LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        // View view = inflater.inflate(R.layout.fragment_native_camera, container, false);
        imgResimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lnResimkaynagi.getChildCount() > 0) {
                    lnResimkaynagi.removeAllViews();
                } else {

                    lnResimkaynagi.removeAllViews();
                    lnResimkaynagi.addView(lytYatay);
                    resimyerleri.clear();
                    RecyclerView rvImages = (RecyclerView) lytYatay.findViewById(R.id.rvImages);
                    if (cekilenresimler.size() > 0) {
                        for (int i = 0; i < cekilenresimler.size(); i++) {
                            resimyerleri.add(new CustomThumb(cekilenresimler.get(i).getAbsolutePath(), true));
                            GonderilecekResimler.add(cekilenresimler.get(i).getAbsolutePath());
                        }
                    }
                    final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
                    final String orderBy = MediaStore.Images.Media._ID;

                    Cursor imagecursor = managedQuery(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                            null, orderBy);
                    int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);


                    for (int i = 0; i < imagecursor.getCount(); i++) {
                        imagecursor.moveToPosition(i);
                        int id = imagecursor.getInt(image_column_index);
                        int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);

                        resimyerleri.add(new CustomThumb(imagecursor.getString(dataColumnIndex), GonderilecekResimler.contains(imagecursor.getString(dataColumnIndex))));
                    }
                    ThumbAdapter thumbAdapter = new ThumbAdapter(resimyerleri);
                    rvImages.setAdapter(thumbAdapter);
                    rvImages.setLayoutManager(new LinearLayoutManager(MesajGonder.this, LinearLayoutManager.HORIZONTAL, false));

                }


            }
        });

        imgGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenelDegiskenler.progressDialog = ProgressDialog.show(MesajGonder.this, "", "Mesaj gönderiliyor...");
                String mesaj = "command=" + GenelDegiskenler.Istekler.ÖĞRETMEN_MESAJ_GÖNDER.toString() + ":" +
                        OgretmenDegiskenler.aktifogretmen.getId() + ";:" +
                        String.valueOf(tumsiniflara) + ";:" +
                        String.valueOf(sinifmesaji) + ";:" +
                        ((sinifmesaji) ? sinif.getId() : ogrenci.getId()) + ";:" +
                        txtMesaj.getText().toString() + ";:" +
                        GonderilecekResimler.size() + ";";
                new ServerdanMesajAl(myHandler, MesajGonder.this).execute(mesaj);
            }
        });
        //endregion


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sinifmesaji) {
            lblMesajHedefi.setText(sinif.getIsim() + " sınıfı öğrencilerinin velilerine mesaj gönderin");
        } else {
            lblMesajHedefi.setText(ogrenci.getAdsoyad() + " isimli öğrencinin velisine mesaj gönderin");
        }
        RecyclerView rvImages = (RecyclerView) lytYatay.findViewById(R.id.rvImages);
        resimyerleri.clear();
        if (cekilenresimler.size() > 0) {

            for (int i = 0; i < cekilenresimler.size(); i++) {
                resimyerleri.add(new CustomThumb(cekilenresimler.get(i).getAbsolutePath(), true));
                GonderilecekResimler.add(cekilenresimler.get(i).getAbsolutePath());
            }
        }
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;

        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);


        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            resimyerleri.add(new CustomThumb(imagecursor.getString(dataColumnIndex), GonderilecekResimler.contains(imagecursor.getString(dataColumnIndex))));
        }

        ThumbAdapter thumbAdapter = new ThumbAdapter(resimyerleri);
        rvImages.setAdapter(thumbAdapter);
        rvImages.setLayoutManager(new LinearLayoutManager(MesajGonder.this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void ResimleriGonder(String mesajid, List<String> gonderilecekResimler) {
        GonderilecekDosyalar.clear();
        for (int i = 0; i < gonderilecekResimler.size(); i++) {

            File from = new File(gonderilecekResimler.get(i));
            String mesaj = "command=" + GenelDegiskenler.Istekler.RESİM_İÇİN_HAZIRLIK_YAP.toString() + ":" +
                    from.getName() + ";:" +
                    from.length() + ";:" +
                    mesajid + ";";
            GonderilecekDosyalar.add(from);
            new ServerdanMesajAl(myHandler, context).execute(mesaj);

        }
        GenelDegiskenler.progressDialog.dismiss();

    }


}

