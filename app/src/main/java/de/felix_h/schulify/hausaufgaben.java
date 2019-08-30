package de.felix_h.schulify;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Scroller;
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
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import me.toptas.fancyshowcase.FancyShowCaseView;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class hausaufgaben extends AppCompatActivity {

    int ANZAHL_SPALTEN = 2;

    int ANZAHL_FILES = 25;

    private int FILE_LENGHT = 3;

    private String[] TABLE_HEADERS = { "Fach", "Hausaufgabe" };

    private String[][] DATA_TO_SHOW = new String[ANZAHL_FILES][FILE_LENGHT];

    FloatingActionButton fab;

    public String path;
    File path_file;

    AlertDialog dialog;
    PendingIntent pendingIntent;

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hausaufgaben);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Schulify/Hausaufgaben/";
        path_file = new File(path);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, hausaufgaben.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        RelativeLayout Layout = findViewById(R.id.hausaufgaben_rl);
        final String Abfragen = Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/";
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
                    tableView.setDataAdapter(new SimpleTableDataAdapter(hausaufgaben.this, DATA_TO_SHOW));
                }
            });


            //TABELLE CLICKLISTENER-------------------------------------------------------------------------------------------------------------------------------------------------------------------

            tableView.addDataClickListener(new TableDataClickListener<String[]>() {
                @Override
                public void onDataClicked(final int rowIndex, final String[] clickedData) {

                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(hausaufgaben.this);
                    final View mView = getLayoutInflater().inflate(R.layout.layout_edit_hausaufgabe, null);

                    final TextView mFName = mView.findViewById(R.id.fName);
                    final EditText haText = mView.findViewById(R.id.ha_text);
                    final CheckBox mCheckBox = mView.findViewById(R.id.lk_CheckBox);
                    Button mAdd = mView.findViewById(R.id.btn_add);
                    Button mDelete = mView.findViewById(R.id.btn_delete);


                    final File file = new File(path + path_file.listFiles()[rowIndex].getName());

                    String[] temp2 = Load(file);
                    String[] cacheAppend;

                    if(temp2.length > FILE_LENGHT)
                        cacheAppend = new String[temp2.length];
                    else
                        cacheAppend = new String[FILE_LENGHT];

                    cacheAppend[0] = temp2[0];
                    cacheAppend[1] = temp2[1];

                    if (cacheAppend.length < 3)
                        cacheAppend[2] = " ";
                    else {
                        for(int i = 2; i < temp2.length; i++)
                        cacheAppend[i] = temp2[i];
                    }

                    mCheckBox.setChecked(!cacheAppend[1].isEmpty() && cacheAppend[1].equals("wichtig"));

                    for(int i = 2; i < cacheAppend.length; i++) {
                        haText.append(cacheAppend[i]);
                        if(!cacheAppend[i].equals("") && i != cacheAppend.length - 1)
                            haText.append("\n");
                    }

                    mFName.setText(cacheAppend[0]);


                    mAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!mFName.getText().toString().isEmpty()) {

                                for (int u = 0; u < 2; u++) {


                                    File file = new File(path + path_file.listFiles()[rowIndex].getName());

                                    String[] loaded = Load(file);

                                    String[] cacheAppend;

                                    if(loaded.length > FILE_LENGHT)
                                        cacheAppend = new String[loaded.length];
                                    else
                                        cacheAppend = new String[FILE_LENGHT];


                                    for(int i = 0; i < cacheAppend.length; i++)
                                    {
                                        cacheAppend[i] = "";
                                    }

                                    cacheAppend[0] = loaded[0];

                                    if (mCheckBox.isChecked())
                                        cacheAppend[1] = "wichtig";
                                    else
                                        cacheAppend[1] = "unwichtig";



                                    String text = haText.getText().toString();
                                    if (!text.isEmpty())
                                    {
                                        /*
                                        String[] parts;
                                        if(text.contains(System.getProperty("line.separator")))
                                        {
                                            parts = text.split(System.getProperty("line.separator"));
                                            for(int i = 1; i < parts.length; i++)
                                                cacheAppend[2] += parts[i];
                                        }
                                        else
                                            cacheAppend[2] = text;
                                       */
                                        cacheAppend[2] = text;
                                    }
                                    else
                                        cacheAppend[2] = "";


                                    Save(file, cacheAppend);

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

                            for(int i = 0; i < FILE_LENGHT; i++)
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
                        return tableWidthInPx / 3 * 2;
                }
            };

            tableView.setColumnModel(tableColumnModel);

            Button mShowDialog = findViewById(R.id.btnShowDialog);


            //FACH HINZUFÜGEN CLICKLISTENER
            mShowDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(hausaufgaben.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_add_ha, null);

                    final EditText mFName = mView.findViewById(R.id.fName);
                    final CheckBox mLK = mView.findViewById(R.id.lk);
                    Button mAdd = mView.findViewById(R.id.btn_add);

                    mAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!mFName.getText().toString().isEmpty() && !mFName.getText().toString().contains("\n")) {
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


                                    String[] cacheAppend;

                                    if(Load(file).length > FILE_LENGHT)
                                        cacheAppend = new String[Load(file).length];
                                    else
                                        cacheAppend = new String[FILE_LENGHT];


                                    for (int i = 1; i < cacheAppend.length; i++)
                                        cacheAppend[i] = "";

                                    cacheAppend[0] = mFName.getText().toString();

                                    if (mLK.isChecked())
                                        cacheAppend[1] = "wichtig";
                                    else
                                        cacheAppend[1] = "unwichtig";

                                    Save(file, cacheAppend);

                                    fab.callOnClick();
                                    updateTable();

                                    dialog.cancel();


                                    String showCaseView = "hausaufgabentabelle";
                                    File showCaseFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "//Schulify/Abfragen/showcase/" + showCaseView);
                                    if (!showCaseFile.exists()) {
                                        showCaseFile.mkdir();
                                        showShowCaseView("Klicke auf die Tabelle, um Hausaufgaben hinzuzufügen oder zu bearbeiten\n\n\n");
                                    }

                                } else
                                    Toast.makeText(hausaufgaben.this, "Maximum von " + ANZAHL_FILES + " Hausaufgaben erreicht!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(hausaufgaben.this, "Bitte gültiges Fach angeben!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mBuilder.setView(mView);
                    dialog = mBuilder.create();
                    dialog.show();
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

        String[] temp;
        if(array.length > FILE_LENGHT)
            temp = new String[array.length];
        else
            temp = new String[FILE_LENGHT];

        for(int j = 0; j < FILE_LENGHT; j++)
            temp[j] = "";
        for(int j = 0; j < array.length; j++)
            temp[j] = array[j];

        return temp;
    }

    //UPDATE TABLE
    public void updateTable() {
        if(path_file.listFiles().length > 0)
        for (int i = 0; i < path_file.listFiles().length; i++) {

            DATA_TO_SHOW[i][0] = Load(path_file.listFiles()[i])[0];

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

            String[] temp;
            if(array.length > FILE_LENGHT)
                temp = new String[array.length];
            else
                temp = new String[FILE_LENGHT];

            if(array.length == 0)
            {
                for(int u = 0; u < FILE_LENGHT; u++)
                    temp[u] = "";
            }
            else
            {
                for(int u = 0; u < array.length; u++)
                    temp[u] = array[u];
            }

            for(int y = 1; y < FILE_LENGHT; y++)
                DATA_TO_SHOW[i][y] = "";

            if(array[1].equals("wichtig"))
                DATA_TO_SHOW[i][1] += "!!!     ";
            for(int w = 2; w < array.length; w++)
                DATA_TO_SHOW[i][1] += temp[w];
        }
    }


    public void sort()
    {
        int[] sorted = new int[path_file.listFiles().length];
        for(int i = 0; i < path_file.listFiles().length; i++)
            path_file.listFiles()[i].renameTo(new File(path + i + ".txt"));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeAct.class);
        i.putExtra("rate", true);
        startActivity(i);
    }
}
