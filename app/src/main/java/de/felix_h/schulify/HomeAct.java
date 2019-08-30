package de.felix_h.schulify;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.app.Notification.VISIBILITY_PUBLIC;


public class HomeAct extends AppCompatActivity implements View.OnClickListener{

    private CardView noten, stundenplan, hausaufgaben, statistiken, einstellungen, info;

    public String AktJahrPath;

    int backAnzahl = 0;

    int JAHR;

    AlertDialog dialog;

    public String path;

    public String notifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notifications = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/" + "notifications/";
        AktJahrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/JahrAuswahl/";

        final int startRate = 7;
        if(getIntent().getBooleanExtra("rate", false))
        {
            final String opencounter = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/opencounter/";
            final File f = new File(opencounter);
            int temp = Integer.parseInt(f.listFiles()[0].getName().toString());
            if(temp >= startRate && temp < 100000)
            {
                final AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(HomeAct.this);
                View mView1 = getLayoutInflater().inflate(R.layout.rate_app, null);
                Button gefällt = mView1.findViewById(R.id.gefällt);
                Button gefällt_nicht = mView1.findViewById(R.id.gefällt_nicht);
                Button später = mView1.findViewById(R.id.später);

                gefällt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        f.listFiles()[0].renameTo(new File(opencounter + 100000));

                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        dialog.cancel();

                    }
                });

                gefällt_nicht.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

                        f.listFiles()[0].renameTo(new File(opencounter + 100000));

                        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(HomeAct.this);
                        View mView2 = getLayoutInflater().inflate(R.layout.layout_feedback, null);
                        Button alles_klar = mView2.findViewById(R.id.alles_klar);

                        alles_klar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });

                        mBuilder2.setView(mView2);
                        dialog = mBuilder2.create();
                        dialog.show();
                    }
                });

                später.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f.listFiles()[0].renameTo(new File(opencounter + (startRate - 3)));
                        dialog.cancel();
                    }
                });

                mBuilder1.setView(mView1);
                dialog = mBuilder1.create();
                dialog.show();
            }
        }
        else
        {

        }




/*
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .session(7)
                .playstoreUrl("url")
                .build();

        ratingDialog.show();

*/


        LinearLayout Layout = findViewById(R.id.home_ll);
        String Abfragen = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/";
        File tempFile = new File(Abfragen + "color/");
        if (tempFile.exists()) {
            ActionBar bar = getSupportActionBar();
            if(! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == Color.rgb(252, 252, 252)) && ! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == 0))
                bar.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(tempFile.listFiles()[0].getName().toString())));
            else
                bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(46, 204, 113)));
            Layout.setBackgroundColor(Integer.parseInt(tempFile.listFiles()[0].getName().toString()));
        }
        else
            Layout.setBackgroundColor(Color.rgb(252, 252, 252));

        File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/");
        if(!f1.exists())
            f1.mkdir();

        File f2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/JahrAuswahl/");
        if(!f2.exists())
            f2.mkdir();

            backAnzahl+= getIntent().getIntExtra("ANZAHL", 0);

            JAHR = getIntent().getIntExtra("JAHR", 0);

        File AktJahrZahl = new File(AktJahrPath + JAHR + "/");
            if(!AktJahrZahl.exists() && JAHR != 0)
                AktJahrZahl.mkdir();


            noten = (CardView) findViewById(R.id.noten);
        stundenplan = (CardView) findViewById(R.id.stundenplan);
        hausaufgaben = (CardView) findViewById(R.id.hausaufgaben);
        statistiken = (CardView) findViewById(R.id.statistiken);
        einstellungen = (CardView) findViewById(R.id.einstellungen);
        info = (CardView) findViewById(R.id.info);

        noten.setOnClickListener(this);
        stundenplan.setOnClickListener(this);
        hausaufgaben.setOnClickListener(this);
        statistiken.setOnClickListener(this);
        einstellungen.setOnClickListener(this);
        info.setOnClickListener(this);

     }



    @Override
    public void onBackPressed() {
        if (backAnzahl != 1) {

            Toast.makeText(getBaseContext(), "drücke noch einmal zum beenden", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, HomeAct.class);
            i.putExtra("ANZAHL", 1);
            startActivity(i);
        } else
        {
            backAnzahl = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
            case R.id.noten : i = new Intent(this, NotenAuswahl.class); startActivity(i); break;
            case R.id.stundenplan : i = new Intent(this, stundenplan.class); startActivity(i); break;
            case R.id.hausaufgaben : i = new Intent(this, hausaufgaben.class); startActivity(i); break;
            case R.id.statistiken : i = new Intent(this, statistiken.class); startActivity(i); break;
            case R.id.einstellungen : i = new Intent(this, einstellungen.class); startActivity(i); break;
            case R.id.info : i = new Intent(this, info.class); startActivity(i); break;
            default: break;
        }

        //Weiter bei https://youtu.be/d6CfaWW7G5Q?t=5m40s


    }


}
