package gq.altafchaudhari.cowatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import gq.altafchaudhari.cowatch.R;

public class SliderAdapter extends PagerAdapter {


    private Context context;
    private List<String> captionList;

    public SliderAdapter(Context context, List<String> captionList) {
        this.context = context;

        this.captionList = captionList;
    }

    @Override
    public int getCount() {
        return captionList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_text_slider, null);

        TextView textView = view.findViewById(R.id.ttv_text);

        textView.setText(captionList.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}