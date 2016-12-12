package tw.edu.ntust.stockoraclet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import tw.edu.ntust.stockoraclet.loginpackage.helper.SQLiteHandler;
import tw.edu.ntust.stockoraclet.loginpackage.helper.SessionManager;
import tw.edu.ntust.stockoraclet.oracletpackage.common;
import tw.edu.ntust.stockoraclet.oracletpackage.oraclet;

/**
 * Created by henrychong on 2016/5/24.
 */
public class OracletPage extends AppCompatActivity {

    private final static String TAG = "activityOne";

    private ProgressDialog progressDialog;
    private AsyncTask retrieveOraclet, sendSub;
    private ListView lvOracletPage;
    private Toolbar oracletToolbar;
    private int theNumber , setP , result_status;
    private double setPrice;
    private SQLiteHandler db;
    private SessionManager session;
    private String email , predictor, targetname;
    private Button btSubscribe;
    private int isContradict;
    private int oracletNumber;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_oraclet_page, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oraclet_page_activity);

        oracletToolbar = (Toolbar) findViewById(R.id.oracletToolbar);
        //oracletToolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(oracletToolbar);

        oracletToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_subscribe:
                        onSubscribeClick();
                        break;
                    case R.id.action_graph:
                        onGraphClick();
                        //onContradictionClick();
                        break;
                    case R.id.action_contradiction:
                        onContradictionClick();
                        break;
                    case R.id.action_comment:
                        onCommentClick();
                        break;
                }
                return true;
            }
        });

        //btSubscribe =(Button) findViewById(R.id.btSubscribe);
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());


        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        email = user.get("email");

        Log.i("testing", email);


        lvOracletPage = (ListView) findViewById(R.id.lvOracletPage);
        if (networkConnected()) {
            retrieveOraclet = new RetrieveOraclet().execute(common.URL);
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }

    }

    public class sendSubscription extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            String u_email = params[1];
            String o_number = params[2];
            String result_status = params[3];
            String p_name = params[4];
            String t_name = params[5];

            Log.d("連接", "ING");
            try {
                Log.d("連接", "try");
                HttpURLConnection connection = (HttpURLConnection) new URL(url +
                        u_email + "&o_number=" + o_number + "&result_status=" + result_status +
                        "&p_name=" + p_name + "&t_name=" + t_name).openConnection();

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    //Context context = ;
                    Log.d("連接", "sub成功");
                    Log.d("u_email= ", u_email);
                    Log.d("o_number= ", o_number);
                    Log.d("p_name= ", p_name);
                    Log.d("t_name= ", t_name);
                    URLEncoder.encode("test", "UTF-8");
                    //Toast.makeText(getApplicationContext(), "訂閱成功！", Toast.LENGTH_LONG).show();
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "訂閱成功！", Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else
                    Log.d("連接", "sub失敗");
                    Log.d("ResponseCode= ", String.valueOf(responseCode));
                    Log.d("Response= ", connection.getResponseMessage());
                    Log.d("u_email= ", u_email);
                    Log.d("o_number= ", o_number);
                    Log.d("p_name= ", p_name);
                    Log.d("t_name= ", t_name);

            } catch (IOException e) {
                Log.d("連接", e.getMessage());

                /*Worker threads are meant for doing background tasks and you can't show anything on UI
                within a worker thread unless you call method like runOnUiThread.
                If you try to show anything on UI thread without calling runOnUiThread,
                there will be a java.lang.RuntimeException.*/

                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(), "請檢查網路是否開啟或通知開發人員", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    public class RetrieveOraclet extends AsyncTask<String, Integer, List<oraclet>> {
        @Override
        protected void onPostExecute(List<oraclet> result) {
//            Log.i("1111", result.get(39).getoccur_time());

            lvOracletPage.setAdapter(new OracletListAdapter(OracletPage.this, result));

            progressDialog.cancel();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OracletPage.this);
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
                Log.i(TAG, "jsonIn" + jsonIn);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<List<oraclet>>() {
            }.getType();

            Log.e("tag", jsonIn);
            return gson.fromJson(jsonIn, listType);

        }
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();
        Bundle bundle = getIntent().getExtras();
        theNumber = bundle.getInt("number");
        //Log.i("hereshowsthenumber", Integer.toString(theNumber));
        //HttpURLConnection connection = (HttpURLConnection) new URL(url + URLEncoder.encode("麥格理", "UTF-8")).openConnection();
        HttpURLConnection connection = (HttpURLConnection) new URL(common.URLoraclet + theNumber).openConnection();

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
            targetname = theOraclet.getpredict_targetname();
            String status;
            if (theOraclet.getresult_status() == 1) {
                status = "已驗證";
            }else{
                status = "未驗證";
            }
            Log.i("testing", String.valueOf(theOraclet.getNumber()));
            tvDate.setText("卜卦日期: " + theOraclet.getpredict_time());
            tvResultStatus.setText("驗證狀態: " + status);
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
//            tvType.setText("Type :" + theOraclet.getType());
            tvPredictTargetName.setText("股票名稱: " + theOraclet.getpredict_targetname());
//            tvNumber.setText("Oraclet ID :" + theOraclet.getNumber());
            String showResult;
            if(theOraclet.getResult() == "1"){
                showResult = "正確";
            }else if(theOraclet.getResult() == "0"){
                showResult = "錯誤";
            }else{
                showResult = "尚未出爐";
            }
            tvResult.setText("預測結果: " +showResult);

            return convertView;
        }
    }

    public void onGraphClick(){

        try{
            if(result_status == 1){
                Intent intent = new Intent(this, GraphActivity.class);
                Log.i("intenttttt?", intent.toString());
                Bundle bundle = new Bundle();
                Log.i("111111111111111", intent.toString());
                bundle.putInt("number", theNumber);
                bundle.putDouble("setPrice", setPrice);
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "股票尚未驗證完畢", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onCommentClick(){
        try{
                Intent intent2 = new Intent(this, CommentActivity.class);
                Log.i("intentttttforComment?", intent2.toString());
                Bundle bundle2 = new Bundle();
                Log.i("11111111forComment", intent2.toString());
                bundle2.putInt("number", theNumber);
                intent2.putExtras(bundle2);
                startActivity(intent2);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "這個卜卦沒有留言", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void onSubscribeClick() {
        try{
            sendSub = new sendSubscription().execute(common.URLSubscription,
                    email, String.valueOf(oracletNumber), String.valueOf(result_status),
                    predictor, targetname);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "連線可能有問題，請稍後再試", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onContradictionClick () {
        try{
            if(isContradict == 1 ){
                Intent intent4 = new Intent(this, Contradiction.class);
                Bundle bundle4 = new Bundle();
                bundle4.putInt("oracletNumber", oracletNumber);
                Log.i("11111111forComment", bundle4.toString());
                intent4.putExtras(bundle4);
                startActivity(intent4);
            }else{
                Toast.makeText(getApplicationContext(), "目前無矛盾預測", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "連線可能有問題，請稍後再試", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

     private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

}
