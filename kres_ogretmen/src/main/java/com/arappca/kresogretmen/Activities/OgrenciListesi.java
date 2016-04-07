package com.arappca.kresogretmen.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.arappca.kresogretmen.Genel.OgretmenDegiskenler;
import com.arappca.kresogretmen.ListAdapters.OgrenciListAdapter;
import com.arappca.kresogretmen.R;
import com.arappca.library.Classes.EduClasses.Ogrenci;
import com.arappca.library.Classes.EduClasses.Sinif;
import com.arappca.library.Classes.ServerdanMesajAl;
import com.arappca.library.Fonksiyonlar;
import com.arappca.library.GenelDegiskenler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.arappca.library.Fonksiyonlar.isInternetAvailable;

public class OgrenciListesi extends AppCompatActivity {
    public Sinif aktifsinif;
    ListView lstOgrenciler;
    Activity myActivity;
    List<Ogrenci> ogrenciList = new ArrayList<Ogrenci>();
    ProgressDialog progressDialog;
    ImageButton btnMesajGonder;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("Sonuç", msg.obj.toString());
            if (!isInternetAvailable(msg.obj.toString(), OgrenciListesi.this)) {
                progressDialog.dismiss();
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
                Fonksiyonlar.mesajGoster(OgrenciListesi.this, "Sunucuya bağlanırken bir hata oluştu");
                progressDialog.dismiss();
                return;
            }
            GenelDegiskenler.Responses responses = GenelDegiskenler.Responses.values()[cevap];

            switch (responses) {
                case ÖĞRENCİ_LİSTESİ:
                    ogrenciList.clear();
                    try {
                        JSONArray ogrenciArray = new JSONArray(inputJSONString);
                        for (int i = 0; i < ogrenciArray.length(); i++) {
                            Ogrenci ogrenci = new Ogrenci();
                            JSONObject jsonogrenci = ogrenciArray.getJSONObject(i);
                            ogrenci.setId(jsonogrenci.getString("id"));
                            ogrenci.setTcno(jsonogrenci.getString("tcno"));
                            ogrenci.setAdsoyad(jsonogrenci.getString("adsoyad"));
                            ogrenci.setAnneadi(jsonogrenci.getString("anneadi"));
                            ogrenci.setBabaadi(jsonogrenci.getString("babaadi"));
                            ogrenci.setEkbilgi(jsonogrenci.getString("ekbilgi"));
                            ogrenci.setSonpuan(jsonogrenci.getInt("sonpuan"));

                            ogrenciList.add(ogrenci);

                        }
                        OgrenciListAdapter adpOgrenciler = new OgrenciListAdapter(myActivity, ogrenciList);
                        lstOgrenciler.setAdapter(adpOgrenciler);
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(OgrenciListesi.this, "", "Öğrenci listesi alınıyor...");
        SiniftakiOgrencileriServerdanAl(aktifsinif.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_listesi);

        //Hangi sınıfın aktif olduğunu intent parametrelerinden alıyorum. (index olarak)
        Intent intent = getIntent();
        int aktifsinifno = intent.getIntExtra(GenelDegiskenler.Extras.SinifID.toString(), 0);
        aktifsinif = OgretmenDegiskenler.aktifogretmen.getSiniflar().get(aktifsinifno);
        myActivity = this;
        //region Kontrolleri Ata
        lstOgrenciler = (ListView) findViewById(R.id.lstOgrenciler);
        btnMesajGonder = (ImageButton) findViewById(R.id.btnMesajGonder);


        //endregion

        //region Delegate Fonksiyonlar
        lstOgrenciler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ogrenci secilenogrenci = ogrenciList.get(position);

                Intent intent = new Intent(OgrenciListesi.this, NotVer.class);
                NotVer.setSeciliOgrenci(secilenogrenci);
                NotVer.setSeciliSinif(aktifsinif);
                startActivity(intent);
            }
        });

        btnMesajGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MesajGonder.sinifmesaji = true;
                MesajGonder.sinif = aktifsinif;
                startActivity(new Intent(OgrenciListesi.this, MesajGonder.class));

            }
        });
        //endregion

        //TODO: TÜM SINIFA NOT VERME İŞLEMLERİ

        if (GenelDegiskenler.progressDialog.isShowing()) {
            GenelDegiskenler.progressDialog.dismiss();
        }
    }

    private void SiniftakiOgrencileriServerdanAl(int ıd) {
        String mesaj = "command=" + GenelDegiskenler.Istekler.SINIFTAKİ_ÖĞRENCİLERİ_İSTE.toString() + ":" + aktifsinif.getId() + ";";
        new ServerdanMesajAl(myHandler, OgrenciListesi.this).execute(mesaj);
    }
}
