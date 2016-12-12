package tw.edu.ntust.stockoraclet;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import tw.edu.ntust.stockoraclet.oracletpackage.common;
import tw.edu.ntust.stockoraclet.oracletpackage.oraclet;

/**
 * Created by henrychong on 2016/5/27.
 */
public class Contradiction extends AppCompatActivity {
    private final static String TAG = "activityOne";
    private ProgressDialog progressDialog;
    private AsyncTask retrieveOraclet;
    private ListView lvBuy, lvSell;
    private double setPrice;
    private int result_status;
    private int isContradict;
    private String predictor;
    private int oracletNumber;
    private String event;
    public class RetrieveOraclet extends AsyncTask<String, Integer, List<oraclet>> {
        @Override
        protected void onPostExecute(List<oraclet> result) {
//            Log.i("1111", result.get(39).getoccur_time());
//            lvSell.setAdapter(new OracletListAdapter(Contradiction.this, result.get(1)));
//            lvBuy.setAdapter(new OracletListAdapter2(Contradiction.this, result));
            List<oraclet> buy = new ArrayList<oraclet>();
            List<oraclet> sell = new ArrayList<oraclet>();
//            buy.add(0,result.get(0));

            int n = result.size();
            int i ;
            for (i = 0 ; i < n ; i++){
                if(result.get(i).getevent_content().equals("買進")){
                    buy.add(result.get(i));
                }else if(result.get(i).getevent_content().equals("賣出")){
                    sell.add(result.get(i));
                }
            }
            Log.i("sell", sell.size()+"");
            Log.i("buy", buy.size()+"");

            Log.i("resultforlist", result.get(0).getevent_content());
            Log.i("resultforlist", result.size()+"");
//          Log.i("resultforlist", result.get(1).getevent_content());




           lvBuy.setAdapter(new OracletListAdapter(Contradiction.this, buy));
           lvSell.setAdapter(new OracletListAdapter(Contradiction.this, sell));



            progressDialog.cancel();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Contradiction.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<oraclet> doInBackground(String... params) {
            String url = params[0];

            String jsonIn;
            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("param");
            try {
                jsonIn = getRemoteData(url, jsonObject.toString());
                Log.i("recording", "jsonIn" + jsonIn);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<List<oraclet>>() {
            }.getType();

            Log.e("tag", jsonIn);
            return gson.fromJson(jsonIn, listType);

//            btSubscribe.setOnClickListener(View.OnClickListener);


        }
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();

        //Log.i("hereshowsthenumber", Integer.toString(theNumber));
        //HttpURLConnection connection = (HttpURLConnection) new URL(url + URLEncoder.encode("麥格理", "UTF-8")).openConnection();
        HttpURLConnection connection = (HttpURLConnection) new URL(common.URLContradiction + oracletNumber).openConnection();

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
        setContentView(R.layout.contradiction_activity);
        Bundle bundle = getIntent().getExtras();
        oracletNumber = bundle.getInt("oracletNumber");
//        lvOracletPage = (ListView) findViewById(R.id.lvOracletPage);
        lvBuy =(ListView) findViewById(R.id.buy);
        lvSell =(ListView) findViewById(R.id.sell);

        if (networkConnected()) {
            retrieveOraclet = new RetrieveOraclet().execute(common.URLContradiction + oracletNumber);
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }

    }
    private class OracletListAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<oraclet> oracletList;


        public OracletListAdapter(Context context, List<oraclet> oracletList) {
            this.layoutInflater = LayoutInflater.from(context);
            this.oracletList = oracletList;
        }

        @Override
        public int getCount() {
            return oracletList.size();
        }

        @Override
        public Object getItem(int position) {
            return oracletList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return oracletList.get(position).getpredict_targetcode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.oraclet_activity, parent, false);
            }
            if(position % 2 == 0){
                convertView.setBackgroundColor(0x3c000000);
            }else{
                convertView.setBackgroundColor(0x8cFF0000);

            }

