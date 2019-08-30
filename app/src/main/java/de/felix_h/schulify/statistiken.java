package de.felix_h.schulify;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class statistiken extends AppCompatActivity {


    public String path;
    public String schriftlich;
    public String muendlich;

    File schriftlich_file;
    File muendlich_file;
    File[] sDirs;
    File[] mDirs;
    private int FILE_LENGHT = 6;
    private int NOTE_LENGHT = 15;
    int ANZAHL_FILES = 25;

    private Button mNextBtn;
    private Button mBackBtn;

    Button fBtn;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private Statistiken_SliderAdapter sliderAdapter;

    PieChart mPieChart = null;
    BarChart mBarChart = null;
    ValueLineChart mLineChart = null;

    private int mCurrentPage;

    private TextView avgText;


    int JAHR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiken);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Noten/";
        schriftlich = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/schriftlich/";
        muendlich = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/muendlich/";

        schriftlich_file = new File(schriftlich);
        muendlich_file = new File(muendlich);
        sDirs = schriftlich_file.listFiles();
        mDirs = muendlich_file.listFiles();

        RelativeLayout Layout = findViewById(R.id.layout);
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



        mNextBtn = (Button) findViewById(R.id.nextBtn);
        mBackBtn = (Button) findViewById(R.id.prevBtn);

        avgText = (findViewById(R.id.avg));

        //Aktuelles Jahr auslesen

        File AktJahrPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/JahrAuswahl/");

        File tempFile1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/schriftlich/");
        if(!tempFile1.exists())
            tempFile1.mkdir();

        File tempFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/muendlich/");
        if(!tempFile2.exists())
            tempFile2.mkdir();


        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSlideViewPager.setCurrentItem(mCurrentPage + 1);

            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });


        File[] AktJahr = AktJahrPath.listFiles();
        JAHR = Integer.parseInt(AktJahr[0].getName().toString());


        mBarChart = (BarChart) findViewById(R.id.barchart);
        mBarChart.setVisibility(View.VISIBLE);

        mLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
        mLineChart.setVisibility(View.INVISIBLE);

        mPieChart = (PieChart) findViewById(R.id.piechart);
        mPieChart.setVisibility(View.INVISIBLE);

        fBtn = findViewById(R.id.button2);
        fBtn.setVisibility(View.INVISIBLE);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter = new Statistiken_SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        onFirstStart();
    }




    public void addDotsIndicator(int position){

        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++) {

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);

        }

        if(mDots.length > 0){

            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));

        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);

            mCurrentPage = i;

            File[] dirs = new File[mDirs.length + sDirs.length];

            for (int j = 0; j < mDirs.length; j++) {
                dirs[j] = mDirs[j];
            }

            int mDirCounter = mDirs.length;

            for (int j = 0; j < sDirs.length; j++) {
                dirs[mDirCounter] = sDirs[j];
                mDirCounter++;
            }


            //////////////////



            for (int j = 0; j < sDirs.length; j++) {
                dirs[j] = sDirs[j];

            }

            int sDirCounter = sDirs.length;

            for (int j = 0; j < mDirs.length; j++) {
                dirs[sDirCounter] = mDirs[j];
                sDirCounter++;
            }










            int fileCounter = 0;

            //Anzahl an Dateien herausfinden
            for (int j = 0; j < dirs.length; j++) {
                for (int z = 0; z < dirs[j].listFiles().length; z++) {
                    fileCounter++;
                }
            }

            final File[] files = new File[fileCounter];
            fileCounter = 0;
            for (int j = 0; j < dirs.length; j++) {
                for (int z = 0; z < dirs[j].listFiles().length; z++) {
                    files[fileCounter] = dirs[j].listFiles()[z];
                    fileCounter++;
                }
            }


            if (i == 0) {


                avgText.setVisibility(View.VISIBLE);
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
                mNextBtn.setClickable(true);
                mNextBtn.setTag("Weiter");
                mBackBtn.setTag("");


                mLineChart.setVisibility(View.INVISIBLE);
                mPieChart.setVisibility(View.INVISIBLE);
                mBarChart.clearChart();
                mBarChart.setVisibility(View.VISIBLE);
                fBtn.setVisibility(View.INVISIBLE);

                String[][] notes = new String[files.length][FILE_LENGHT];

                BarChart mBarChart = (BarChart) findViewById(R.id.barchart);

                int[] mColors = {
                        0xFF123456,
                        0xFF343456,
                        0xFF563456,
                        0xFF873F56,
                        0xFF56B7F1,
                        0xFF343456,
                        0xFF1FF4AC,
                        0xFF1BA4E6
                };


                //Für jedes Fach jedes Halbjahr nach gleichen Noten auslesen und die werte addieren und dann durch die anzahl an noten pro fach teilen

                String[] fächer = new String[ANZAHL_FILES];
                int fächercounter = 0;
                boolean noFile;

                for (int u = 0; u < files.length; u++) {
                    noFile = true;
                    String[] loads = Load(files[u]);
                    for (int z = 0; z < fächercounter; z++) {
                        if (!loads[5].equalsIgnoreCase(fächer[z])) {
                            noFile = true;
                        } else {
                            noFile = false;
                            break;
                        }

                    }
                    if (noFile) {
                        fächer[fächercounter] = loads[5];
                        fächercounter++;
                    }
                }


                int[] fächerNoten = new int[fächercounter];
                int notenCounter[] = new int[fächercounter];
                float[] avg = new float[fächercounter];
                for (int u = 0; u < files.length; u++) {
                    notes[u] = Load(files[u]);
                    for (int z = 0; z < fächercounter; z++) {
                        if (notes[u][5].equalsIgnoreCase(fächer[z])) {
                            for (int y = 0; y < 4; y++) {
                                if (!notes[u][y + 1].equals("")) {
                                    fächerNoten[z] += Integer.parseInt(notes[u][y + 1]);
                                    notenCounter[z]++;
                                }
                            }
                        }
                    }
                }

                for (int x = 0; x < fächercounter; x++) {
                    if (notenCounter[x] != 0) {
                        avg[x] = fächerNoten[x] / notenCounter[x];
                        double fN = fächerNoten[x];
                        double nC = notenCounter[x];
                        double number = fN / nC;
                        int decimal = (int) number;
                        double fractional = number - decimal;
                        if(fractional >= 0.5)
                            avg[x] += 1;


                        if (fächer[x].length() > 7)
                            fächer[x] = fächer[x].substring(0, 7);

                        mBarChart.addBar(new BarModel(fächer[x], avg[x], mColors[(int) Math.floor(Math.random() * (mColors.length))]));
                    }
                }


                mBarChart.startAnimation();


            } else if (i == mDots.length - 1) {


                avgText.setVisibility(View.INVISIBLE);
                mNextBtn.setEnabled(false);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setClickable(false);
                mNextBtn.setTag("");
                mBackBtn.setTag("Zurück");


                mBarChart.setVisibility(View.INVISIBLE);
                mLineChart.setVisibility(View.INVISIBLE);
                fBtn.setVisibility(View.INVISIBLE);


                mPieChart.clearChart();
                mPieChart.setVisibility(View.VISIBLE);

                int[] allNotes = new int[NOTE_LENGHT];
                for (int y = 0; y < allNotes.length; y++)
                    allNotes[y] = 0;

                String[][] notes = new String[files.length][FILE_LENGHT];
                for (int u = 0; u < files.length; u++) {
                    notes[u] = Load(files[u]);
                }

                for (int y = 0; y < files.length; y++) {
                    for (int j = 0; j < 4; j++) {
                        for (int z = 0; z < allNotes.length; z++) {
                            if (!notes[y][j + 1].equals(""))
                                if (Integer.parseInt(notes[y][j + 1]) == z + 1)
                                    allNotes[z]++;
                        }
                    }
                }


                String[] mColors = {
                        "#39add1", // light blue
                        "#3079ab", // dark blue
                        "#c25975", // mauve
                        "#e15258", // red
                        "#f9845b", // orange
                        "#838cc7", // lavender
                        "#7d669e", // purple
                        "#53bbb4", // aqua
                        "#51b46d", // green
                        "#e0ab18", // mustard
                        "#637a91", // dark gray
                        "#f092b0", // pink
                        "#b7c0c7",  // light gray
                        "#F5DEB3", //wheat
                        "#D2691E",   //chocolate#
                        "#000000"   //black

                };


                for (int j = 0; j < allNotes.length; j++) {
                    String color = mColors[j];
                    mPieChart.addPieSlice(new PieModel("Note: " + (j + 1), allNotes[j], Color.parseColor(color)));
                }


                mPieChart.startAnimation();

            }


            else

            {

                avgText.setVisibility(View.INVISIBLE);
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
                mNextBtn.setClickable(true);
                mNextBtn.setTag("Weiter");
                mBackBtn.setTag("Zurück");


                mPieChart.setVisibility(View.INVISIBLE);
                mBarChart.setVisibility(View.INVISIBLE);
                mLineChart.clearChart();
                mLineChart.setVisibility(View.VISIBLE);
                fBtn.setVisibility(View.VISIBLE);



                final ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
                mCubicValueLineChart.setShowIndicator(false);

                final ValueLineSeries series = new ValueLineSeries();
                series.setColor(0xFF56B7F1);


                String[] paths = new String[files.length];
                for (int j = 0; j < paths.length; j++)
                    paths[j] = files[j].getName();


                final String[][] notes = new String[files.length][FILE_LENGHT];
                for (int u = 0; u < files.length; u++)
                    notes[u] = Load(files[u]);

                final ArrayList<String> fächerNamen = new ArrayList<String>() {
                };

                boolean vorhanden;
                //Dopplungen der Fächernamen ausschließen
                for (int j = 0; j < files.length; j++)
                {
                    vorhanden = false;
                    for (String f : fächerNamen)
                    {
                        if (f.equalsIgnoreCase(notes[j][5]))
                            vorhanden = true;
                    }
                    if(!vorhanden)
                        fächerNamen.add(notes[j][5]);
                }





                //Array to Char Sequence
                CharSequence[] cs = fächerNamen.toArray(new CharSequence[fächerNamen.size()]);

                final AlertDialog.Builder builder = new AlertDialog.Builder(statistiken.this);
                builder.setTitle("Wähle ein Fach");

                builder.setItems(cs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        String selectedFach = fächerNamen.get(which);

                        ArrayList<Integer> noten1 = new ArrayList<Integer>();
                        ArrayList<Integer> noten2 = new ArrayList<Integer>();
                        ArrayList<Integer> noten3 = new ArrayList<Integer>();
                        ArrayList<Integer> noten4 = new ArrayList<Integer>();


                        //noten in Arraylist hinzufügen
                        for(int j = 0; j < files.length; j++)
                                if(selectedFach.equalsIgnoreCase(notes[j][5]))
                                {
                                    if(!notes[j][1].equals(""))
                                        noten1.add(Integer.parseInt(notes[j][1]));
                                    if(!notes[j][2].equals(""))
                                        noten2.add(Integer.parseInt(notes[j][2]));
                                    if(!notes[j][3].equals(""))
                                        noten3.add(Integer.parseInt(notes[j][3]));
                                    if(!notes[j][4].equals(""))
                                        noten4.add(Integer.parseInt(notes[j][4]));
                                }


                        float lastNote = 0;
                        int counter = 1;
                        series.addPoint(new ValueLinePoint("", 0f));

                        for(int x : noten1) {
                            series.addPoint(new ValueLinePoint("" + counter, (float) x));
                            lastNote = x;
                            counter++;
                        }
                        for(int x : noten2) {
                            series.addPoint(new ValueLinePoint("" + counter, (float) x));
                            lastNote = x;
                            counter++;
                        }
                        for(int x : noten3) {
                            series.addPoint(new ValueLinePoint("" + counter, (float) x));
                            lastNote = x;
                            counter++;
                        }
                        for(int x : noten4) {
                            series.addPoint(new ValueLinePoint("" + counter, (float) x));
                            lastNote = x;
                            counter++;
                        }

                        series.addPoint(new ValueLinePoint("", lastNote));

                        mCubicValueLineChart.addSeries(series);
                        mCubicValueLineChart.startAnimation();





                    }
                });
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCubicValueLineChart.clearChart();
                        mCubicValueLineChart.setShowIndicator(true);
                        builder.show();
                        series.setSeries(new ArrayList<ValueLinePoint>());

                    }
                });


            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    };

    public String[] Load (File file)
    {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl = 0;
        try {
            while ((test = br.readLine()) != null) {
                anzahl++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fis.getChannel().position(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try {
            while ((line = br.readLine()) != null) {
                array[i] = line;
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }



    public void onFirstStart()
    {


        avgText.setVisibility(View.VISIBLE);
        mLineChart.setVisibility(View.INVISIBLE);
        mPieChart.setVisibility(View.INVISIBLE);
        mBarChart.clearChart();
        mBarChart.setVisibility(View.VISIBLE);


        File[] dirs = new File[mDirs.length + sDirs.length];

        for(int j = 0; j < mDirs.length; j++)
        {
            dirs[j] = mDirs[j];
        }

        int mDirCounter = mDirs.length;

        for(int j = 0; j < sDirs.length; j++)
        {
            dirs[mDirCounter] = sDirs[j];
            mDirCounter++;
        }

        int fileCounter = 0;

        //Anzahl an Dateien herausfinden
        for(int j = 0; j < dirs.length; j++)
        {
            for (int z = 0; z < dirs[j].listFiles().length; z++)
            {
                fileCounter++;
            }
        }

        File[] files = new File[fileCounter];
        fileCounter = 0;
        for(int j = 0; j < dirs.length; j++)
        {
            for (int z = 0; z < dirs[j].listFiles().length; z++)
            {
                files[fileCounter] = dirs[j].listFiles()[z];
                fileCounter++;
            }
        }


        String[][] notes = new String[files.length][FILE_LENGHT];

        BarChart mBarChart = (BarChart) findViewById(R.id.barchart);

        int[] mColors = {
                0xFF123456,
                0xFF56B7F1,
                0xFF1BA4E6,
                0xFF343456,
                0xFF563456,
                0xFF873F56,
                0xFF1FF4AC,
                0xFF343456,
        };







        //Für jedes Fach jedes Halbjahr nach gleichen Noten auslesen und die werte addieren und dann durch die anzahl an noten pro fach teilen

        String[] fächer = new String[ANZAHL_FILES];
        int fächercounter = 0;
        boolean noFile;

        for(int u = 0; u < files.length; u++)
        {
            noFile = true;
            String[] loads = Load(files[u]);
            for(int z = 0; z < fächercounter; z++)
            {
                if(!loads[5].equalsIgnoreCase(fächer[z])) {
                    noFile = true;
                }
                else
                {
                    noFile = false;
                    break;
                }

            }
            if(noFile) {
                fächer[fächercounter] = loads[5];
                fächercounter++;
            }
        }

        float gesamtcounter = 0;
        float gesamtnote = 0;
        int[] fächerNoten = new int[fächercounter];
        int notenCounter[] = new int[fächercounter];
        float[] avg = new float[fächercounter];
        for(int u = 0; u < files.length; u++) {
            notes[u] = Load(files[u]);
            for(int z = 0; z < fächercounter; z++)
            {
                if(notes[u][5].equalsIgnoreCase(fächer[z]))
                {
                    for(int y = 0; y < 4; y++)
                    {
                        if(!notes[u][y+1].equals(""))
                        {
                            fächerNoten[z] += Integer.parseInt(notes[u][y+1]);
                            notenCounter[z]++;

                            if(notes[u][0].equals("lk"))
                            {
                                gesamtcounter += 2;
                                gesamtnote += 2 * Integer.parseInt(notes[u][y+1]);
                            }
                            else
                            {
                                gesamtcounter++;
                                gesamtnote += Integer.parseInt(notes[u][y+1]);
                            }
                        }
                    }
                }
            }
        }

        for(int x = 0; x < fächercounter; x++)
        {
            if(notenCounter[x] != 0)
            {
                avg[x] = fächerNoten[x] / notenCounter[x];
                double fN = fächerNoten[x];
                double nC = notenCounter[x];
                double number = fN / nC;
                int decimal = (int) number;
                double fractional = number - decimal;
                if(fractional >= 0.5)
                    avg[x] += 1;

                if(fächer[x].length() > 7)
                    fächer[x] = fächer[x].substring(0,7);

                mBarChart.addBar(new BarModel(fächer[x], avg[x], mColors[(int) Math.floor(Math.random() * (mColors.length))]));
            }
        }

        float avgGesNote = gesamtnote / gesamtcounter;

        avgGesNote = avgGesNote + (float) 0.005;                   // damit er "richtig" rundet, bei 3 nachkommastellen 0.0005 usw.
        avgGesNote = (int)(avgGesNote*100);               // hier wird der float *100 gerechnet und auf int gecastet, so fallen alle weiteren Nachkommastellen weg
        avgGesNote = avgGesNote/100;

        avgText.setText("Durchschnittliche Gesamtnote: " + avgGesNote);


        mBarChart.startAnimation();










        /*

        mBarChart.setVisibility(View.VISIBLE);


        String[][] notes = new String[files.length][FILE_LENGHT];

        for (int y = 0; y < files.length; y++) {
            notes[y] = Load(files[y]);
        }

        int[] allNotes = new int[NOTE_LENGHT];
        for (int y = 0; y < allNotes.length; y++)
            allNotes[y] = 0;

        for (int y = 0; y < files.length; y++) {
            for (int j = 0; j < 4; j++) {
                for (int z = 0; z < allNotes.length; z++) {
                    if (!notes[y][j + 1].equals(""))
                        if (Integer.parseInt(notes[y][j + 1]) == z + 1)
                            allNotes[z]++;
                }
            }
        }


        BarChart mBarChart = (BarChart) findViewById(R.id.barchart);

        int[] mColorsInt = new int[files.length];

        String[] mColors = {
                "#39add1", // light blue
                "#3079ab", // dark blue
                "#c25975", // mauve
                "#e15258", // red
                "#f9845b", // orange
                "#838cc7", // lavender
                "#7d669e", // purple
                "#53bbb4", // aqua
                "#51b46d", // green
                "#e0ab18", // mustard
                "#637a91", // dark gray
                "#f092b0", // pink
                "#b7c0c7",  // light gray
                "#F5DEB3", //wheat
                "#D2691E",   //chocolate#
                "#000000",   //black
                //Weil MaxFileLenght = 25
                "#39add1", // light blue
                "#3079ab", // dark blue
                "#c25975", // mauve
                "#e15258", // red
                "#f9845b", // orange
                "#838cc7", // lavender
                "#7d669e", // purple
                "#53bbb4", // aqua
                "#51b46d", // green
                "#e0ab18", // mustard
        };

        for(int j = 0; j < files.length; j++)
            mColorsInt[j] = Color.parseColor(mColors[j]);

        for(int j = 0; j < files.length; j++)
        {
            float avg = 0;
            int notenCounter = 0;
            for(int z = 0; z < 4; z++)
            {
                if(!notes[j][z+1].equals(""))
                {
                    avg += Integer.parseInt(notes[j][z+1]);
                    notenCounter++;
                }
            }
            avg = avg / notenCounter;

            if(notes[j][5].length() > 7)
                notes[j][5] = notes[j][5].substring(0,7);

            mBarChart.addBar(new BarModel(notes[j][5], avg, mColorsInt[j]));
        }


        mBarChart.startAnimation();


         */

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeAct.class);
        i.putExtra("rate", true);
        startActivity(i);
    }

    //https://github.com/blackfizz/EazeGraph
}

