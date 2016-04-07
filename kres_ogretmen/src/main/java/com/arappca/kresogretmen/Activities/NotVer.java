package com.arappca.kresogretmen.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.arappca.kresogretmen.Genel.OgretmenDegiskenler;
import com.arappca.kresogretmen.ListAdapters.DersListAdapter;
import com.arappca.kresogretmen.R;
import com.arappca.library.Classes.EduClasses.Ders;
import com.arappca.library.Classes.EduClasses.Ogrenci;
import com.arappca.library.Classes.EduClasses.Sinif;
import com.arappca.library.Classes.ServerdanMesajAl;
import com.arappca.library.Classes.ServerdanResimAl;
import com.arappca.library.Fonksiyonlar;
import com.arappca.library.GenelDegiskenler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.arappca.library.Fonksiyonlar.isInternetAvailable;

public class NotVer extends AppCompatActivity {
    static Ogrenci seciliOgrenci;
    static Sinif seciliSinif;
    static boolean sinifcanotver;
    Button btnOlumlu;
    Button btnOlumsuz;
    GridView grdDersler;
    DersListAdapter adpOlumludersler;
    DersListAdapter adpOlumsuzdersler;
    ProgressDialog progressDialog;
    private Activity myActivity;
    private List<Ders> olumluDersler = new ArrayList<Ders>();
    private List<Ders> olumsuzDersler = new ArrayList<Ders>();
    Handler resimHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            adpOlumludersler = new DersListAdapter(myActivity, olumluDersler);
            adpOlumsuzdersler = new DersListAdapter(myActivity, olumsuzDersler);
            grdDersler.setAdapter(adpOlumludersler);
            progressDialog.dismiss();

            //OgrenciNotlariniAl();


