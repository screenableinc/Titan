package classmate.screenable.titan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private CircleImageView profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        profile_image = findViewById(R.id.profile_image);

        SharedPreferences preferences = getSharedPreferences("credentials",MODE_PRIVATE);
        File path = new File(Globals.profile_folder+File.separator+preferences.getString(Globals.id_keyName,"")+".jpg");
        if(path.exists()){

//            Picasso.get().load(path).resize(50,50).onlyScaleDown().into(profile_image);
            Bitmap bitmapImage = BitmapFactory.decodeFile(path.toString());
            if(bitmapImage==null){
                profile_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp));
            }else{
//            int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 50, 50, true);
            profile_image.setImageBitmap(scaled);}
        }else {
            new DownloadProfileImage().execute();
        }

//        String program = preferences.getString("program",null);
//        while (program.startsWith(" ")){
//            program=program.substring(1);
//        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        LinearLayout goto_search = (LinearLayout) findViewById(R.id.search_go);
        LinearLayout goto_library = (LinearLayout) findViewById(R.id.go_to_library);

        goto_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setBackgroundResource(R.color.colorWarning);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        view.setBackgroundResource(R.color.colorAccent);
                        Intent mainIntent = new Intent(MainActivity.this,LibraryActivity.class);

                        MainActivity.this.startActivity(mainIntent);

                    }
                }, 50);

            }
        });
        goto_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setBackgroundResource(R.color.colorWarning);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        view.setBackgroundResource(R.color.colorAccent);
                        Intent mainIntent = new Intent(MainActivity.this,Search.class);

                        MainActivity.this.startActivity(mainIntent);

                    }
                }, 50);


            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPosition;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.w("TESTTT","position: "+"\n posoffset: "+positionOffset+"\n offsetpix:"+ positionOffsetPixels);
                Performance fragment = (Performance)mSectionsPagerAdapter.instantiateItem(mViewPager, 1);
                if(positionOffset>0.99f){
                    if (fragment != null) {

                        fragment.onFragmentEntered();
                    }
                    lastPosition = position;
                }


            }

            @Override
            public void onPageSelected(int position) {


                }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Home();
                case 1:
                    return new Performance();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Home";
                case 1:
                    return "Performance";
            }
            return null;
        }
    }

    private class DownloadProfileImage extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            new classmate.screenable.titan.DownloadProfileImage(MainActivity.this).download(profile_image);
            return null;
        }
    }


}
