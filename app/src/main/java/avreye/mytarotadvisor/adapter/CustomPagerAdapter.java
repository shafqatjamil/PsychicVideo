package avreye.mytarotadvisor.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import avreye.mytarotadvisor.R;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    int[] image_resources = {R.drawable.ts1,R.drawable.ts2,R.drawable.ts3,R.drawable.ts4};
    private  LayoutInflater layoutInflater;
    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        layoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view =  layoutInflater.inflate(R.layout.swipe_layout, collection, false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.swipe_layout_image);

        imageView.setImageResource(image_resources[position]);
        collection.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((LinearLayout) view);
    }

    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



}