package classmate.screenable.titan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import classmate.screenable.titan.Globals;
import classmate.screenable.titan.R;

public class VideosFragment extends Fragment {
    String videos;
    LinearLayout parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.videos, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("library",Context.MODE_PRIVATE);
        videos = preferences.getString(Globals.CATEGORY_VID_SHAREDPREF_KEY_NAME, "[]");
         parent = rootView.findViewById(R.id.parent);





        try {


            LoadVideos(parent);
        }catch (Exception e){
            Log.w("TODO", e.toString());
        }




        return rootView;
    }

    public void LoadFromActivity(){
        try {


            LoadVideos(parent);
        }catch (Exception e){
            Log.w("TODO", e.toString());
        }
    }



    private void LoadVideos(final LinearLayout parent) throws Exception{
        parent.removeAllViews();
        final JSONArray jsonOfVideos = new JSONArray(videos);
        String selected_course =((LibraryActivity) getActivity()).selected_course;
        for (int i = 0; i < jsonOfVideos.length(); i++) {
//            Log.w("decipher",jsonOfVideos.getJSONObject(i).toString());
            JSONObject video = new JSONObject(jsonOfVideos.getString(i));
            String course = video.getString("course");
            if(!course.equals(selected_course)){
                continue;
            }
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View view = inflater.inflate(R.layout.savedvideo,null);
            TextView textViewTitle = view.findViewById(R.id.title);
            ImageView ImageViewThumbnail = view.findViewById(R.id.thumbnail);
            ImageView ImageViewDelete = view.findViewById(R.id.delete);
            LinearLayout clickable = view.findViewById(R.id.clickable);
            final int count=i;


            JSONObject snippet = video.getJSONObject("snippet");
            String id  = video.getJSONObject("id").getString("videoId");

            ImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Log.e("REMOVEDDD",jsonOfVideos.remove(count).toString());
                            saveArray(RemoveJSONArray(jsonOfVideos,count).toString());
                            parent.removeView(view);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setTitle("Are you sure ?").create().show();
                }
            });
            clickable.setTag(id);
            Log.w("decipher",video.toString());

            String title = snippet.getString("title");
            String thumb_url = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
            textViewTitle.setText(title);
            clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),VideoWatch.class).putExtra("videoId",view.getTag().toString()));
                }
            });
            Picasso.get().load(thumb_url).into(ImageViewThumbnail);

            parent.addView(view);

        }
    }
    public void saveArray(String array){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("library",Context.MODE_PRIVATE).edit();
        editor.putString(Globals.CATEGORY_VID_SHAREDPREF_KEY_NAME,array);
        editor.commit();
    }

    public static JSONArray RemoveJSONArray( JSONArray jarray,int pos) {

        JSONArray Njarray=new JSONArray();
        try{
            for(int i=0;i<jarray.length();i++){
                if(i!=pos)
                    Njarray.put(jarray.get(i));
            }
        }catch (Exception e){e.printStackTrace();}
        return Njarray;

    }
}
