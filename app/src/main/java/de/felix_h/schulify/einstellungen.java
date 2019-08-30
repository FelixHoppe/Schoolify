package de.felix_h.schulify;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Layout;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;


import java.io.File;
import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;


public class einstellungen extends AppCompatActivity implements View.OnClickListener{

    private CardView color, jahr, notifications;

    AlertDialog dialog;
    public String Abfragen;
    public String AktJahrPath;
    File aktJahrFile;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einstellungen);

        Abfragen = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/";
        AktJahrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/JahrAuswahl/";

        linearLayout = findViewById(R.id.einstellungen_ll);

        color = (CardView) findViewById(R.id.color);
        jahr = (CardView) findViewById(R.id.jahr);
        notifications = (CardView) findViewById(R.id.notifications);

        color.setOnClickListener(this);
        jahr.setOnClickListener(this);
        notifications.setOnClickListener(this);



        File tempFile = new File(Abfragen + "color/");
        if (tempFile.exists()) {
            ActionBar bar = getSupportActionBar();
            if(! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == Color.rgb(252, 252, 252)) && ! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == 0))
                bar.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(tempFile.listFiles()[0].getName().toString())));
            else
                bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(46, 204, 113)));
            linearLayout.setBackgroundColor(Integer.parseInt(tempFile.listFiles()[0].getName().toString()));
        }



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.color :




                ColorPicker colorPicker = new ColorPicker(this);
                colorPicker.setRoundColorButton(true);
                colorPicker.setTitle("Farbe auswählen");
                colorPicker.setColorButtonSize(60, 60);

                File tempFile = new File(Abfragen + "color/");
                if (tempFile.exists()) {
                    if(! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == Color.rgb(252, 252, 252)) && ! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == 0))
                        colorPicker.setDefaultColorButton(Integer.parseInt(tempFile.listFiles()[0].getName().toString()));
                    else
                        colorPicker.setDefaultColorButton(Color.WHITE);
                } else {
                    colorPicker.setDefaultColorButton(Color.WHITE);
                }

                colorPicker.setColumns(4);
                colorPicker.setColors(Color.rgb(252, 252, 252), Color.YELLOW, Color.RED, Color.MAGENTA, Color.rgb(255, 135, 0), Color.rgb(255, 194, 136), Color.GREEN , Color.rgb(46, 204, 113), Color.CYAN, Color.BLUE, Color.GRAY, Color.rgb(23, 27, 35));
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {


                        File tempFile = new File(Abfragen + "color/");
                        if(!tempFile.exists())
                        {
                            tempFile.mkdir();
                            File colorFile = new File(Abfragen + "color/" + color);
                            colorFile.mkdir();
                        }
                        else
                            tempFile.listFiles()[0].renameTo(new File(Abfragen + "color/" + color));

                        File tempFile2 = new File(Abfragen + "color/");
                        if (tempFile2.exists()) {
                            ActionBar bar = getSupportActionBar();
                            if(! (Integer.parseInt(tempFile2.listFiles()[0].getName().toString()) == Color.rgb(252, 252, 252)) && ! (Integer.parseInt(tempFile2.listFiles()[0].getName().toString()) == 0))
                                bar.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(tempFile2.listFiles()[0].getName().toString())));
                            else
                                bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(46, 204, 113)));
                            linearLayout.setBackgroundColor(Integer.parseInt(tempFile2.listFiles()[0].getName().toString()));
                        }

                    }

                    @Override
                    public void onCancel(){
                        // put code
                    }
                });



                break;


            case R.id.jahr :

                File[] temp = new File(AktJahrPath).listFiles();

                for(File f : temp)
                    if(!f.getName().equals("0"))
                        aktJahrFile = f;

                ArrayList<String> Schuljahre = new ArrayList<>();
                for(int i = 0; i < 13; i++)
                    Schuljahre.add((i+1) + ". Klasse");

                CharSequence[] cs = Schuljahre.toArray(new CharSequence[Schuljahre.size()]);

                final AlertDialog.Builder builder = new AlertDialog.Builder(einstellungen.this);
                builder.setTitle("Wähle ein Jahr");

                builder.setItems(cs, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aktJahrFile.renameTo(new File(AktJahrPath + (which + 1) ));
                    }

                });

                builder.show();

                break;


            case R.id.notifications :






                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(einstellungen.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_notifications, null);
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();




                break;

            default :
                break;
        }

        //Weiter bei https://youtu.be/d6CfaWW7G5Q?t=5m40s


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeAct.class);
        i.putExtra("rate", true);
        startActivity(i);
    }

}
