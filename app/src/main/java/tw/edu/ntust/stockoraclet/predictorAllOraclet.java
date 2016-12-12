package tw.edu.ntust.stockoraclet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import tw.edu.ntust.stockoraclet.loginpackage.helper.SQLiteHandler;
import tw.edu.ntust.stockoraclet.loginpackage.helper.SessionManager;
import tw.edu.ntust.stockoraclet.oracletpackage.common;
import tw.edu.ntust.stockoraclet.oracletpackage.oraclet;



/**
 * Created by henrychong on 2016/5/27.
 */
public class predictorAllOraclet extends AppCompatActivity {
    private final static String TAG = "activityOne";
    private ProgressDialog progressDialog2;
    private ProgressDialog progressDialog;
    private AsyncTask getData;
    private AsyncTask retrievePredictorAllOraclet;
    private RecyclerView recyclerView2;
    private SQLiteHandler db;
    private SessionManager session;
    private String predictor, userEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predictoralloraclet_main);
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        userEmail = user.get("email");
        Log.i("testing", userEmail);

        recyclerView2 = (RecyclerView) findViewById(R.id.lvPredictorAllOraclet);
        Bundle bundle = getIntent().getExtras();
        predictor = bundle.getString("predictor");
        getData = new GetClass(this).execute();

        if (networkConnected()) {
            retrievePredictorAllOraclet = new RetrievePredictorAllOraclet().execute(common.URLPredictorOraclet + predictor);
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }

    }
    public class RetrievePredictorAllOraclet extends AsyncTask<String, Integer, List<oraclet>> {
        @Override
        protected void onPostExecute(List<oraclet> result) {
            //Log.i("1111", result.get(1).getOccur_time());
            recyclerView2.setLayoutManager(new LinearLayoutManager(predictorAllOraclet.this));
            recyclerView2.setAdapter(new PredictorAllOracletListAdapter(getLayoutInflater(), result));
            progressDialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(predictorAllOraclet.this);
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
                Log.i("hereIsInput", "jsonIn" + jsonIn);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<List<oraclet>>() {
            }.getType();

            Log.i("taggggforHOT", jsonIn);
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
    public void onPause() {
        if (retrievePredictorAllOraclet != null) {
            retrievePredictorAllOraclet.cancel(true);
            retrievePredictorAllOraclet = null;
        }
        super.onPause();
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    private class PredictorAllOracletListAdapter extends RecyclerView.Adapter<PredictorAllOracletListAdapter.ViewHolder> {
        //        private LayoutInflater inflater;
//        private List<Stock> stockList;
        private LayoutInflater layoutInflater;
        private List<oraclet> oracletList;

        public PredictorAllOracletListAdapter(LayoutInflater layoutInflater, List<oraclet> oracletList) {
            this.layoutInflater = layoutInflater;
            this.oracletList = oracletList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.recyclerview_cardview_item, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            final oraclet theOraclet = oracletList.get(position);
            Double accu;
            if(theOraclet.getAccuracy() != null){
                accu = Double.parseDouble(theOraclet.getAccuracy());
                accu = accu*100;
                viewHolder.tvAccuracy.setText(String.format("%.2f",accu)+"%");
            }else{
                viewHolder.tvAccuracy.setText("0.00%");
            }
            viewHolder.tvPredictPeople.setText(String.valueOf(theOraclet.getpredict_people()));
            viewHolder.tvPredictTargetName.setText(String.valueOf(theOraclet.getpredict_targetname()));
            viewHolder.tvPredictTargetId.setText(String.valueOf(theOraclet.getpredict_targetcode()));
            String status;
            if (theOraclet.getresult_status() == 1){
                status = "已驗證";
            }else{
                status = "未驗證";
            }
            viewHolder.tvResultStatus.setText(status);
            viewHolder.tvEventContent.setText(String.valueOf(theOraclet.getevent_content()));
//            viewHolder.tvNumber.setText(String.valueOf(theOraclet.getNumber()));

            if (theOraclet.getNameLength() > 2) {
                viewHolder.tvPredictPeople.setPadding(0, 10, 0, 10);
                viewHolder.tvPredictPeople.setTextSize(TypedValue.COMPLEX_UNIT_PT, 7);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int oracletNumber = theOraclet.getNumber();
                    Intent intent = new Intent(predictorAllOraclet.this, OracletPage.class);
                    Log.i("what the heck?", intent.toString());
                    Bundle bundle = new Bundle();
                    bundle.putInt("number", oracletNumber);
                    intent.putExtras(bundle);
                    startActivity(intent);

//                    Intent intent2 = new Intent(predictorAllOraclet.this, setNotification.class);
//                    Bundle bundle2 = new Bundle();
//                    bundle2.putString("predictor", predictor);
//                    intent.putExtras(bundle2);
//                    startActivity(intent2);

                }
            });
        }

        @Override
        public int getItemCount() {
            return oracletList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder  {
            TextView tvPredictPeople, tvPredictTargetName, tvPredictTargetId, tvResultStatus
                    , tvEventContent, tvNumber , tvAccuracy;

            public ViewHolder(View itemView) {
                super(itemView);
                tvAccuracy = (TextView) itemView.findViewById(R.id.tvAccuracy);
                tvPredictPeople = (TextView) itemView.findViewById(R.id.tvPredictPeople);
                tvPredictTargetName = (TextView) itemView.findViewById(R.id.tvPredictTargetName);
                tvPredictTargetId = (TextView) itemView.findViewById(R.id.tvPredictTargetId);
                tvResultStatus= (TextView) itemView.findViewById(R.id.tvResultStatus);
                tvEventContent = (TextView) itemView.findViewById(R.id.tvEventContent);
                tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);

            }


        }
    }

    private class GetClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetClass(Context c){
            this.context = c;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog2.cancel();
        }

        protected void onPreExecute(){
            progressDialog2= new ProgressDialog(this.context);
            progressDialog2.setMessage("Loading");
            progressDialog2.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL("http://140.118.7.46:8000/setNotification?userEmail="+userEmail+"&predictor="+predictor+"&status=0");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }
}
