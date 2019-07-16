package classmate.screenable.titan;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    String _source;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<String> data, String source) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this._source=source;
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
                Log.w("SLIDE",item.getString("thumbnailurl").replaceAll("\\\\",""));

                holder.source.setImageDrawable(context.getResources().getDrawable(R.drawable.slideshare_logosmall));
                Picasso.get().load("http:"+item.getString("thumbnailurl").replaceAll("\\\\","")).into(holder.thumbnail);

            }
            holder.options.setImageDrawable(context.getResources().getDrawable(R.drawable.options));

//            holder.myTextView.setText(animal);
            }catch (Exception e){
                Log.w("TODO",e.toString());
            }

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


        }

        @Override
        public void onClick(View view) {

//            go to video view
            if (view.getId()==R.id.clickable){
                context.startActivity(new Intent(context,VideoWatch.class).putExtra("videoId",view.getTag().toString()));
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
}
