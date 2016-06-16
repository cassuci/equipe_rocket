package com.draft.rckt.equiperocket.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.draft.rckt.equiperocket.Gasto.GastoController;
import com.draft.rckt.equiperocket.R;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                /**TODO
                 * Mudar para chamar activity de login
                 *
                 */
                Intent i = new Intent(SplashScreen.this, GastoController.class);
                startActivity(i);

                // Fecha esta activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
