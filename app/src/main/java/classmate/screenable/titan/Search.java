package classmate.screenable.titan;

import android.accounts.NetworkErrorException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    String target_api;
    ProgressBar progressBar;
    static ArrayList<String> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressBar=findViewById(R.id.progressbar);

        Spinner type_spinner = (Spinner) findViewById(R.id.type);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> type_adapter = ArrayAdapter.createFromResource(Search.this,
                R.array.resulttype, android.R.layout.simple_spinner_item);
        type_spinner.setAdapter(type_adapter);


        recyclerView = (RecyclerView) findViewById(R.id.video_results);
        SearchView searchView = (SearchView) findViewById(R.id.searchmaterial);
        searchView.setQueryHint("Search Material");
        int id =  searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.w("SPINNER", adapterView.getItemAtPosition(i).toString());
                if(adapterView.getItemAtPosition(i).toString().equals("Video")){
                    target_api="youtube";

                }else {
                    target_api="slideshare";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

// Set search text color
        textView.setTextColor(Color.WHITE);

// Set search hints color
        textView.setHintTextColor(Color.GRAY);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                Log.w("WEIRD","function called");
                items.clear();
                progressBar.setVisibility(View.VISIBLE);
                if(target_api.equals("youtube")){

                    recyclerView.removeAllViews();
                    new SearchYouTube().execute(query);
                }else {

                    recyclerView.removeAllViews();


                    new SearchSlideShare().execute(query);
                }
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
                    Log.w("SLIDE",array.getString(i));
                    items.add(array.getString(i));
                }
            }catch (NetworkErrorException e){
                Toast.makeText(Search.this,"Network error", Toast.LENGTH_LONG).show();
            }catch (SocketTimeoutException e){
                Log.w("SLIDE", e.toString());
            }
            catch (Exception e){
                new ErrorRecorder(Search.this,e,"high");
                Toast.makeText(Search.this,"Unexpected error, we have been notified", Toast.LENGTH_LONG).show();

                Log.w("SLIDESHARE",e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(Search.this));
            try {
                Pair<List<String>, List<String>> pair = new GetCoursesFromPreference(Search.this).codeAndName();
                adapter = new MyRecyclerViewAdapter(Search.this, items, "slideshare",pair.first,pair.second);
//        adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
            }catch (Exception e){
//                handle getting courses failed exception
            }
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

                }

            }catch (NetworkErrorException e){
                Toast.makeText(Search.this,"Network error", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                new ErrorRecorder(Search.this,e,"high");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Search.this,"Unexpected error, we have been notified", Toast.LENGTH_LONG).show();

                    }
                });


            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                Pair<List<String>, List<String>> pair = new GetCoursesFromPreference(Search.this).codeAndName();
                recyclerView.setLayoutManager(new LinearLayoutManager(Search.this));
                adapter = new MyRecyclerViewAdapter(Search.this, items, "youtube",pair.first,pair.second);
//        adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
            }catch (Exception e){
                Log.w("TODOSearch",e.toString());

            }
        }
    }
}
