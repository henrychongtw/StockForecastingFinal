package tw.edu.ntust.stockoraclet;

import android.animation.ObjectAnimator;
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
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import tw.edu.ntust.stockoraclet.loginpackage.helper.SQLiteHandler;
import tw.edu.ntust.stockoraclet.loginpackage.helper.SessionManager;
import tw.edu.ntust.stockoraclet.oracletpackage.SubscriptionInfo;
import tw.edu.ntust.stockoraclet.oracletpackage.common;

/**
 * Created by henrychong on 2016/5/26.
 */
public class Subscription extends Fragment{
    private final static String TAG = "SubscriptionJson";
    private Toolbar subscriptionToolbar;
    private ProgressDialog progressDialog;
    private AsyncTask retrieveSubscription;
    private ObjectAnimator animator;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView recyclerView;
    private String email;
    private SQLiteHandler db;
    private SessionManager session;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View subscription = inflater.inflate(R.layout.subscription_activity, container, false);

        recyclerView = (RecyclerView) subscription.findViewById(R.id.recycleSubscription);

        db = new SQLiteHandler(getActivity().getApplicationContext());
        // session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        email = user.get("email");
        Log.i("testing", email);
        Log.i("testing", "myemail");


        if (networkConnected()) {
            retrieveSubscription = new RetrieveSubscription().execute(common.URLAllOraclet + email);

        } else {
            showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return subscription;
    }

    public class RetrieveSubscription extends AsyncTask<String, Integer, List<SubscriptionInfo>> {
        @Override
        protected void onPostExecute(final List<SubscriptionInfo> result) {
            //Log.i("1111", result.get(1).getOccur_time());
            final SubscriptionListAdapter adapter = new SubscriptionListAdapter(getActivity().getLayoutInflater(), result);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setStackFromEnd(true);
            layoutManager.setReverseLayout(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
                private RecyclerView.ViewHolder vh;

                /*
                getMovementFlags用於設置是否處理拖拽事件和滑動事件，以及拖拽和滑動操作的方向，
                比如如果是列表類型的RecyclerView，拖拽只有UP、DOWN兩個方向，
                而如果是網格類型的則有UP、DOWN、LEFT、RIGHT四個方向。
                 */
                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }

                /*
                dragFlags 是拖拽標志，swipeFlags是滑動標志，我們把swipeFlags 都設置为0，表示不處理滑動操作。
                如果我們設置了非0的dragFlags ，那麼當我們長按item的時候就會進入拖拽並在拖拽過程中不斷回調onMove()方法，
                我們就在這個方法裏獲取當前拖拽的item和已經被拖拽到所處位置的item的ViewHolder，
                有了這2個ViewHolder，我們就可以交換他們的數據集並調用Adapter的notifyItemMoved方法來刷新item。
                 */
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    int fromPosition = viewHolder.getAdapterPosition();//得到拖動ViewHolder的position
                    int toPosition = target.getAdapterPosition();//得到目標ViewHolder的position
                    if (fromPosition < toPosition) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(result, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(result, i, i - 1);
                        }
                    }
                    adapter.notifyItemMoved(fromPosition, toPosition);
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                }

