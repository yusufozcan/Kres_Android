package com.arappca.kresogretmen.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.arappca.kresogretmen.Genel.OgretmenDegiskenler;
import com.arappca.kresogretmen.R;
import com.arappca.library.GenelDegiskenler;

public class AnaEkran extends AppCompatActivity {
    Button btnNotver;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_ekran);

        //region Kontrolleri ata
        btnNotver = (Button) findViewById(R.id.btnNotver);
        //endregion

        //region Add Delegates
        btnNotver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (OgretmenDegiskenler.aktifogretmen.getSiniflar().size()) {
                    case 0:
                        //TODO: Öğretmenin Sınıfı Olmadığı Konusunda Uyar
                        break;
                    case 1:
                        Intent intent = new Intent(AnaEkran.this, OgrenciListesi.class);
                        intent.putExtra(GenelDegiskenler.Extras.SinifID.toString(), 0);
                        startActivity(intent);
                        break;

                    default:

                        break;
                }
            }
        });

        //bu region ve end region  bu kod bloğunu kapatabilmek için. şu şekilde. daha güzel ve toplu görünmesi için
        //endregion

    }
}
