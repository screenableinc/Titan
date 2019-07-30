package classmate.screenable.titan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AssignmentsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.assignments, container, false);

        LinearLayout parent = rootView.findViewById(R.id.parent);
        try {


//            LoadVideos(parent);
        }catch (Exception e){
            Log.w("TODO", e.toString());
        }




        return rootView;
    }
}
