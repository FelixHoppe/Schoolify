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

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {

        this.context = context;

    }


     //Arrays
    public int[] slide_images = {

             R.drawable.sheets,
             R.drawable.calendar_menu,
             R.drawable.graph


     };

    public String[] slide_headings = {

            "Hausaufgaben",
            "Stundenplan",
            "Noten"

    };

    public String[] slide_descs = {

            "Das mobile Hausaufgabenheft mit eingebauter Erinnerungsfunktion, durch die du nie wieder deine Hausaufgaben vergessen wirst!",
            "Behalte den Überblick über deine Fächer mit dem einfachen Stundenplan.",
            "Trage deine Noten ein. Die App berechnet dann daraus deinen Durchschnitt, die Notenverteilung und deine Erfolgskurve."
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
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);


        slideImageView.setImageResource(slide_images[position]);
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