            TextView tvOracletAccuracy = (TextView) convertView.findViewById(R.id.tvOracletAccuracy);
            TextView tvResult = (TextView) convertView.findViewById(R.id.tvResult);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            TextView tvResultStatus = (TextView) convertView.findViewById(R.id.tvResultStatus);
            TextView tvPredictTime = (TextView) convertView.findViewById(R.id.tvPredictTime);
            TextView tvEventContent = (TextView) convertView.findViewById(R.id.tvEventContent);
            TextView tvPredictTargetId = (TextView) convertView.findViewById(R.id.tvPredictTargetId);
            TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            TextView tvHasContradiction = (TextView) convertView.findViewById(R.id.tvHasContradiction);
            TextView tvPredictPeople = (TextView) convertView.findViewById(R.id.tvPredictPeople);
//            TextView tvType = (TextView) convertView.findViewById(R.id.tvType);
            TextView tvPredictTargetName = (TextView) convertView.findViewById(R.id.tvPredictTargetName);

            oraclet theOraclet = oracletList.get(position);
            Double accu;
            if(theOraclet.getAccuracy() != null){
                accu = Double.parseDouble(theOraclet.getAccuracy());
                accu = accu*100;
                tvOracletAccuracy.setText("準確度: " +String.format("%.2f",accu)+"%");
            }else{
                tvOracletAccuracy.setText("準確度: " +"0.00%");
            }
            setPrice = theOraclet.getnow_price();
            result_status = theOraclet.getresult_status();
            isContradict = theOraclet.isHasContradiction();
            predictor = theOraclet.getpredict_people();
            oracletNumber = theOraclet.getNumber();

            String status;
            if (theOraclet.getresult_status() == 1){
                status = "已驗證";
            }else{
                status = "未驗證";
            }

            tvDate.setText("預測時間: " + theOraclet.getpredict_time());
            tvResultStatus.setText("結果驗證狀態: " + status);
            tvPredictTime.setText("預測事件時間: " + theOraclet.getoccur_time());
            tvEventContent.setText("股票性質: " + theOraclet.getevent_content());
            tvPredictTargetId.setText("股票號碼: " + theOraclet.getpredict_targetcode());
            tvPrice.setText("預測價格: " + theOraclet.getnow_price());

            String contradict;
            if (theOraclet.isHasContradiction() == 1){
                contradict = "有";
            }else{
                contradict = "無";
            }
            tvHasContradiction.setText("有無矛盾預測: " + contradict);
            tvPredictPeople.setText("預測人名稱: " + theOraclet.getpredict_people());
            tvPredictTargetName.setText("股票名稱: " + theOraclet.getpredict_targetname());

            String showResult;
            if(theOraclet.getResult() == "1"){
                showResult = "預測正確";
            }else if(theOraclet.getResult() == "0"){
                showResult = "預測錯誤";
            }else{
                showResult = "結果尚未出爐";
            }
            tvResult.setText("預測結果: " +showResult);

//
//                    tvDate.setText("Date :" + theOraclet.getoccur_time());
//                    tvResultStatus.setText("Result Status :" + theOraclet.getresult_status());
//                    tvPredictTime.setText("Predict Time :" + theOraclet.getpredict_time());
//                    tvEventContent.setText("Event Content :" + theOraclet.getevent_content());
//                    tvNumber.setText("Number :" + theOraclet.getNumber());
//                    tvPredictTargetId.setText("PredictTargetId :" + theOraclet.getpredict_targetcode());
//                    tvPrice.setText("Price :" + theOraclet.getnow_price());
//                    tvHasContradiction.setText("HasContradiction :" + theOraclet.isHasContradiction());
//                    tvPredictPeople.setText("PredictPeople :" + theOraclet.getpredict_people());
////                    tvType.setText("Type :" + theOraclet.getType());
//                    tvPredictTargetName.setText("PredictTargetName :" + theOraclet.getpredict_targetname());


            return convertView;
        }
    }




    @Override
    protected void onPause() {
        if (retrieveOraclet != null) {
            retrieveOraclet.cancel(true);
            retrieveOraclet = null;
        }
        super.onPause();
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }
}
