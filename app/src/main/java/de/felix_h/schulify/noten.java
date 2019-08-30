package de.felix_h.schulify;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class noten extends AppCompatActivity {

    int JAHR;

    int ANZAHL_SPALTEN = 5;

    int ANZAHL_FILES = 25;

    private int FILE_LENGHT = 6;

    private String[] TABLE_HEADERS = {"Fach", "1", "2", "3", "4"};

    private String[][] DATA_TO_SHOW = new String[ANZAHL_FILES][ANZAHL_SPALTEN];

    FloatingActionButton fab;

    public String path;
    File path_file;


    AlertDialog dialog;


    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noten);




        RelativeLayout Layout = findViewById(R.id.noten_rl);
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




        //Aktuelles Jahr auslesen

        File AktJahrPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/JahrAuswahl/");

        /*
        File tempFile1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/schriftlich/");
        if(!tempFile1.exists())
            tempFile1.mkdir();

        File tempFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Noten/muendlich/");
        if(!tempFile2.exists())
            tempFile2.mkdir();

         */


        File[] AktJahr = AktJahrPath.listFiles();
        JAHR = Integer.parseInt(AktJahr[0].getName().toString());


        boolean schriftlich = (getIntent().getStringExtra("info").equals("schriftlich"));


        if (schriftlich)
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Noten/schriftlich/" + JAHR + "/";
        else
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Noten/muendlich/" + JAHR + "/";


        path_file = new File(path);

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        updateTable();


        fab = findViewById(R.id.fab);
        final TableView<String[]> tableView = findViewById(R.id.tableView);

        if (tempFile.exists()) {
            if(! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == Color.rgb(252, 252, 252)) && ! (Integer.parseInt(tempFile.listFiles()[0].getName().toString()) == 0))
                tableView.setHeaderBackgroundColor(Integer.parseInt(tempFile.listFiles()[0].getName().toString()));
            else
                tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        }
        else
        {
            tableView.setHeaderBackgroundColor(Color.parseColor("#2ecc71"));
        }

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        tableView.setColumnCount(ANZAHL_SPALTEN);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableView.setDataAdapter(new SimpleTableDataAdapter(noten.this, DATA_TO_SHOW));
            }
        });


        //TABELLE CLICKLISTENER
        tableView.addDataClickListener(new TableDataClickListener<String[]>() {
            @Override
            public void onDataClicked(final int rowIndex, final String[] clickedData) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(noten.this);
                View mView = getLayoutInflater().inflate(R.layout.layout_edit_note, null);

                final EditText mFName = mView.findViewById(R.id.fName);
                final EditText mFNumber1 = mView.findViewById(R.id.number1);
                final EditText mFNumber2 = mView.findViewById(R.id.number2);
                final EditText mFNumber3 = mView.findViewById(R.id.number3);
                final EditText mFNumber4 = mView.findViewById(R.id.number4);

                final CheckBox mCheckBox = mView.findViewById(R.id.lk_CheckBox);
                Button mAdd = mView.findViewById(R.id.btn_add);
                Button mDelete = mView.findViewById(R.id.btn_delete);

                String toSplit = clickedData[0];
                final String splitted;
                if (toSplit.contains("."))
                    splitted = toSplit.substring(0, toSplit.lastIndexOf('.'));
                else
                    splitted = toSplit;

                //mFName.setText(splitted);
                mFName.setText(splitted);

                final File file = new File(path + path_file.listFiles()[rowIndex].getName());

                mFNumber1.setHint("Note 1");
                mFNumber2.setHint("Note 2");
                mFNumber3.setHint("Note 3");
                mFNumber4.setHint("Note 4");

                String[] cacheAppend = Load(file);

                mCheckBox.setChecked(!cacheAppend[0].isEmpty() && cacheAppend[0].equals("lk"));
                mFNumber1.setText(cacheAppend[1]);
                mFNumber2.setText(cacheAppend[2]);
                mFNumber3.setText(cacheAppend[3]);

                if (cacheAppend.length > 4)
                    mFNumber4.setText(cacheAppend[4]);


                mAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!mFName.getText().toString().isEmpty()) {

                            for (int u = 0; u < 2; u++) {


                                File file = new File(path + path_file.listFiles()[rowIndex].getName());
                                if (!file.exists())
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                String[] cacheAppend = new String[FILE_LENGHT];
                                String[] loaded = Load(file);


                                cacheAppend[0] = "";
                                cacheAppend[1] = loaded[0];
                                cacheAppend[2] = loaded[1];
                                cacheAppend[3] = loaded[2];
                                cacheAppend[4] = loaded[3];
                                cacheAppend[5] = loaded[5];


                                if (mCheckBox.isChecked())
                                    cacheAppend[0] = "lk";
                                else
                                    cacheAppend[0] = "gk";


                                boolean toast = true;
                                if (!mFNumber1.getText().toString().isEmpty() && !(mFNumber1.getText().toString().length() > 3) && isInteger(mFNumber1.getText().toString()) && Integer.parseInt(mFNumber1.getText().toString()) <= 15 && Integer.parseInt(mFNumber1.getText().toString()) > 0) {
                                    cacheAppend[1] = mFNumber1.getText().toString();
                                    Save(file, cacheAppend);
                                    toast = false;
                                } else {
                                    if (mFNumber1.getText().toString().isEmpty() || (mFNumber1.getText().toString().length() > 3) || !(Integer.parseInt(mFNumber1.getText().toString()) <= 15) || !(Integer.parseInt(mFNumber1.getText().toString()) > 0))
                                    cacheAppend[1] = "";
                                    Save(file, cacheAppend);
                                }

                                if (!mFNumber2.getText().toString().isEmpty() && !(mFNumber2.getText().toString().length() > 3) && isInteger(mFNumber2.getText().toString()) && Integer.parseInt(mFNumber2.getText().toString()) <= 15 && Integer.parseInt(mFNumber2.getText().toString()) > 0) {
                                    cacheAppend[2] = mFNumber2.getText().toString();
                                    Save(file, cacheAppend);
                                    toast = false;
                                } else {
                                    if (mFNumber2.getText().toString().isEmpty() || (mFNumber2.getText().toString().length() > 3) || !(Integer.parseInt(mFNumber2.getText().toString()) <= 15) || !(Integer.parseInt(mFNumber2.getText().toString()) > 0))
                                    cacheAppend[2] = "";
                                    Save(file, cacheAppend);
                                }

                                if (!mFNumber3.getText().toString().isEmpty() && !(mFNumber3.getText().toString().length() > 3) && isInteger(mFNumber3.getText().toString()) && Integer.parseInt(mFNumber3.getText().toString()) <= 15 && Integer.parseInt(mFNumber3.getText().toString()) > 0) {
                                    cacheAppend[3] = mFNumber3.getText().toString();
                                    Save(file, cacheAppend);
                                    toast = false;
                                } else {
                                    if (mFNumber3.getText().toString().isEmpty() || (mFNumber3.getText().toString().length() > 3) || !(Integer.parseInt(mFNumber3.getText().toString()) <= 15) || !(Integer.parseInt(mFNumber3.getText().toString()) > 0))
                                    cacheAppend[3] = "";
                                    Save(file, cacheAppend);
                                }

                                if (!mFNumber4.getText().toString().isEmpty() && !(mFNumber4.getText().toString().length() > 3) && isInteger(mFNumber4.getText().toString()) && Integer.parseInt(mFNumber4.getText().toString()) <= 15 && Integer.parseInt(mFNumber4.getText().toString()) > 0) {
                                    cacheAppend[4] = mFNumber4.getText().toString();
                                    Save(file, cacheAppend);
                                    toast = false;
                                } else {
                                    if (mFNumber4.getText().toString().isEmpty() || (mFNumber4.getText().toString().length() > 3) || !(Integer.parseInt(mFNumber4.getText().toString()) <= 15) || !(Integer.parseInt(mFNumber4.getText().toString()) > 0))
                                    cacheAppend[4] = "";
                                    Save(file, cacheAppend);
                                }

                                if(toast)
                                    Toast.makeText(noten.this, "Bitte gib eine gültige Note ein!", Toast.LENGTH_SHORT).show();

                                fab.callOnClick();
                                updateTable();


                            }

                            dialog.cancel();

                        }
                    }
                });


                mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        for (int i = 0; i < ANZAHL_SPALTEN; i++)
                            DATA_TO_SHOW[path_file.listFiles().length - 1][i] = "";

                        file.delete();
                        updateTable();
                        fab.callOnClick();

                        dialog.cancel();
                        sort();
                    }
                });


                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();


                updateTable();
                //-------------------------------------------------
            }

            public <T> T[] append(T[] arr, T element) {
                final int N = arr.length;
                arr = Arrays.copyOf(arr, N + 1);
                arr[N] = element;
                return arr;
            }

            public void Save(File file, String[] data) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    try {
                        String[] existingText = Load(file);
                        for (int i = 0; i < existingText.length; i++)
                            fos.write(existingText[i].getBytes());

                        for (int i = 0; i < data.length; i++) {
                            fos.write(data[i].getBytes());
                            if (i < data.length - 1) {
                                fos.write("\n".getBytes());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public String[] Load(File file) {
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
        });

        tableView.setDataAdapter(new SimpleTableDataAdapter(this, DATA_TO_SHOW));
        tableView.setColumnCount(ANZAHL_SPALTEN);


        TableColumnModel tableColumnModel = new TableColumnModel() {
            @Override
            public int getColumnCount() {
                return ANZAHL_SPALTEN;
            }

            @Override
            public void setColumnCount(int columnCount) {
                ANZAHL_SPALTEN = columnCount;
            }

            @Override
            public int getColumnWidth(int columnIndex, int tableWidthInPx) {
                if (columnIndex == 0)
                    return tableWidthInPx / 3;
                else
                    return ((tableWidthInPx / 3) * 2) / (ANZAHL_SPALTEN - 1);
            }
        };

        tableView.setColumnModel(tableColumnModel);

        Button mShowDialog = findViewById(R.id.btnShowDialog);


        //FACH HINZUFÜGEN CLICKLISTENER
        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(noten.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add, null);

                final EditText mFName = mView.findViewById(R.id.fName);
                final CheckBox mLK = mView.findViewById(R.id.lk);
                Button mAdd = mView.findViewById(R.id.btn_add);

                mAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mFName.getText().toString().isEmpty() && !mFName.getText().toString().contains("\n")) {
                            boolean vorhanden = false;

                            while (mFName.getText().toString().substring(mFName.getText().toString().length() - 1).equals(" "))
                                if (mFName.getText().toString().substring(mFName.getText().toString().length() - 1).equals(" "))
                                    mFName.setText(mFName.getText().toString().substring(0, mFName.getText().toString().length() - 1));

                            for (int i = 0; i < path_file.listFiles().length; i++) {
                                if (Load(path_file.listFiles()[i])[5].equals(mFName.getText().toString())) {
                                    vorhanden = true;
                                    break;
                                }
                            }
                            if (!vorhanden) {
                                if (path_file.listFiles().length < ANZAHL_FILES) {

                                    File file;
                                    if (path_file.listFiles().length != 0) {
                                        String splitted = path_file.listFiles()[path_file.listFiles().length - 1].getName().substring(0, path_file.listFiles()[path_file.listFiles().length - 1].getName().lastIndexOf('.'));
                                        int temp = Integer.parseInt(splitted) + 1;
                                        file = new File(path + temp + ".txt");
                                    } else
                                        file = new File(path + 0 + ".txt");


                                    if (!file.exists())
                                        try {
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    if (mLK.isChecked()) {
                                        String[] cacheAppend = new String[FILE_LENGHT];
                                        for (int i = 1; i < cacheAppend.length; i++)
                                            cacheAppend[i] = "";
                                        cacheAppend[0] = "lk";
                                        cacheAppend[5] = mFName.getText().toString();
                                        Save(file, cacheAppend);
                                    } else {
                                        String[] cacheAppend = new String[FILE_LENGHT];
                                        for (int i = 1; i < cacheAppend.length; i++)
                                            cacheAppend[i] = "";
                                        cacheAppend[0] = "gk";
                                        cacheAppend[5] = mFName.getText().toString();
                                        Save(file, cacheAppend);
                                    }

                                    fab.callOnClick();
                                    updateTable();

                                    dialog.cancel();



                                    String showCaseView = "notentabelle";
                                    File showCaseFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/showcase/" + showCaseView);
                                    if (!showCaseFile.exists()) {
                                        showCaseFile.mkdir();
                                        showShowCaseView("Klicke auf die Tabelle, um Noten hinzuzufügen oder zu bearbeiten\n\n\n");
                                    }


                                } else
                                    Toast.makeText(noten.this, "Maximum von " + ANZAHL_FILES + " Fächern erreicht!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(noten.this, "Fach existiert bereits!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(noten.this, "Bitte gültiges Fach angeben!", Toast.LENGTH_SHORT).show();
                    }
                });
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
            }


            public void Save(File file, String[] data) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    try {
                        String[] existingText = Load(file);
                        for (int i = 0; i < existingText.length; i++)
                            fos.write(existingText[i].getBytes());

                        for (int i = 0; i < data.length; i++) {
                            fos.write(data[i].getBytes());
                            if (i < data.length - 1) {
                                fos.write("\n".getBytes());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public String[] Load(File file) {
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
        });


        String showCaseView = "notenhausaufgabenbtn";
        File showCaseFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/showcase/" + showCaseView);
        if (!showCaseFile.exists()) {
            showCaseFile.mkdir();
            showShowCaseView(findViewById(R.id.btnShowDialog), "Hier kannst du ein neues Fach hinzufügen");
        }



    }

    private void showShowCaseView(View view, String text) {
        new FancyShowCaseView.Builder(this)
                .title(text)
                .focusOn(view)
                .build()
                .show();
    }

    private void showShowCaseView(String text) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        new FancyShowCaseView.Builder(this)
                .title(text)
                .focusRectAtPosition(width / 2, (int) ( height / 5.4857), width, (int) (height / 19.2))
                .build()
                .show();
    }



    //UPDATE TABLE
    public void updateTable() {
        for (int i = 0; i < path_file.listFiles().length; i++) {

            String toSplit = path_file.listFiles()[i].getName();

            if (path_file.listFiles().length != 0)
                DATA_TO_SHOW[i][0] = Load(path_file.listFiles()[i])[5];

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(path_file.listFiles()[i]);
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
            int j = 0;
            try {
                while ((line = br.readLine()) != null) {
                    array[j] = line;
                    j++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int y = 1; y < ANZAHL_SPALTEN; y++)
                DATA_TO_SHOW[i][y] = "";

            for (int z = 1; z < array.length - 1; z++) {            //Bei fehlern mit + 1, -1 variieren
                DATA_TO_SHOW[i][z] = array[z];
            }
        }
    }

    public void Save(File file, String[] data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                String[] existingText = Load(file);
                for (int i = 0; i < existingText.length; i++)
                    fos.write(existingText[i].getBytes());

                for (int i = 0; i < data.length; i++) {
                    fos.write(data[i].getBytes());
                    if (i < data.length - 1) {
                        fos.write("\n".getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] Load(File file) {
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

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public void sort() {
        int[] sorted = new int[path_file.listFiles().length];
        for (int i = 0; i < path_file.listFiles().length; i++)
            path_file.listFiles()[i].renameTo(new File(path + i + ".txt"));
    }


}
