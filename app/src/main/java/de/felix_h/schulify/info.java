package de.felix_h.schulify;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.File;

public class info extends AppCompatActivity implements View.OnClickListener{

    private CardView libraries, agb, kontakt;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        libraries = (CardView) findViewById(R.id.libraries);
        agb = (CardView) findViewById(R.id.agb);
        kontakt = (CardView) findViewById(R.id.kontakt);

        libraries.setOnClickListener(this);
        agb.setOnClickListener(this);
        kontakt.setOnClickListener(this);


        LinearLayout Layout = findViewById(R.id.info_ll);
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

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeAct.class);
        i.putExtra("rate", true);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.libraries:


                final AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(info.this);
                View mView1 = getLayoutInflater().inflate(R.layout.info_text_libs, null);

                final TextView text1 = mView1.findViewById(R.id.info_textview_libs);


                mBuilder1.setView(mView1);
                dialog = mBuilder1.create();
                dialog.show();



                return;

            case R.id.agb:

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://app.felix-h.de/datenschutz"));
                startActivity(browserIntent);


                return;

            case R.id.kontakt:

                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "" + "&body=" + "" + "&to=" + "appsupport@felix-h.de");
                testIntent.setData(data);
                startActivity(testIntent);
                return;

            default:
                return;
        }


    }
}