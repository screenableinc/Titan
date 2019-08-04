package classmate.screenable.titan;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

public class ClickVisual {
    Context context;
    View view;
    public ClickVisual(Context context, View view){
        this.context=context;this.view=view;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }
}
