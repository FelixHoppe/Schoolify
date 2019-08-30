package de.felix_h.schulify;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import java.io.File;

import ng.max.slideview.SlideView;


public class StartActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 1;
    private SlideView slideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        slideView = (SlideView) findViewById(R.id.slideView);

        if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideView slideView) {

                    requestStoragePermission();
                    slideView.setVisibility(View.INVISIBLE);

                }
            });

        }
        else
            normalStart();
    }








    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Erlaubnis erforderlich")
                    .setMessage("Ohne Dateien speichern zu können, kann die App leider nicht funktionieren")
                    .setPositiveButton("verstanden", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(StartActivity.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("abbrechen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                normalStart();
            } else {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Erlaubnis erforderlich")
                            .setMessage("Bitte erlaube in den Einstellungen unter App-Info den Dateienzugriff, da die App ansonsten nichts abspeichern kann")
                            .setPositiveButton("verstanden", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                }
                else
                    slideView.setVisibility(View.VISIBLE);


            }
        }
    }

    public void normalStart()
    {
        String mainPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/";
        String abfragenPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/";
        String AktJahrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/JahrAuswahl/";
        String showCase = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/showcase/";
        String schriftlich = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/schriftlich/";
        String muendlich = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/muendlich/";
        String noten = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/";
        String hausaufgaben = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Hausaufgaben";

        File counter;
        File open_counter = new File(abfragenPath + "opencounter/");

        File dir = new File(mainPath);

        if (!dir.exists()) {
            dir.mkdirs();

            File f_noten = new File(noten);
            File f_muendlich = new File(muendlich);
            File f_schriftlich = new File(schriftlich);
            File f_hausaufgaben = new File(hausaufgaben);
            f_noten.mkdir();
            f_muendlich.mkdir();
            f_schriftlich.mkdir();
            f_hausaufgaben.mkdir();

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else {
            File abfragen = new File(abfragenPath);
            if (!abfragen.exists()) {
                abfragen.mkdir();
                File f_showCase = new File(showCase);
                f_showCase.mkdir();

                open_counter.mkdir();

                counter = new File(abfragenPath + "opencounter/1");
                counter.mkdir();

                File aktJahr = new File(AktJahrPath);
                if (!aktJahr.exists()) {
                    aktJahr.mkdir();

                    Intent i = new Intent(getApplicationContext(), Jahr.class);
                    startActivity(i);
                }
            } else {
                counter = open_counter.listFiles()[0];
                counter.renameTo(new File(abfragenPath + "opencounter/" + (Integer.parseInt(counter.getName().toString()) + 1)));

                Intent i = new Intent(getApplicationContext(), HomeAct.class);
                startActivity(i);
            }
        }
    }
}