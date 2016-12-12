package tw.edu.ntust.stockoraclet;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Peter on 2016/5/10.
 */
public class about extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View about = inflater.inflate(R.layout.about_main, container, false);
        return about;
    }
}
