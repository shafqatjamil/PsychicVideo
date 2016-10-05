package avreye.mytarotadvisor.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import avreye.mytarotadvisor.R;
import avreye.mytarotadvisor.adapter.CustomPagerAdapter;

public class TutorialActivity extends AppCompatActivity {
    int selectedIndex= 0;
    boolean mPageEnd;
    ImageButton imageButton_left;
    ImageButton imageButton_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        imageButton_left = (ImageButton) findViewById(R.id.tutorial_screen_left_button);
        imageButton_right = (ImageButton) findViewById(R.id.tutorial_screen_right_button);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(this);
        viewPager.setAdapter(customPagerAdapter);

        mPageEnd = false;
        ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                selectedIndex = arg0;

            }
            boolean callHappened;
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                if( mPageEnd && arg0 == customPagerAdapter.getCount()-1 && !callHappened){
                    Log.d(getClass().getName(), "Okay");
                    mPageEnd = false;//To avoid multiple calls.
                    callHappened = true;
                    Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
                    TutorialActivity.this.startActivity(intent);
                    TutorialActivity.this.finish();
                }else{
                    mPageEnd = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                if(selectedIndex == customPagerAdapter.getCount() - 1)
                {
                    mPageEnd = true;
                }
            }
        };
        viewPager.setOnPageChangeListener(mListener);
        imageButton_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewPager.getCurrentItem() == viewPager.getChildCount())
                {
                    Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
                    TutorialActivity.this.startActivity(intent);
                    TutorialActivity.this.finish();
                }
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
            }
        });
        imageButton_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1,true);
            }
        });
    }
}
