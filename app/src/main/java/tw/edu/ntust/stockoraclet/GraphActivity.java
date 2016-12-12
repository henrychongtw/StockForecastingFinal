package tw.edu.ntust.stockoraclet;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tw.edu.ntust.stockoraclet.graphpackage.BarChartItem;
import tw.edu.ntust.stockoraclet.graphpackage.ChartItem;
import tw.edu.ntust.stockoraclet.graphpackage.DemoBase;
import tw.edu.ntust.stockoraclet.graphpackage.LineChartItem;
import tw.edu.ntust.stockoraclet.oracletpackage.Info;
import tw.edu.ntust.stockoraclet.oracletpackage.common;

/**
 * Created by henrychong on 2016/5/24.
 */
public class GraphActivity extends DemoBase {
    private TextView tvGraphTitle;
    private final static String TAG = "activityOne";
    private ProgressDialog progressDialog;
    private AsyncTask retrieveNumbers;
    private ListView lv;
    private ArrayList<BarEntry> entries;
    private ArrayList<String> mm ;
    private int theNumber , result_status ;
    private double setPrice;

    public class RetrieveNumbers extends AsyncTask<String, Integer, List<Info>> {
        @Override
        protected void onPostExecute(List<Info> result) {

            int n = result.size();
            int i;
            ArrayList<ChartItem> list = new ArrayList<ChartItem>();

//            /*Pie items*/
//
//            list.add(new PieChartItem(generateDataPie(2), getApplicationContext()));
//
//            /*Pie items*/



            /*Bar items*/
            mm = new ArrayList<String>();
            entries = new ArrayList<BarEntry>();

            String[] d = new String[n];
            BarDataSet dd = new BarDataSet(entries, "驗證號碼:" + result.get(1).getO_number() );
            dd.setBarSpacePercent(20f);
            dd.setColors(ColorTemplate.VORDIPLOM_COLORS);
            dd.setHighLightAlpha(255);
            for(i = 0 ; i < n ; i++)
            {
                mm.add(result.get(i).getDate());
                entries.add(new BarEntry((int) result.get(i).getPrice(), i));

            }
            BarData cd = new BarData(mm, dd);
            list.add(new BarChartItem(cd, getApplicationContext()));
            /*Bar items*/


            /*Line items*/
            ArrayList<Entry> e1 = new ArrayList<Entry>();
            for(i = 0 ; i < n ; i++)
            {

                e1.add(new Entry((int) result.get(i).getPrice(), i));

            }


            LineDataSet d1 = new LineDataSet(e1, "驗證號碼 : " + result.get(0).getO_number());
            d1.setLineWidth(2.5f);
            d1.setCircleRadius(4.5f);
            d1.setHighLightColor(Color.rgb(244, 180, 225));
            d1.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
            d1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            d1.setDrawValues(false);

            ArrayList<Entry> e2 = new ArrayList<Entry>();

            for ( i = 0; i < n; i++) {
                e2.add(new Entry((int) setPrice, i));
            }

            LineDataSet d2 = new LineDataSet(e2, "預測價格： " + setPrice );
            d2.setLineWidth(2.5f);
            d2.setCircleRadius(4.5f);
            d2.setHighLightColor(Color.rgb(244, 117, 117));
            d2.setColor(ColorTemplate.COLORFUL_COLORS[3]);
            d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
            d2.setDrawValues(false);

            ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
            sets.add(d1);
            sets.add(d2);
            LineData cd2 = new LineData(mm, sets);

            list.add(new LineChartItem(cd2, getApplicationContext()));

            /*Line items*/



            ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
            lv.setAdapter(cda);

            progressDialog.cancel();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GraphActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<Info> doInBackground(String... params) {
            String url = params[0];

            String jsonIn;
            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("param");
            try {
                jsonIn = getRemoteData(url, jsonObject.toString());
                Log.i(TAG, "jsonIn" + jsonIn);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Info>>() {
            }.getType();

            Log.i("tag?????????", jsonIn);
            return gson.fromJson(jsonIn, listType);
        }
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(common.URL2 + theNumber ).openConnection();
        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn: " + jsonIn);
        return jsonIn.toString();
    }

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.graph_activity);
        Bundle bundle = getIntent().getExtras();
        theNumber = bundle.getInt("number");
        setPrice = bundle.getDouble("setPrice");
        result_status = bundle.getInt("result_status");
        //Log.i("setPRiceathere",Integer.toString(setPrice));
        tvGraphTitle = (TextView) findViewById(R.id.tvGraphTitle);
        lv = (ListView) findViewById(R.id.listView1);
        if (networkConnected()) {
            retrieveNumbers = new RetrieveNumbers().execute(common.URL2 + theNumber );
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }

    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }


    private PieData generateDataPie(int cnt) {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new Entry((int) (Math.random() * 70) + 30, i));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(getQuarters(), d);
        return cd;
    }

    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("第一季");
        q.add("第二季");
        q.add("第三季");
        q.add("第四季");

        return q;
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("一月");
        m.add("二月");
        m.add("三月");
        m.add("四月");
        m.add("五月");
        m.add("六月");
        m.add("七月");
        m.add("八月");
        m.add("九月");
        m.add("十月");
        m.add("十一月");
        m.add("十二月");

        return m;
    }

    @Override
    protected void onPause() {
        if (retrieveNumbers != null) {
            retrieveNumbers.cancel(true);
            retrieveNumbers = null;
        }
        super.onPause();
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    //public void onBackClick(View view) {
        //finish();
    //}

}
