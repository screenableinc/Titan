package classmate.screenable.titan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    AlertDialog.Builder builder;
    Context context;
    String _source;
    List<String> course_names;
    List<String> course_codes;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<String> data, String source, List<String> course_names,List<String> course_codes) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this._source=source;
        this.course_codes=course_codes;
        this.course_names=course_names;


    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.search_result_template, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        try {
            JSONObject item = new JSONObject(mData.get(position));
            if(_source.equals("youtube")) {
                JSONObject snippet = item.getJSONObject("snippet");
                String id  = item.getJSONObject("id").getString("videoId");


                String title = snippet.getString("title");
                String thumb_url = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                holder.title.setText(title);
                holder.title_and_shii.setTag(id);
                holder.source.setImageDrawable(context.getResources().getDrawable(R.drawable.youtube_logo));

                Picasso.get().load(thumb_url).into(holder.thumbnail);
            }else if (_source.equals("slideshare")){
                holder.title.setText(item.getString("title"));
                Log.w("SLIDE",item.getString("url"));

                holder.source.setImageDrawable(context.getResources().getDrawable(R.drawable.slideshare_logosmall));
                Picasso.get().load("http:"+item.getString("thumbnailurl").replaceAll("\\\\","")).into(holder.thumbnail);

            }

            holder.options.setTag(item);
            holder.options.setImageDrawable(context.getResources().getDrawable(R.drawable.add_to));

//            holder.myTextView.setText(animal);
            }catch (Exception e){
                Log.w("TODO",e.toString());
            }

    }


    private void createDialog(final Object tag, final View view){

        builder=new AlertDialog.Builder(context).setItems(course_names.toArray(new String[course_names.size()]), new DialogInterface.OnClickListener() {
            String category="";
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    SharedPreferences.Editor editor = context.getSharedPreferences("library", Context.MODE_PRIVATE).edit();
                    SharedPreferences preferences = context.getSharedPreferences("library", Context.MODE_PRIVATE);


                    if (_source.equals("youtube")) {
                        category = Globals.CATEGORY_VID_SHAREDPREF_KEY_NAME;
                        JSONArray array=new JSONArray(preferences.getString(category,"[]"));
                        JSONObject object = new JSONObject(tag.toString());
                        object.put("course",course_names.get(i));
                        array.put(object);

                        editor.putString(category, array.toString());
                        editor.commit();
                    }else {
                        category = Globals.CATEGORY_SLIDE_SHAREDPREF_KEY_NAME;
//                        load async task and save to database
                        view.setVisibility(View.GONE);
                        JSONObject object = new JSONObject(tag.toString());
                        object.put("course",course_names.get(i));
                        new SaveToDb((ImageView) view).execute(object.toString());
                        Log.w("CC",object.toString());
                    }


                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
        });
        builder.create().show();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        LinearLayout title_and_shii;
        ImageView options;
        ImageView source;
        ImageView thumbnail;
        ViewHolder(View itemView) {
            super(itemView);

            title_and_shii = (LinearLayout) itemView.findViewById(R.id.clickable);
            thumbnail=(ImageView) itemView.findViewById(R.id.thumbnail);
            options=(ImageView) itemView.findViewById(R.id.options);
            source=(ImageView) itemView.findViewById(R.id.source);
            title = (TextView) itemView.findViewById(R.id.title);
            title_and_shii.setOnClickListener(this);
            options.setOnClickListener(this);


        }



        @Override
        public void onClick(View view) {

//            go to video view
            if (view.getId()==R.id.clickable){
                context.startActivity(new Intent(context,VideoWatch.class).putExtra("videoId",view.getTag().toString()));
            }else if (view.getId()==R.id.options){
                Log.w("TODO","Should have started");
                createDialog(view.getTag(), view);

            }

            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    private class SaveToDb extends AsyncTask<Object,Integer,String>{
        ImageView icon;
        private SaveToDb(ImageView icon ){
            this.icon=icon;
        }

        @Override
        protected String doInBackground(Object... objects) {
            try{
                boolean success = new SQL_INTERACT(context).SQLPush(objects[0].toString());
                if(success) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.checked));
                            icon.setOnClickListener(null);
                            icon.setVisibility(View.VISIBLE);
                        }
                    });
                }else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            icon.setVisibility(View.VISIBLE);
                        }
                    });
                }


            }catch (Exception e){
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        icon.setVisibility(View.VISIBLE);
                    }
                });
                Toast.makeText(context,"failed to add to playlist",Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    }
}
