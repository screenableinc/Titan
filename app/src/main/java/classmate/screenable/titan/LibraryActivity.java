package classmate.screenable.titan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class LibraryActivity extends AppCompatActivity {

/*    TODO
        in future....update to use recyclerview
 */
    private Spinner courses;
    public static String selected_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        courses = (Spinner) findViewById(R.id.course);
        tabs.setupWithViewPager(viewPager);

        try {
            Pair<List<String>, List<String>> pair = new GetCoursesFromPreference(LibraryActivity.this).codeAndName();
            final List<String> course_codes =pair.first;
            final ArrayAdapter<String> time_adapter = new ArrayAdapter<String>(LibraryActivity.this,android.R.layout.simple_spinner_dropdown_item,course_codes);
            time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            courses.setAdapter(time_adapter);
            courses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_course=course_codes.get(i);
                    VideosFragment videosFragment = (VideosFragment) sectionsPagerAdapter.instantiateItem(viewPager, 1);
                    DocumentFragment documentFragment = (DocumentFragment) sectionsPagerAdapter.instantiateItem(viewPager, 0);
                    AssignmentsFragment assignmentsFragment = (AssignmentsFragment) sectionsPagerAdapter.instantiateItem(viewPager, 2);
                    PastPapersFragment pastPapersFragment = (PastPapersFragment) sectionsPagerAdapter.instantiateItem(viewPager, 3);
                    videosFragment.LoadFromActivity();pastPapersFragment.LoadFromActivity();documentFragment.LoadFromActivity();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }catch (Exception e){
//            TODO log error
            Toast.makeText(LibraryActivity.this,"failed to make spinner, developer notified", Toast.LENGTH_LONG).show();
        }


        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Bundle bundle = new Bundle();
            switch (position){

                case 0:

                    return new DocumentFragment();
                case 1:
                    return new VideosFragment();
                case 2:
                    return new AssignmentsFragment();
                case 3:
                    return new PastPapersFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                case 0:

                    return "Documents";
                case 1:
                    return "Videos";
                case 2:
                    return "Assignments";
                case 3:
                    return "Past Papers";


            }
            return null;
        }
    }
}