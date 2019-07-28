package classmate.screenable.titan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import classmate.screenable.titan.R;

public class DocumentFragment extends Fragment {
    String documents;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.documents, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("library", Context.MODE_PRIVATE);
        documents = preferences.getString(Globals.CATEGORY_SLIDE_SHAREDPREF_KEY_NAME, "[]");
        LinearLayout parent = rootView.findViewById(R.id.parent);
        try {


            LoadVideos(parent);
        }catch (Exception e){
            Log.w("TODO", e.toString());
        }






        return rootView;
    }

    private void LoadVideos(final LinearLayout parent) throws Exception{
        Log.w("decipher",documents);
        final JSONArray jsonOfDocuments = new JSONArray(documents);
        for (int i = 0; i < jsonOfDocuments.length(); i++) {
//            Log.w("decipher",jsonOfVideos.getJSONObject(i).toString());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View view = inflater.inflate(R.layout.saveddocument,null);
            TextView textViewTitle = view.findViewById(R.id.title);
            ImageView ImageViewThumbnail = view.findViewById(R.id.thumbnail);
            ImageView ImageViewDelete = view.findViewById(R.id.delete);
            ImageView ImageViewDownload = view.findViewById(R.id.download);
            LinearLayout clickable = view.findViewById(R.id.clickable);
            final int count=i;

            JSONObject doc_details = new JSONObject(jsonOfDocuments.getString(i));

            String id  = doc_details.getString("id");
            if(doc_details.getInt("downloadable")==1){
                ImageViewDownload.setVisibility(View.VISIBLE);

            }

            ImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Log.e("REMOVEDDD",jsonOfVideos.remove(count).toString());
                            saveArray(RemoveJSONArray(jsonOfDocuments,count).toString());
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


            String title = doc_details.getString("title");
            String thumb_url = doc_details.getString("thumbnailurl");
            textViewTitle.setText(title);
            clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    startActivity(new Intent(getActivity(),VideoWatch.class).putExtra("videoId",view.getTag().toString()));
                }
            });
            Picasso.get().load("http:"+thumb_url.replaceAll("\\\\","")).into(ImageViewThumbnail);

            parent.addView(view);

        }
    }
    public void saveArray(String array){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("library",Context.MODE_PRIVATE).edit();
        editor.putString(Globals.CATEGORY_SLIDE_SHAREDPREF_KEY_NAME,array);
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
