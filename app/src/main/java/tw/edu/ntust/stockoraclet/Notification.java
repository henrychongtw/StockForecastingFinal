package tw.edu.ntust.stockoraclet;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import tw.edu.ntust.stockoraclet.loginpackage.helper.SQLiteHandler;
import tw.edu.ntust.stockoraclet.oracletpackage.SubscriptionInfo;
import tw.edu.ntust.stockoraclet.oracletpackage.common;


/**
 * Created by henrychong on 2016/5/28.
 */
public class Notification extends Fragment {
    private final static String TAG = "Notification";
    private ProgressDialog progressDialog;
    private AsyncTask retrieveStock;
    private RecyclerView recyclerView;
    private TextView notify;
    private SQLiteHandler db;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View notification = inflater.inflate(R.layout.notification_main, container, false);
        recyclerView = (RecyclerView) notification.findViewById(R.id.recyclerView_hot);


        if (networkConnected()) {
            retrieveStock = new RetrieveStock().execute(common.URLAllOraclet);
            //retrieveStock = new RetrieveStock().execute(common.URLAllPredictor);
        } else {
            showToast(getActivity(), R.string.msg_NoNetwork);
        }

        return notification;
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    private class RetrieveStock extends AsyncTask<String, Integer, List<SubscriptionInfo>>{
        @Override
        protected void onPostExecute(List<SubscriptionInfo> stocks) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new StockAdapter(getActivity().getLayoutInflater(), stocks));

            progressDialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<SubscriptionInfo> doInBackground(String... params) {
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
            Type listType = new TypeToken<List<SubscriptionInfo>>() {
            }.getType();

            Log.e("tag", jsonIn);
            return gson.fromJson(jsonIn, listType);

        }

    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();
        db = new SQLiteHandler(getActivity().getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");

        HttpURLConnection connection = (HttpURLConnection) new URL(url + email).openConnection();

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
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onPause() {
        if(retrieveStock != null) {
            retrieveStock.cancel(true);
            retrieveStock = null;
        }
        super.onPause();
    }

    private class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<SubscriptionInfo> stockList;

        public StockAdapter(LayoutInflater layoutInflater, List<SubscriptionInfo> stockList) {
            this.layoutInflater = layoutInflater;
            this.stockList = stockList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.recyclerview_notification, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
//            String accu;
            double ACCU;
            final SubscriptionInfo stock = stockList.get(position);
            //viewHolder.tvPredictorName.setText(String.valueOf(stock.getP_name()));

            /*
            if(stock.getAccuracy() != null){
                ACCU = Double.parseDouble(stock.getAccuracy());
                ACCU = ACCU*100;
//                accu = Double.toString(ACCU);
//                Log.i("accuracy here" , accu);  //String.format( "Value of a: %.2f", a )
                viewHolder.notificationAccuracy.setText(String.format("%.2f",ACCU)+"%");
//                accu = stock.getAccuracy();
//                Log.i("accuracy here" , accu);
            }else {
                viewHolder.notificationAccuracy.setText("0.00%");
            }

            if(stock.getNotification() == 1){
                viewHolder.tvNotification.setText("New Update!");
                viewHolder.tvNotification.setTextColor(Color.YELLOW);

            }
            else{
                viewHolder.tvNotification.setText("No Update");
                viewHolder.tvNotification.setTextColor(Color.WHITE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(stock.getNotification() == 1) {
                        stock.setNotification(0);
                        notifyDataSetChanged();
                    }
                    String predictorname = viewHolder.tvPredictorName.getText().toString();

                    Intent intent = new Intent(getActivity(), predictorAllOraclet.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("predictor", predictorname);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            */
        }


        @Override
        public int getItemCount() { return stockList.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvPredictorName, tvNotification ,notificationAccuracy;
            public ViewHolder(View itemView) {
                super(itemView);
                notificationAccuracy = (TextView) itemView.findViewById(R.id.notificationAccuracy);
                tvPredictorName = (TextView) itemView.findViewById(R.id.tvPredictorName);
                tvNotification = (TextView) itemView.findViewById(R.id.tvNotification);
            }
        }
    }

}

