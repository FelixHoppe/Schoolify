package de.felix_h.schulify;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by felix on 08.02.2018.
 */

public class Statistiken_SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public Statistiken_SliderAdapter(Context context) {

        this.context = context;

    }


    //Arrays
    public int[] slide_images = {

            //R.drawable.calendar_menu,
            //R.drawable.graph,
            //R.drawable.sheets

    };

    public String[] slide_headings = {

            "Notendurchschnitt",
            "Erfolgskurve",
            "Notenverteilung"

    };

    public String[] slide_descs = {

            "Hier siehst du die Durchschnittliche Note des jeweiligen Faches. Wische im Diagramm nach links um (falls vorhanden) weitere Fächer zu sehen.",
            "Wähle ein Fach aus, dann klicke auf den Graphen um die einzelnen Werte abzulesen. Bei großer Anzahl an Noten kannst du in den Graphen heran zoomen.",
            "In der Mitte siehst du die Anzahl der unten angezeigten Note. Durch Rotation kannst du die Anzahl der einzelnen Noten abrufen."
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.statistiken_slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);


        //slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout)object);

    }

}
