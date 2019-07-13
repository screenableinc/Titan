package classmate.screenable.titan;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Search extends AppCompatActivity {
    MyRecyclerViewAdapter adapter;
    ArrayList<String> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = (SearchView) findViewById(R.id.searchmaterial);
        searchView.setQuery("Search Material",false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.w("TODO",query+" see");

//                new SearchYouTube().execute(query);
                new SearchSlideShare().execute(query);

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.video_results);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, items);
//        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }
    public static class SearchSlideShare extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            try{
                new SlideShare().access(strings[0]);
            }catch (Exception e){
                Log.w("SLIDESHARE",e.toString());
            }

            return null;
        }
    }
    public static class SearchYouTube extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject result=  new YouTube().access(strings[0]);
                Log.w("TODO","gere"+result.toString());
                JSONArray arr_items = result.getJSONArray("items");
                for (int i = 0;i<arr_items.length();i++){
                    arr_items.put(arr_items.get(i));
                    Log.w("5TODO",arr_items.get(i).toString());
                }

            }catch(Exception e){
                Log.w("TODO",e.toString());
            }
            return null;
        }
    }
}
