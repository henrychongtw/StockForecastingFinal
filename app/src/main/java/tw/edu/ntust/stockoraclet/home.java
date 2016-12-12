package tw.edu.ntust.stockoraclet;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tw.edu.ntust.stockoraclet.oracletpackage.common;
import tw.edu.ntust.stockoraclet.oracletpackage.oraclet;

/**
 * Created by Peter on 2016/5/10.
 */
public class home extends Fragment {
    private String date;
    private final static String TAG = "activityOne";
    private ProgressDialog progressDialog;
    private AsyncTask retrieveOraclet;
    private RecyclerView.Adapter adapter;
    private ListView lvOraclet;
    private RecyclerView recyclerView;
    private EditText etCode , etDate;
    private Button submit , submitDate;
    private String URLCODE;
    private int position;
    private String clickPeople;
    private Boolean firstTime = null;
    private Calendar myCalendar ;
    private String userEmail;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        userEmail = getArguments().getString("userEmail");
//        Log.i("emailhere", userEmail);
        final View home = inflater.inflate(R.layout.home_main, container, false);
        etCode =(EditText)home.findViewById(R.id.etCode);
        submit =(Button)home.findViewById(R.id.btSubmit);
        etDate = (EditText)home.findViewById(R.id.etDate);
        submitDate = (Button)home.findViewById(R.id.btSubmitDate);

        /*for pop up date*/
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /*for pop up date*/


        recyclerView = (RecyclerView) home.findViewById(R.id.lvOraclet);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.i("urltest", URLCODE);
                if (etCode.getText().toString().trim().length() == 0){
                    showToast(getActivity() , R.string.noID);

                }else{
                    if (networkConnected()) {
                        retrieveOraclet = new RetrieveOraclet().execute(common.URLcode + etCode.getText().toString().trim());
                        //need further trim , now if enter wrong ID , app will crash ( nullpointerexception)
                    } else {
                        showToast(getActivity(), R.string.msg_NoNetwork);
                    }

                }

            }
        });
        submitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.i("urltest", URLCODE);
                if (etDate.getText().toString().trim().length() == 0){
                    showToast(getActivity() , R.string.noDate);


                }else{
                    if (networkConnected()) {
                        retrieveOraclet = new RetrieveOraclet().execute(common.URLcode + etDate.getText().toString().trim());
                        //need further trim , now if enter wrong ID , app will crash ( nullpointerexception)
                    } else {
                        showToast(getActivity(), R.string.msg_NoNetwork);
                    }
                }

            }
        });


        if (networkConnected()) {
            retrieveOraclet = new RetrieveOraclet().execute(common.URLcode + "2823"); //9136
        } else {
            showToast(getActivity(), R.string.msg_NoNetwork);
        }

        return home;
    }
    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);

        etDate.setText(sdf.format(myCalendar.getTime()));
    }

    public class RetrieveOraclet extends AsyncTask<String, Integer, List<oraclet>> {
        @Override
        protected void onPostExecute(List<oraclet> result) {
//            Log.i("1111", result.get(39).getoccur_time());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new OracletListAdapter(getActivity().getLayoutInflater(), result));
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
        protected List<oraclet> doInBackground(String... params) {
            String url = params[0];

            String jsonIn;
            String jsonIn2;
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

            Log.e("tagggg", jsonIn);
            return gson.fromJson(jsonIn, listType);

        }
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();


        // HttpURLConnection connection = (HttpURLConnection) new URL(url + URLEncoder.encode("麥格理", "UTF-8")).openConnection();

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
        if (retrieveOraclet != null) {
            retrieveOraclet.cancel(true);
            retrieveOraclet = null;
        }
        super.onPause();
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }


    private class OracletListAdapter extends RecyclerView.Adapter<OracletListAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<oraclet> oracletList;

        public OracletListAdapter(LayoutInflater layoutInflater, List<oraclet> oracletList) {
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
            //recyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));

            final oraclet theOraclet = oracletList.get(position);
            Double accu;
            if(theOraclet.getAccuracy() != null){
                accu = Double.parseDouble(theOraclet.getAccuracy());
                accu = accu*100;
                viewHolder.tvAccuracy.setText(String.format("%.2f",accu)+"%");
            }else{
                viewHolder.tvAccuracy.setText("0.00%");
            }
            viewHolder.tvDate.setText(String.valueOf(theOraclet.getpredict_time()));
            viewHolder.tvPredictPeople.setText(String.valueOf(theOraclet.getpredict_people()));
            viewHolder.tvPredictTargetName.setText(String.valueOf(theOraclet.getpredict_targetname()));
            viewHolder.tvPredictTargetId.setText(String.valueOf(theOraclet.getpredict_targetcode()));
            String status;
            if (theOraclet.getresult_status() == 1){
                status = "已驗證";
            }else{
                status = "未驗證";
            }
            viewHolder.tvResultStatus.setText(String.valueOf(status));
            viewHolder.tvEventContent.setText(String.valueOf(theOraclet.getevent_content()));


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oracletNumber = theOraclet.getNumber();
                    Log.d("oracletNumber= ", String.valueOf(oracletNumber));
                    Intent intent = new Intent(getActivity(), OracletPage.class);
                    Log.i("what the heck?", intent.toString());
                    Bundle bundle = new Bundle();
                    bundle.putInt("number", oracletNumber);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return oracletList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder  {
            TextView tvPredictPeople, tvPredictTargetName, tvPredictTargetId, tvResultStatus
                    , tvEventContent, tvDate , tvAccuracy;

            public ViewHolder(View itemView) {
                super(itemView);
                tvAccuracy = (TextView) itemView.findViewById(R.id.tvAccuracy);
                tvPredictPeople = (TextView) itemView.findViewById(R.id.tvPredictPeople);
                tvPredictTargetName = (TextView) itemView.findViewById(R.id.tvPredictTargetName);
                tvPredictTargetId = (TextView) itemView.findViewById(R.id.tvPredictTargetId);
                tvResultStatus= (TextView) itemView.findViewById(R.id.tvResultStatus);
                tvEventContent = (TextView) itemView.findViewById(R.id.tvEventContent);
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            }

        }
    }
}
