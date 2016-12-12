package tw.edu.ntust.stockoraclet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by henrychong on 2016/5/26.
 */
public class SubscribeActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private AsyncTask getData;
    private String email , o_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_activity);
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        o_number = bundle.getString("o_number");

        getData = new GetData(this).execute();
    }

    public class GetData extends AsyncTask<String, Void, Void> {
        private final Context context;

        public GetData(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progressDialog= new ProgressDialog(this.context);
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            try{
                final TextView outputView = (TextView) findViewById(R.id.tvSubscribed);
//                http://140.118.7.46:8000/subscribePredictor?userEmail=stillmight@gmail.com&predictor=chengwaye
                String urlConnect = "http://140.118.7.47:8000/subscribePredictor?userEmail="+email+"&o_number="+o_number;
                URL url = new URL(urlConnect);

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responseCode = connection.getResponseCode();
                Log.d("responseCode: ", String.valueOf(responseCode));
//                final StringBuilder output = new StringBuilder("Request URL " + url);
//                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
//                output.append(System.getProperty("line.separator") + "Type " + "GET");
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line = "";
//                StringBuilder responseOutput = new StringBuilder();
//                System.out.println("output===============" + br);
//                while((line = br.readLine()) != null ) {
//                    responseOutput.append(line);
//                }
//                br.close();
//
//                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                SubscribeActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        outputView.setText(output);
                        progressDialog.dismiss();

                    }
                });

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;

        }
    }


}
