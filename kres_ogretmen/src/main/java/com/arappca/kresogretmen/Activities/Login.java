package com.arappca.kresogretmen.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arappca.kresogretmen.Genel.OgretmenDegiskenler;
import com.arappca.kresogretmen.R;
import com.arappca.library.Classes.EduClasses.Ogretmen;
import com.arappca.library.Classes.EduClasses.Okul;
import com.arappca.library.Classes.EduClasses.Sinif;
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
import static com.arappca.library.Fonksiyonlar.mesajGoster;

public class Login extends AppCompatActivity {
    EditText txtUsername;
    EditText txtPassword;
    Button btnLogin;
    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (!isInternetAvailable(msg.obj.toString(), Login.this)) {
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
                Fonksiyonlar.mesajGoster(Login.this, "Sunucuya bağlanırken bir hata oluştu");
                GenelDegiskenler.progressDialog.dismiss();
                return;
            }
            GenelDegiskenler.Responses responses = GenelDegiskenler.Responses.values()[cevap];
            switch (responses) {
                case GİRİŞ_BAŞARILI:
                    //region Giriş işlemleri

                    Log.d("Sonuç", msg.obj.toString());

                    try {
                        JSONObject jObject = new JSONObject(inputJSONString);
                        Ogretmen ogretmen = new Ogretmen();
                        ogretmen.setId(jObject.getString("id"));
                        ogretmen.setMail(jObject.getString("mail"));
                        ogretmen.setUsername(jObject.getString("username"));
                        ogretmen.setAdsoyad(jObject.getString("adsoyad"));


                        JSONArray jsonsiniflar = jObject.getJSONArray("siniflar");
                        for (int i = 0; i < jsonsiniflar.length(); i++) {
                            Sinif sinif = new Sinif();
                            sinif.setId(jsonsiniflar.getJSONObject(i).getInt("id"));
                            sinif.setIsim(jsonsiniflar.getJSONObject(i).getString("isim"));

                            Okul okul = new Okul();
                            JSONObject jsonokul = jsonsiniflar.getJSONObject(i).getJSONObject("okul");
                            okul.setId(jsonokul.getString("id"));
                            okul.setIsim(jsonokul.getString("isim"));
                            okul.setAdres(jsonokul.getString("adres"));
                            okul.setTelefon(jsonokul.getString("telefon"));
                            okul.setYonetici(new Yonetici());
                            sinif.setOkul(okul);
                            ogretmen.getSiniflar().add(sinif);
                        }
                        OgretmenDegiskenler.aktifogretmen = ogretmen;

                        switch (OgretmenDegiskenler.aktifogretmen.getSiniflar().size()) {
                            case 0:
                                //TODO: Öğretmenin Sınıfı Olmadığı Konusunda Uyar
                                break;
                            case 1:
                                Intent intent = new Intent(Login.this, OgrenciListesi.class);
                                intent.putExtra(GenelDegiskenler.Extras.SinifID.toString(), 0);
                                startActivity(intent);
                                break;

                            default:

                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //endregion
                case HATALI_LOGİN:
                    GenelDegiskenler.progressDialog.dismiss();
                    mesajGoster(Login.this, "Hatalı kullanıcı adı veya şifre. Lütfen bilgileri kontrol edip tekrar deneyin.");
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
        setContentView(R.layout.activity_login);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenelDegiskenler.progressDialog = ProgressDialog.show(Login.this, "", "Giriş yapılıyor...");
                String mesaj = "command=" + GenelDegiskenler.Istekler.ÖĞRETMEN_GİRİŞİ.toString() + ":" + txtUsername.getText().toString() + ";:" + txtPassword.getText().toString() + ";";
                new ServerdanMesajAl(myHandler, Login.this).execute(mesaj);


            }
        });
    }
}
