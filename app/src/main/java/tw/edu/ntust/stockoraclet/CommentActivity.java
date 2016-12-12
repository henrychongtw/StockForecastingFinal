package tw.edu.ntust.stockoraclet;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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

import tw.edu.ntust.stockoraclet.graphpackage.ChartItem;
import tw.edu.ntust.stockoraclet.graphpackage.DemoBase;
import tw.edu.ntust.stockoraclet.graphpackage.PieChartItem;
import tw.edu.ntust.stockoraclet.oracletpackage.comment;
import tw.edu.ntust.stockoraclet.oracletpackage.common;

/**
 * Created by henrychong on 2016/5/25.
 */
public class CommentActivity extends DemoBase {
    private ProgressDialog progressDialog;
    private final static String TAG = "activityOne";
    private AsyncTask retrieveComments; //retrieveNumbers
    private ListView lv, lv2;
    private int theNumber;
    private int push , boo, total, arrow;
    private float pushPercent , booPercent ,arrowPercent;

    public class RetrieveNumbers extends AsyncTask<String, Integer, List<comment>> {
        @Override
        protected void onPostExecute(List<comment> result) {
            //            String pushWord = URLEncoder.encode("推", "UTF-8");
//            String booWord = URLEncoder.encode("噓", "UTF-8");

//            if(theComment.getStatus() ==  ){
//                push = push + 1;
//            }else if(theComment.getStatus() == ""){
//                boo = boo +1;
//            }
//            Log.i("Push", theComment.getStatus());
//            Log.i("Boo", Integer.toString(boo));

            int n = result.size();
            int i;

            for(i = 0 ; i<n ; i++){
                if(result.get(i).getStatus().equals("推") ){
                    push = push + 1;
                }else if(result.get(i).getStatus().equals("噓")){
                    boo = boo + 1;
                }else{
                    arrow = arrow +1;
                }
            }

            total = push + boo + arrow;
            pushPercent = (push/total)*100;
            booPercent = (boo/total)*100;
            arrowPercent = (arrow/total)*100;


            Log.i("Boo", Integer.toString(boo));
            Log.i("Push", Integer.toString(push));


            ArrayList<ChartItem> list = new ArrayList<ChartItem>();

            /*Pie items*/

            list.add(new PieChartItem(generateDataPie(2), getApplicationContext()));

            /*Pie items*/
            ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
            lv.setAdapter(cda);
            lv2.setAdapter(new CommentListAdapter(CommentActivity.this, result));

            progressDialog.cancel();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<comment> doInBackground(String... params) {
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
            Type listType = new TypeToken<List<comment>>() {
            }.getType();

            Log.i("tag?????????", jsonIn);
            return gson.fromJson(jsonIn, listType);
        }
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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
        setContentView(R.layout.comment_activity);
        lv = (ListView) findViewById(R.id.listViewCommentGraph);
        lv2 = (ListView) findViewById(R.id.listViewComment);
        Bundle bundle = getIntent().getExtras();
        theNumber = bundle.getInt("number");
        Log.i("hereshowsthenumber", Integer.toString(theNumber));
        if (networkConnected()) {
            retrieveComments = new RetrieveNumbers().execute(common.URLcomment + Integer.toString(theNumber));
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

//        for (int i = 0; i < 3; i++) {
//            entries.add(new Entry((int) (Math.random() * 70) + 30, i));
//        }
        entries.add(new Entry(push, 0));
        entries.add(new Entry(boo, 1));
        entries.add(new Entry(arrow, 2));


        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData cd = new PieData(getQuarters(), d);
        return cd;
    }

    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("喜歡的百分比、總和="+push);
        q.add("不喜歡的百分比、總和="+boo);
        q.add("中立百分比、總和="+arrow);
        return q;
    }

    private class CommentListAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<comment> commentList;


        public CommentListAdapter(Context context, List<comment> commentList) {
            this.layoutInflater = LayoutInflater.from(context);
            this.commentList = commentList;
        }

        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return commentList.get(position).getO_number();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.commentlv_activity, parent, false);
            }
            if(position % 2 == 0){
                convertView.setBackgroundColor(Color.rgb(255, 255, 112));
            }else{
                convertView.setBackgroundColor(Color.rgb(255, 176, 97));

            }
            TextView tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);

            comment theComment = commentList.get(position);

            tvStatus.setText("推噓狀態: " + theComment.getStatus());
            tvName.setText("使用者: " + theComment.getName());
            tvContent.setText("留言: " + theComment.getContent());
            tvTime.setText("留言時間: " + theComment.getTime());

            return convertView;
        }
    }



    @Override
    protected void onPause() {
        if (retrieveComments != null) {
            retrieveComments.cancel(true);
            retrieveComments = null;
        }
        super.onPause();
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    public void onBackClick(View view) {
        finish();
    }
}
