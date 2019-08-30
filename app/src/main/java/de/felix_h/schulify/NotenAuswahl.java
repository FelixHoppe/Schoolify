package de.felix_h.schulify;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.io.File;

public class NotenAuswahl extends AppCompatActivity {

    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noten_auswahl);

        LinearLayout Layout = findViewById(R.id.notenAuswahl_ll);
        String Abfragen = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/";
        File tempFile = new File(Abfragen + "color/");
        if (tempFile.exists()) {
            ActionBar bar = getSupportActionBar();
            if(! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == Color.rgb(252, 252, 252)) && ! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == 0))
                bar.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(tempFile.listFiles()[0].getName().toString())));
            else
                bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(46, 204, 113)));
            Layout.setBackgroundColor(Integer.parseInt(tempFile.listFiles()[0].getName().toString()));
        }else
            Layout.setBackgroundColor(Color.rgb(252, 252, 252));



        CardView cardViewSchriftlich = (CardView) findViewById(R.id.cardViewSchriftlich);
        CardView cardViewMündlich = (CardView) findViewById(R.id.cardViewMündlich);

        cardViewSchriftlich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NotenAuswahl.this, noten.class);
                intent.putExtra("info","schriftlich");

                startActivity(intent);

            }
        });

        cardViewMündlich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NotenAuswahl.this, noten.class);
                intent.putExtra("info","mündlich");

                startActivity(intent);

            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeAct.class);
        i.putExtra("rate", true);
        startActivity(i);
    }

}
