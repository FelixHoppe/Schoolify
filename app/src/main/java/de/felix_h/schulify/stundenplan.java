package de.felix_h.schulify;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.FileObserver;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.models.ValueLinePoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.felix_h.schulify.librarys.scrolltable.Position;
import de.felix_h.schulify.librarys.scrolltable.ScrollTableView;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class stundenplan extends AppCompatActivity {

    private static final String[] topTitles = new String[] {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};

    private ScrollTableView scrollTableView;
    Button btn;
    AlertDialog dialog;
    private String[][] fächer;
    File stundenplanPath;

    File[] wochentage = new File[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stundenplan);

        stundenplanPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Stundenplan/");

        RelativeLayout Layout = findViewById(R.id.stundenplan_rl);
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



        scrollTableView = (ScrollTableView) findViewById(R.id.scroll_table_view);
        final ArrayList<String> leftTitle = createLeftTitle();
        final ArrayList<String> topTitles = createTopTitles();
        btn = findViewById(R.id.btnSP_Add);

        File tempF = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Stundenplan/");
        if(!tempF.exists())
            tempF.mkdir();

        wochentage[0] = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Stundenplan/montag.txt");
        wochentage[1] = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Stundenplan/dienstag.txt");
        wochentage[2] = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Stundenplan/mittwoch.txt");
        wochentage[3] = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Stundenplan/donnerstag.txt");
        wochentage[4] = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Stundenplan/freitag.txt");

        if(!stundenplanPath.exists())
            stundenplanPath.mkdir();

        for(int i = 0; i < wochentage.length; i++)
        {
            if(!wochentage[i].exists())
                try {
                    wochentage[i].createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        scrollTableView.setDatas(createTopTitles(), createLeftTitle(), createContent(leftTitle.size(), topTitles.size()));



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Position> positions = scrollTableView.getSelectedPositions();

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(stundenplan.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_stundenplan, null);
                final TextView spText = mView.findViewById(R.id.addSP);
                final EditText spName = mView.findViewById(R.id.spName);
                Button spAdd = mView.findViewById(R.id.btn_addSP);
                fächer = new String[5][15];
                for(int i = 0; i < fächer.length; i++)
                    for(int j = 0; j < fächer[i].length; j++)
                        if(fächer[i][j] == null)
                            fächer[i][j] = "";

                spAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!spName.getText().toString().contains("\n"))
                        {
                            for(int i = 0; i < wochentage.length; i++)
                            {
                                if(!wochentage[i].exists())
                                    try {
                                        wochentage[i].createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                else
                                {
                                    String[] loaded = Load(wochentage[i]);

                                    for(int j = 0; j < fächer.length; j++)
                                        fächer[i][j] = "";

                                    for(int y = 0; y < loaded.length; y++)
                                        fächer[i][y] = loaded[y];
                                }

                            }

                            for(int i = 0; i < positions.size(); i++)
                            {
                                int x = positions.get(i).x;
                                int y = positions.get(i).y;
                                fächer[x][y] = spName.getText().toString();
                                Save(wochentage[x], fächer[x]);
                            }


                            scrollTableView.setDatas(createTopTitles(), createLeftTitle(), createContent(leftTitle.size(), topTitles.size()));

                            dialog.cancel();


                            String showCaseView = "stundenplan2";
                            File showCaseFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/showcase/" + showCaseView);
                            if (!showCaseFile.exists()) {
                                showCaseFile.mkdir();
                                showShowCaseView("Um Felder wieder zu löschen, einfach beim Ändern das Fächer-Feld leer lassen");
                            }
                        }
                        else
                            Toast.makeText(stundenplan.this, "Bitte nur eine Zeile eingeben!", Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();



            }
        });


        String showCaseView = "stundenplan1";
        File showCaseFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/showcase/" + showCaseView);
        if (!showCaseFile.exists()) {
            showCaseFile.mkdir();
            showShowCaseView("Markiere ein oder mehrere Felder und klicke anschließend auf 'MARKIERTE FELDER BEARBEITEN'");
        }

    }

    private void showShowCaseView(String text) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        new FancyShowCaseView.Builder(this)
                .title(text)
                .focusCircleAtPosition(width/3, height / 5, width / 6)
                .build()
                .show();
    }



    private ArrayList<ArrayList<String>> createContent(int row, int column) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        String[][] fächer = new String[5][15];
        for(int i = 0; i < fächer.length; i++)
        {
            for(int j = 0; j < fächer[i].length; j++) {
                fächer[i][j] = "";
            }
            String[] temp = Load(wochentage[i]);
            for(int j = 0; j < temp.length; j++) {
                fächer[i][j] = temp[j];
            }
        }
        for (int i = 0; i < row; i++) {
            ArrayList<String> strings = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                strings.add(fächer[j][i]);
            }
            results.add(strings);
        }
        return results;
    }

    private ArrayList<String> createTopTitles() {
        ArrayList<String> results = new ArrayList<>();
        for (String string : topTitles) {
            results.add(string);
        }
        return results;
    }

    private ArrayList<String> createLeftTitle() {
        ArrayList<String> results = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            results.add(i + ". Stunde");
        }
        results.add("1. Pause");
        for (int i = 3; i < 5; i++) {
            results.add(i + ". Stunde");
        }
        results.add("2. Pause");
        for (int i = 5; i < 10; i++) {
            results.add(i + ". Stunde");
        }
        for(int i = 10; i < 13; i++){
            results.add(i + ".Stunde");
        }
        return results;
    }


    public void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                String[] existingText = Load(file);
                for(int i = 0; i < existingText.length; i++)
                    fos.write(existingText[i].getBytes());

                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }

    public String[] Load(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeAct.class);
        i.putExtra("rate", true);
        startActivity(i);
    }


}
