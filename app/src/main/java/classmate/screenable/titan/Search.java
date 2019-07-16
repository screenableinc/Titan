package classmate.screenable.titan;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Search extends AppCompatActivity {
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    static ArrayList<String> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView = (RecyclerView) findViewById(R.id.video_results);
        SearchView searchView = (SearchView) findViewById(R.id.searchmaterial);
        searchView.setQueryHint("Search Material");
        int id =  searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);

// Set search text color
        textView.setTextColor(Color.WHITE);

// Set search hints color
        textView.setHintTextColor(Color.GRAY);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.w("TODO",query+" see");

                new SearchYouTube().execute(query);
//                new SearchSlideShare().execute(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                make suggestioins
            }
        });


    }
    public class SearchSlideShare extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            try{
                JSONArray array = new SlideShare().access(strings[0]);
                for (int i = 0; i < array.length(); i++) {
                    items.add(array.getString(i));
                }
            }catch (Exception e){

                Log.w("SLIDESHARE",e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new MyRecyclerViewAdapter(getApplicationContext(), items,"slideshare");
//        adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }
    public class SearchYouTube extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject result=  new YouTube().access(strings[0]);
                Log.w("TODO","gere"+result.toString());
                JSONArray arr_items = result.getJSONArray("items");
                Log.w("TODO",arr_items.length()+"ppp");
                for (int i = 0;i<arr_items.length();i++){
                    items.add(arr_items.get(i).toString());
                    Log.w("5TODO",arr_items.get(i).toString());
                }

            }catch(Exception e){
                Log.w("TODO",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter = new MyRecyclerViewAdapter(getApplicationContext(), items,"youtube");
//        adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }
}