                //我們在開始拖曳的時候给item添加一個動畫效果，然後在拖曳完成的時候還原：
                //當長按選中item的時候（拖拽開始的時候）調用
                @Override
                public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                    super.onSelectedChanged(viewHolder, actionState);
                    if (viewHolder != null) {
                        vh = viewHolder;
                        pickUpAnimation(viewHolder.itemView);
                    }
                }
                //當手指鬆開的時候（拖拽完成的時候）調用
                @Override
                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    super.clearView(recyclerView, viewHolder);
                }
            });

            itemTouchHelper.attachToRecyclerView(recyclerView);

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
                Log.i("hereIsInput", "jsonIn" + jsonIn);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<List<SubscriptionInfo>>() {
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
        if (retrieveSubscription != null) {
            retrieveSubscription.cancel(true);
            retrieveSubscription = null;
        }
        super.onPause();
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    //按住物件時的浮起效果
    private void pickUpAnimation(View view) {
        animator = ObjectAnimator.ofFloat(view, "translationY", 1f, 35f, 1f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }


    private class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<SubscriptionInfo> subscriptionInfoList;

        public SubscriptionListAdapter(LayoutInflater layoutInflater, List<SubscriptionInfo> subscriptionInfoList) {
            this.layoutInflater = layoutInflater;
            this.subscriptionInfoList = subscriptionInfoList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(R.layout.recyclerview_subscription, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            final SubscriptionInfo theSubscribed = subscriptionInfoList.get(position);
            //viewHolder.tvSubscribedPredictPeople.setText(String.valueOf(theSubscribed.getPredict_people()));
            viewHolder.tvPredictorName.setText(theSubscribed.getP_name());
            viewHolder.tvPredictNumber.setText("驗證號碼: " + String.valueOf(theSubscribed.getO_number()));
            viewHolder.tvTargetName.setText(theSubscribed.getT_name());

            if(theSubscribed.getResult_status() == 1)
                viewHolder.tvSubResultStatus.setText("已驗證");
            else
                viewHolder.tvSubResultStatus.setText("未驗證");

            //retrieveCheckVerify = new RetrieveCheckVerify().execute(common.URLCheckVerify + theSubscribed.getO_number());

            //viewHolder.tvSubResultStatus.setText(String.valueOf(resultStore.get(0)));
            //viewHolder.tvSubResultStatus.setText("已驗證");
            //viewHolder.tvSubResultStatus.setText("未驗證");


            //Log.d("Fail", "V");

            //Double accu;

            /*
            if(theSubscribed.getAccuracy() != null){
                accu = Double.parseDouble(theSubscribed.getAccuracy());
                accu = accu*100;
                viewHolder.tvAccuracy2.setText(String.format("%.2f",accu)+"%");
            }else{
                viewHolder.tvAccuracy2.setText("0.00%");
            }
            String check;
            if(theSubscribed.getNotification() == 1){
                check = "New Update!";
                viewHolder.tvNotification.setText(String.valueOf(check));
                viewHolder.tvNotification.setTextColor(Color.YELLOW);
            }else{
                check = "No Update";
                viewHolder.tvNotification.setText(String.valueOf(check));
                viewHolder.tvNotification.setTextColor(Color.WHITE);
            }
            */

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    if(theSubscribed.getNotification() == 1) {
                        theSubscribed.setNotification(0);
                        notifyDataSetChanged();
                    }
                    */
                    //String oracletNumber = viewHolder.tvOracletName.getText().toString();
                    int oracletNumber = theSubscribed.getO_number();
                    //Log.d("Sub_OracletNumber=", oracletNumber);
                    Intent intent = new Intent(getActivity(), OracletPage.class);
                    //Log.i("what the heck?", oracletNumber);
                    Bundle bundle = new Bundle();
                    bundle.putInt("number", oracletNumber);
                    Log.i("what the heck?", bundle.toString());

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return subscriptionInfoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            //TextView tvSubscribedPredictPeople, tvNotification , tvAccuracy2;
            TextView tvPredictorName, tvPredictNumber, tvSubResultStatus, tvTargetName;

            public ViewHolder(View itemView) {
                super(itemView);
                //tvSubscribedPredictPeople = (TextView) itemView.findViewById(R.id.tvSubscribedPredictPeople);
                //tvNotification = (TextView) itemView.findViewById(R.id.tvNotification);
                //tvAccuracy2 = (TextView) itemView.findViewById(R.id.tvAccuracy2);
                tvPredictorName = (TextView) itemView.findViewById(R.id.tvPredictorName);
                tvPredictNumber = (TextView) itemView.findViewById(R.id.tvPredictNumber);
                tvTargetName = (TextView) itemView.findViewById(R.id.tvTargetName);
                tvSubResultStatus = (TextView) itemView.findViewById(R.id.tvSubResultStatus);
            }
        }
    }
}
