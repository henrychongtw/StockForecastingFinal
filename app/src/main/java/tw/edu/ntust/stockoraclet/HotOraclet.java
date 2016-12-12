package tw.edu.ntust.stockoraclet;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.util.List;

import tw.edu.ntust.stockoraclet.oracletpackage.common;
import tw.edu.ntust.stockoraclet.oracletpackage.hotOracletGet;

/**
 * Created by henrychong on 2016/5/24.
 */
public class HotOraclet extends Fragment {
    private final static String TAG = "activityOne";
    private ProgressDialog progressDialog;
    private AsyncTask retrieveHotOraclet;
    private RecyclerView recyclerView2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View hotOraclet = inflater.inflate(R.layout.hotoraclet_main, container, false);

        recyclerView2 = (RecyclerView) hotOraclet.findViewById(R.id.lvHotOraclet);

        if (networkConnected()) {
            retrieveHotOraclet = new RetrieveHotOraclet().execute(common.URLL);
        } else {
            showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return hotOraclet;
    }
    public class RetrieveHotOraclet extends AsyncTask<String, Integer, List<hotOracletGet>> {
        @Override
        protected void onPostExecute(List<hotOracletGet> result) {
            //Log.i("1111", result.get(1).getOccur_time());
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView2.setAdapter(new HotOracletListAdapter(getActivity().getLayoutInflater(), result));
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
        protected List<hotOracletGet> doInBackground(String... params) {
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
            Type listType = new TypeToken<List<hotOracletGet>>() {
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
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public void onPause() {
        if (retrieveHotOraclet != null) {
            retrieveHotOraclet.cancel(true);
            retrieveHotOraclet = null;
        }
        super.onPause();
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    private class HotOracletListAdapter extends RecyclerView.Adapter<HotOracletListAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<hotOracletGet> hotOracletList;

        public HotOracletListAdapter(LayoutInflater layoutInflater, List<hotOracletGet> hotOracletList) {
            this.layoutInflater = layoutInflater;
            this.hotOracletList = hotOracletList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.recyclerview_cardview_item, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {

            final hotOracletGet theOraclet = hotOracletList.get(position);
            Double accu;
            if(theOraclet.getAccuracy() != null){
                accu = Double.parseDouble(theOraclet.getAccuracy());
                accu = accu*100;
                viewHolder.tvAccuracy.setText(String.format("%.2f",accu)+"%");
            }else{
                viewHolder.tvAccuracy.setText("0.00%");
            }
            viewHolder.tvPredictPeople.setText(String.valueOf(theOraclet.getPredict_people()));
            viewHolder.tvPredictTargetName.setText(String.valueOf(theOraclet.getPredict_targetname()));
            viewHolder.tvPredictTargetId.setText(String.valueOf(theOraclet.getPredict_targetcode()));
            String status;
            if (theOraclet.getResult_status() == 1){
                status = "已驗證";
            }else{
                status = "未驗證";
            }
            viewHolder.tvResultStatus.setText(String.valueOf(status));
            viewHolder.tvEventContent.setText(String.valueOf(theOraclet.getEvent_content()));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oracletNumber = theOraclet.getNumber();
                    int result_status = theOraclet.getResult_status();
                    Intent intent = new Intent(getActivity(), OracletPage.class);
                    Log.i("what the heck?", intent.toString());
                    Bundle bundle = new Bundle();
                    bundle.putInt("number", oracletNumber);
                    bundle.putInt("result_status", result_status);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return hotOracletList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvPredictPeople, tvPredictTargetName, tvPredictTargetId, tvResultStatus,
                    tvEventContent , tvNumber , tvAccuracy;

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
}
