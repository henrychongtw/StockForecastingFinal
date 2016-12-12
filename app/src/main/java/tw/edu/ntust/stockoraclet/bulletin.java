package tw.edu.ntust.stockoraclet;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Peter on 2016/5/10.
 */
public class bulletin extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View bulletin = inflater.inflate(R.layout.bulletin_main, container, false);
        return bulletin;
    }
}
