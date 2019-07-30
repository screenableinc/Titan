package classmate.screenable.titan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.File;

import classmate.screenable.titan.R;

public class DocumentFragment extends Fragment {
    String documents;
    LinearLayout parent;
    private static final int REQUEST_WRITE_STORAGE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.documents, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("library", Context.MODE_PRIVATE);
        documents = preferences.getString(Globals.CATEGORY_SLIDE_SHAREDPREF_KEY_NAME, "[]");
        parent = rootView.findViewById(R.id.parent);
        new LoadDocs().execute();
        new LoadPortalDocs().execute();

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SyncPortalMaterial().execute();
            }
        });





        return rootView;
    }

    private void LoadVideos(final LinearLayout parent, final JSONArray jsonOfDocuments) throws Exception{


        for (int i = 0; i < jsonOfDocuments.length(); i++) {
//            Log.w("decipher",jsonOfVideos.getJSONObject(i).toString());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View view = inflater.inflate(R.layout.saveddocument,null);
            TextView textViewTitle = view.findViewById(R.id.title);
            ImageView ImageViewThumbnail = view.findViewById(R.id.thumbnail);
            final ImageView ImageViewDelete = view.findViewById(R.id.delete);
            final ImageView ImageViewDownload = view.findViewById(R.id.download);
            final ImageView ImageViewDownloaded = view.findViewById(R.id.checked);
            final CircularProgressBar progressBar = view.findViewById(R.id.downloadprogress);
            final ProgressBar loading = view.findViewById(R.id.loading);
            LinearLayout clickable = view.findViewById(R.id.clickable);
            final int count=i;



            JSONObject doc_details = new JSONObject(jsonOfDocuments.getString(i));
            final String title = doc_details.getString("title");
            final String id  = doc_details.getString("id");

            if(doc_details.getInt("downloadable")==1){

                ImageViewDownload.setVisibility(View.VISIBLE);


                ImageViewDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        request persmissions
                        if (permissions()) {
                            PromptDownloadDialog(progressBar, loading, id, title, ImageViewDownload,null,"slideshare", ImageViewDelete);
                        }else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_STORAGE);


                        }
                    }
                });

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

    private void LoadPortalDocs(final LinearLayout parent, final JSONArray jsonOfDocuments) throws Exception{


        for (int i = 0; i < jsonOfDocuments.length(); i++) {
//            Log.w("decipher",jsonOfVideos.getJSONObject(i).toString());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View view = inflater.inflate(R.layout.saveddocument,null);
            TextView textViewTitle = view.findViewById(R.id.title);
            ImageView ImageViewThumbnail = view.findViewById(R.id.thumbnail);
            final ImageView ImageViewDelete = view.findViewById(R.id.delete);
            final ImageView ImageViewDownload = view.findViewById(R.id.download);
            final ImageView ImageViewDownloaded = view.findViewById(R.id.checked);
            final CircularProgressBar progressBar = view.findViewById(R.id.downloadprogress);
            final ProgressBar loading = view.findViewById(R.id.loading);
            LinearLayout clickable = view.findViewById(R.id.clickable);
            final int count=i;



            JSONObject doc_details = new JSONObject(jsonOfDocuments.getString(i));
            final String title = doc_details.getString("title");
            final String id  = doc_details.getString("id");
            final String url  = doc_details.getString("url");

            final String path  = doc_details.getString("path");
            File fileDirectory = new File(path);
            if (fileDirectory.exists()){
                ImageViewDownloaded.setVisibility(View.VISIBLE);
            }else {
                ImageViewDownload.setVisibility(View.VISIBLE);


                ImageViewDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        request persmissions
                        if (permissions()) {
                            PromptDownloadDialog(progressBar, loading, id, title, ImageViewDownload, url, "unilus", ImageViewDownloaded);
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_STORAGE);


                        }
                    }
                });

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




        textViewTitle.setText(title);
        clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    startActivity(new Intent(getActivity(),VideoWatch.class).putExtra("videoId",view.getTag().toString()));
            }
        });
//            Picasso.get().load("http:"+thumb_url.replaceAll("\\\\","")).into(ImageViewThumbnail);
        if(title.toLowerCase().contains("pdf")){
            ImageViewThumbnail.setImageDrawable(getResources().getDrawable(R.drawable.pdf));
        }else if(title.toLowerCase().contains("doc")){
            ImageViewThumbnail.setImageDrawable(getResources().getDrawable(R.drawable.word));
        }else if(title.toLowerCase().contains("ppt")){
            ImageViewThumbnail.setImageDrawable(getResources().getDrawable(R.drawable.ppt));
        }else {
            ImageViewThumbnail.setImageDrawable(getResources().getDrawable(R.drawable.file));
        }
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

//    check permissions
    public boolean permissions(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted...request now
            return false;


        }else {
            return true;
    }

    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void PromptDownloadDialog(final CircularProgressBar bar, final ProgressBar loading, final String id, final String suggestedTitle, final ImageView downloadIcon, final String url, final String source, final ImageView checked){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final LinearLayout linearLayout =(LinearLayout) inflater.inflate(R.layout.save_as,null);
        final EditText filename = linearLayout.findViewById(R.id.filename);
        filename.setText(suggestedTitle);
        filename.selectAll();
        builder.setView(linearLayout)
                .setTitle("Save As")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (!filename.getText().toString().equals("")) {
                    downloadIcon.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    new DownloadFile(id, filename.getText().toString(), getActivity(), bar, loading, downloadIcon,source,url,checked).execute();
                }
            }
        }).create().show();
    }

    public class SyncPortalMaterial extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Pair<Boolean, Integer> getmaterial = new GetMaterial(getContext(), "ECF300").startConnect();
            }catch (Exception e){
                Log.w("CC",e.toString());
            }
                return null;
        }
    }

    private class LoadPortalDocs extends AsyncTask{
        JSONArray array=new JSONArray();
        boolean success=false;

        @Override
        protected void onPostExecute(Object o) {
            if (success) {
                try {
                    Log.w("TODO",array.toString());

                    LoadPortalDocs(parent, array);
                }catch (Exception e){
                    Log.w("CC loadpd",e.toString());
                }
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                array = new SQL_INTERACT(getActivity()).GetDocuments(FeedReaderContract.FeedEntry.UNILUS_DOC_TABLE_NAME);
                Log.w("CCcritical2",array.toString());
                success=true;
            }catch (Exception e){
                Log.w("CCcritical",e.toString());
            }
            return null;
        }
    }

    public class LoadDocs extends AsyncTask{
        boolean success;
        JSONArray array;
        private LoadDocs(){

        }

        @Override
        protected void onPostExecute(Object o) {
            if(success){
            try {


                LoadVideos(parent,array);
                Log.w("TODO", array.toString()+" success");
            }catch (Exception e){
                Log.w("TODO", e.toString());

            }
            }else {
//                something went wrong
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try{
                array = new SQL_INTERACT(getActivity()).GetDocuments(FeedReaderContract.FeedEntry.TABLE_NAME);
                success=true;

            }catch (Exception e){
                Log.w("TODO", e.toString()+" failed");
            }

            return null;
        }
    }
    public class DeleteFile extends AsyncTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            File dir = new File(Globals.folder);
            File file=new File(dir,objects[0].toString());
            file.delete();
            return null;
        }
    }
}
