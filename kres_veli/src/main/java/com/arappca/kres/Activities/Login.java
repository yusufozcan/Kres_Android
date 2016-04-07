package com.arappca.kres.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arappca.kres.R;
import com.arappca.kres.VeliDegiskenler;
import com.arappca.library.Classes.EduClasses.Ogrenci;
import com.arappca.library.Classes.EduClasses.Okul;
import com.arappca.library.Classes.EduClasses.Sinif;
import com.arappca.library.Classes.EduClasses.Veli;
import com.arappca.library.Classes.EduClasses.Yonetici;
import com.arappca.library.Classes.ServerdanMesajAl;
import com.arappca.library.Fonksiyonlar;
import com.arappca.library.GenelDegiskenler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.arappca.library.Fonksiyonlar.isInternetAvailable;


public class Login extends AppCompatActivity {
    EditText txtUsername;
    EditText txtPassword;
    Button btnLogin;
    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (!isInternetAvailable(msg.obj.toString(), Login.this)) {
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
                Fonksiyonlar.mesajGoster(Login.this, "Sunucuya bağlanırken bir hata oluştu");
                return;
            }
            GenelDegiskenler.Responses responses = GenelDegiskenler.Responses.values()[cevap];
            switch (responses) {
                case GİRİŞ_BAŞARILI:
                    // calling to this function from other pleaces
                    // The notice call method of doing things
                    Log.d("Sonuç", msg.obj.toString());

                    try {
                        JSONObject jObject = new JSONObject(inputJSONString);
                        Veli veli = new Veli();
                        veli.setId(jObject.getString("id"));
                        veli.setMail(jObject.getString("mail"));
                        veli.setUsername(jObject.getString("username"));
                        veli.setAdsoyad(jObject.getString("adsoyad"));
                        veli.setAdres(jObject.getString("adres"));
                        veli.setTelefon(jObject.getString("telefon"));

                        JSONArray jsonOgrenciler = jObject.getJSONArray("ogrenciler");
                        for (int i = 0; i < jsonOgrenciler.length(); i++) {
                            Ogrenci ogrenci = new Ogrenci();
                            ogrenci.setId(jsonOgrenciler.getJSONObject(i).getString("id"));
                            ogrenci.setTcno(jsonOgrenciler.getJSONObject(i).getString("tcno"));
                            ogrenci.setAdsoyad(jsonOgrenciler.getJSONObject(i).getString("adsoyad"));
                            ogrenci.setAnneadi(jsonOgrenciler.getJSONObject(i).getString("anneadi"));
                            ogrenci.setBabaadi(jsonOgrenciler.getJSONObject(i).getString("babaadi"));
                            ogrenci.setEkbilgi(jsonOgrenciler.getJSONObject(i).getString("ekbilgi"));
                            ogrenci.setSonpuan(jsonOgrenciler.getJSONObject(i).getInt("sonpuan"));

                            JSONArray jsonSiniflar = jsonOgrenciler.getJSONObject(i).getJSONArray("siniflar");

                            for (int s = 0; s < jsonSiniflar.length(); s++) {
                                //"okul":{"id":1,"isim":"TEST OKULU 1","adres":"test okulu 1 in adresi","telefon":"test okulu 1 telefonu","yonetici":{}}},{"id":"2","isim":"TEST SINIFI 2","okul":{"id":2,"isim":"TEST OKULU 2","adres":"test okulu 2 adresi","telefon":"test okulu 2 telefonu","yonetici":{}}}]}]}
                                Sinif sinif = new Sinif();
                                sinif.setId(jsonSiniflar.getJSONObject(s).getInt("id"));
                                sinif.setIsim(jsonSiniflar.getJSONObject(s).getString("isim"));

                                JSONObject okullar = jsonSiniflar.getJSONObject(s).getJSONObject("okul");
                                Okul okul = new Okul();
                                okul.setId(okullar.getString("id"));
                                okul.setIsim(okullar.getString("isim"));
                                okul.setAdres(okullar.getString("adres"));
                                okul.setTelefon(okullar.getString("telefon"));
                                okul.setYonetici(new Yonetici());

                                sinif.setOkul(okul);
                                ogrenci.getSiniflar().add(sinif);
                            }
                            veli.getOgrenciler().add(ogrenci);
                        }

                        VeliDegiskenler.aktifveli = veli;
                        startActivity(new Intent(Login.this, VeliAnaEkran.class));

                        Log.d("Sonuç", "Giriş Başarılı");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case HATALI_LOGİN:
                    Log.d("Sonuç", responses.toString());

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = "command=" + GenelDegiskenler.Istekler.VELİ_GİRİŞİ.toString() + ":" + txtUsername.getText().toString() + ";:" + txtPassword.getText().toString() + ";";
                new ServerdanMesajAl(myHandler, Login.this).execute(mesaj);

            }
        });

    }

}
