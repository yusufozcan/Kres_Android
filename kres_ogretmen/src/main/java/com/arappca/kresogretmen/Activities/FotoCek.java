package com.arappca.kresogretmen.Activities;

import android.app.Activity;
import android.os.Bundle;

import com.arappca.kresogretmen.R;

import java.io.File;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FotoCek extends Activity {
    public static MesajGonder mesajAktivity;
    public static List<File> cekilenresimler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_foto_cek);


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }


}