          /*  ResimResponse resimResponse = (ResimResponse) msg.obj;
            Bitmap resim =resimResponse.getResim();
            String resimadi = resimResponse.getResimadi();
            resimKaydet(resim,getFilesDir(),resimadi);
            for (int i=0;i<dersler.size();i++){
                if ( dersler.get(i).getResimadi().equals(resimadi)){
                    dersler.get(i).setResim(resim);
                }
            }*/

        }
    };
    private List<Ders> dersler = new ArrayList<Ders>();
    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.d("Message", msg.obj.toString());
            if (!isInternetAvailable(msg.obj.toString(), NotVer.this)) {
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
                Fonksiyonlar.mesajGoster(NotVer.this, "Sunucuya bağlanırken bir hata oluştu");
                progressDialog.dismiss();
                return;
            }
            GenelDegiskenler.Responses responses = GenelDegiskenler.Responses.values()[cevap];
            switch (responses) {
                case DERS_LİSTESİ:
                    //region DERS LİSTESİ İŞLEMLERİ
                    try {
                        JSONArray derslerJsonArray = new JSONArray(inputJSONString);
                        dersler.clear();
                        olumsuzDersler.clear();
                        olumsuzDersler.clear();

                        for (int i = 0; i < derslerJsonArray.length(); i++) {
                            Ders ders = new Ders();

                            ders.setId(derslerJsonArray.getJSONObject(i).getString("id"));
                            ders.setDersadi(derslerJsonArray.getJSONObject(i).getString("dersadi"));
                            ders.setResimadi(derslerJsonArray.getJSONObject(i).getString("resimadi"));
                            ders.setPuan(derslerJsonArray.getJSONObject(i).getInt("puan"));

                            if (derslerJsonArray.getJSONObject(i).getString("olumlu").equals("e")) {
                                ders.setIsOlumlu(true);
                                olumluDersler.add(ders);
                            } else {
                                ders.setIsOlumlu(false);
                                olumsuzDersler.add(ders);

                            }

                            dersler.add(ders);
                        }

                        boolean eksikresimvar = false;
                        //RESİM KONTROL
                        for (int i = 0; i < dersler.size(); i++) {
                            File file = new File(getFilesDir(), dersler.get(i).getResimadi());
                            Log.d("Dosya durumu", String.valueOf(file.exists()));

                            if (!file.exists()) {
                                eksikresimvar = true;
                                Resimiste(dersler.get(i).getResimadi());
                            }
                        }

                        DersListeleriniDoldur(eksikresimvar);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //endregion
                case NOT_VERİLDİ:
                    //region NOT VERİLDİ İŞLEMLERİ
                    String dersID = ""; // Your string JSON here
                    Ders notlananders = null;
                    m = reg.matcher(msg.obj.toString());
                    while (m.find()) {
                        dersID = m.group(2);
                    }

                    for (int i = 0; i < dersler.size(); i++) {
                        if (dersler.get(i).getId().equals(dersID)) {
                            notlananders = dersler.get(i);
                        }
                    }
                    notlananders.setPuan(notlananders.getPuan() + 1);
                    if (grdDersler.getAdapter() == adpOlumludersler) {
                        grdDersler.setAdapter(null);
                        grdDersler.setAdapter(adpOlumludersler);
                    } else {
                        grdDersler.setAdapter(null);
                        grdDersler.setAdapter(adpOlumsuzdersler);
                    }

                    Toast.makeText(NotVer.this, "Derecelendirmeniz kaydedildi", Toast.LENGTH_SHORT).show();
                    break;
                //endregion
                case ÖĞRENCİ_NOTLARI:
                    //region ÖĞRENCİ NOTLARI İŞLEMLERİ


                    break;
                //endregion
            }

        }
    };

    public static void setSinifca(boolean sinifca) {
        sinifcanotver = sinifca;
    }

    public static void setSeciliOgrenci(Ogrenci ogrenci) {
        seciliOgrenci = ogrenci;
    }

    public static void setSeciliSinif(Sinif sinif) {
        seciliSinif = sinif;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_not_ver);

        myActivity = this;
        Log.d("Seçili öğrenci TC", String.valueOf(seciliOgrenci.getTcno()));

        //region Kontrolleri Ata
        btnOlumlu = (Button) findViewById(R.id.btnOlumlu);
        btnOlumsuz = (Button) findViewById(R.id.btnOlumsuz);
        grdDersler = (GridView) findViewById(R.id.grdDersler);

        //endregion


        //region Delegates
        btnOlumsuz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    btnOlumlu.setBackgroundDrawable(getResources().getDrawable(R.drawable.leftroundedtransparentbutton));
                    btnOlumsuz.setBackgroundDrawable(getResources().getDrawable(R.drawable.rightroundedbutton));
                } else {
                    btnOlumlu.setBackground(getResources().getDrawable(R.drawable.leftroundedtransparentbutton));
                    btnOlumsuz.setBackground(getResources().getDrawable(R.drawable.rightroundedbutton));
                }
                btnOlumsuz.setTextColor(Color.argb(255, 255, 255, 255));
                btnOlumlu.setTextColor(Color.argb(255, 5, 144, 255));
                grdDersler.setAdapter(adpOlumsuzdersler);

            }
        });

        btnOlumlu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    btnOlumlu.setBackgroundDrawable(getResources().getDrawable(R.drawable.leftroundedbutton));
                    btnOlumsuz.setBackgroundDrawable(getResources().getDrawable(R.drawable.rightroundedtransparentbutton));
                } else {
                    btnOlumlu.setBackground(getResources().getDrawable(R.drawable.leftroundedbutton));
                    btnOlumsuz.setBackground(getResources().getDrawable(R.drawable.rightroundedtransparentbutton));
                }
                btnOlumlu.setTextColor(Color.argb(255, 255, 255, 255));
                btnOlumsuz.setTextColor(Color.argb(255, 5, 144, 255));
                grdDersler.setAdapter(adpOlumludersler);

            }
        });

        grdDersler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!sinifcanotver) {
                    Ders ders;
                    String dersid;
                    if (grdDersler.getAdapter() == adpOlumludersler) {
                        dersid = olumluDersler.get(position).getId();
                        ders = olumluDersler.get(position);
                    } else {
                        dersid = olumsuzDersler.get(position).getId();
                        ders = olumsuzDersler.get(position);
                    }

                    String mesaj = "command=" + GenelDegiskenler.Istekler.NOT_VER.toString() +
                            ":" + OgretmenDegiskenler.aktifogretmen.getId() + ";:" +
                            dersid + ";:" +
                            seciliSinif.getId() + ";";
                    new ServerdanMesajAl(myHandler, NotVer.this).execute(mesaj);
                } else {
                    //TODO: SINIFA NOT VERME İŞLEMİ
                }


            }
        });
        //endregion

        DersleriAl();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void DersleriAl() {
        progressDialog = ProgressDialog.show(NotVer.this, "", "Değerlendirme konuları alınoyor...");
        String mesaj = "command=" + GenelDegiskenler.Istekler.ÖĞRETMEN_DERS_LİSTESİ_İSTE.toString() +
                ":" + OgretmenDegiskenler.aktifogretmen.getId() + ";:" +
                seciliSinif.getId() + ";:" +
                seciliOgrenci.getId() + ";";

        new ServerdanMesajAl(myHandler, NotVer.this).execute(mesaj);
    }

    private void DersListeleriniDoldur(boolean eksikresimvar) {
        if (eksikresimvar) {
            AsyncTask<String, Integer, Boolean> task = new AsyncTask<String, Integer, Boolean>() {
                @Override
                protected Boolean doInBackground(String[] params) {
                    boolean eksikdosya = true;
                    while (eksikdosya) {
                        eksikdosya = false;
                        for (int i = 0; i < dersler.size(); i++) {
                            File file = new File(getFilesDir(), dersler.get(i).getResimadi());
                            if (!file.exists()) {
                                eksikdosya = true;
                            }

                        }

                    }
                    return eksikdosya;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    resimHandler.sendEmptyMessage(0);
                }
            };
            task.execute("");

            // resimHandler.sendEmptyMessage(0);
        } else {
            resimHandler.sendEmptyMessage(0);

        }


    }

    private void Resimiste(String resimadi) {
        String mesaj = "command=" + GenelDegiskenler.Istekler.RESİM_İSTE.toString() +
                ":" + resimadi + ";";
        new ServerdanResimAl(resimHandler, NotVer.this, resimadi).execute(mesaj);
    }

    private void OgrenciNotlariniAl() {
        String mesaj = "command=" + GenelDegiskenler.Istekler.ÖĞRENCİ_NOTLARINI_İSTE.toString() +
                ":" + seciliOgrenci.getId() + ";";

        new ServerdanMesajAl(myHandler, NotVer.this).execute(mesaj);


    }


}
