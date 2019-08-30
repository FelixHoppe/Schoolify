package de.felix_h.schulify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class Jahr extends AppCompatActivity implements View.OnClickListener{

    private CardView n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jahr);

        n1 = (CardView) findViewById(R.id.n1);
        n2 = (CardView) findViewById(R.id.n2);
        n3 = (CardView) findViewById(R.id.n3);
        n4 = (CardView) findViewById(R.id.n4);
        n5 = (CardView) findViewById(R.id.n5);
        n6 = (CardView) findViewById(R.id.n6);

        n7 = (CardView) findViewById(R.id.n7);
        n8 = (CardView) findViewById(R.id.n8);
        n9 = (CardView) findViewById(R.id.n9);
        n10 = (CardView) findViewById(R.id.n10);
        n11 = (CardView) findViewById(R.id.n11);
        n12 = (CardView) findViewById(R.id.n12);
        n13 = (CardView) findViewById(R.id.n13);

        n1.setOnClickListener(this);
        n2.setOnClickListener(this);
        n3.setOnClickListener(this);
        n4.setOnClickListener(this);
        n5.setOnClickListener(this);
        n6.setOnClickListener(this);
        n7.setOnClickListener(this);
        n8.setOnClickListener(this);
        n9.setOnClickListener(this);
        n10.setOnClickListener(this);
        n11.setOnClickListener(this);
        n12.setOnClickListener(this);
        n13.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
            case R.id.n1 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 1);
                startActivity(i); break;
            case R.id.n2 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 2);
                startActivity(i); break;
            case R.id.n3 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 3);
                startActivity(i); break;
            case R.id.n4 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 4);
                startActivity(i); break;
            case R.id.n5 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 5);
                startActivity(i); break;
            case R.id.n6 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 6);
                startActivity(i); break;
            case R.id.n7 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 7);
                startActivity(i); break;
            case R.id.n8 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 8);
                startActivity(i); break;
            case R.id.n9 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 9);
                startActivity(i); break;
            case R.id.n10 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 10);
                startActivity(i); break;
            case R.id.n11 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 11);
                startActivity(i); break;
            case R.id.n12 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 12);
                startActivity(i); break;
            case R.id.n13 :
                i = new Intent(this, HomeAct.class);
                i.putExtra("JAHR", 13);
                startActivity(i); break;
            default: break;
        }

        //Weiter bei https://youtu.be/d6CfaWW7G5Q?t=5m40s


    }
}
