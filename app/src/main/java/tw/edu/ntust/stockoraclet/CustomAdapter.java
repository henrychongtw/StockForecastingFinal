package tw.edu.ntust.stockoraclet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by henrychong on 2016/9/22.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private int[] mDataSetTypes;

    public static final int item1 = 0;
    public static final int item2 = 1;
    public static final int item3 = 2;
    public static final int item4 = 3;
    public static final int item5 = 4;
    public static final int item6 = 5;
    public static final int item7 = 6;
    public static final int item8 = 7;
    public static final int item9 = 8;



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class Item1ViewHolder extends ViewHolder {
        TextView temp;

        public Item1ViewHolder(View v) {
            super(v);
            this.temp = (TextView) v.findViewById(R.id.temp);
        }
    }

    public class Item2ViewHolder extends ViewHolder {
        TextView temp2;

        public Item2ViewHolder(View v) {
            super(v);
            this.temp2 = (TextView) v.findViewById(R.id.temp2);
        }
    }

    public class Item3ViewHolder extends ViewHolder {
        TextView temp3;

        public Item3ViewHolder(View v) {
            super(v);
            this.temp3 = (TextView) v.findViewById(R.id.temp3);
        }
    }

    public class Item4ViewHolder extends ViewHolder {
        TextView temp4;

        public Item4ViewHolder(View v) {
            super(v);
            this.temp4 = (TextView) v.findViewById(R.id.temp4);
        }
    }

    public class Item5ViewHolder extends ViewHolder {
        TextView temp5;

        public Item5ViewHolder(View v) {
            super(v);
            this.temp5 = (TextView) v.findViewById(R.id.temp5);
        }
    }

    public class Item6ViewHolder extends ViewHolder {
        TextView temp6;

        public Item6ViewHolder(View v) {
            super(v);
            this.temp6 = (TextView) v.findViewById(R.id.temp6);
        }
    }


    public class Item7ViewHolder extends ViewHolder {
        TextView temp7;

        public Item7ViewHolder(View v) {
            super(v);
            this.temp7 = (TextView) v.findViewById(R.id.temp7);
        }
    }

    public class Item8ViewHolder extends ViewHolder {
        TextView temp8;

        public Item8ViewHolder(View v) {
            super(v);
            this.temp8 = (TextView) v.findViewById(R.id.temp8);
        }
    }

    public class Item9ViewHolder extends ViewHolder {
        TextView temp9;

        public Item9ViewHolder(View v) {
            super(v);
            this.temp9 = (TextView) v.findViewById(R.id.temp9);
        }
    }



    public CustomAdapter(String[] dataSet, int[] dataSetTypes) {
        mDataSet = dataSet;
        mDataSetTypes = dataSetTypes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == item1) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item, viewGroup, false);

            return new Item1ViewHolder(v);
        } else if (viewType == item2) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item2, viewGroup, false);
            return new Item2ViewHolder(v);

        }else if (viewType == item3 ) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item3, viewGroup, false);
            return new Item3ViewHolder(v);
        }else if (viewType == item4 ) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item4, viewGroup, false);
            return new Item4ViewHolder(v);
        }else if (viewType == item5 ) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item5, viewGroup, false);
            return new Item5ViewHolder(v);
        }else if (viewType == item6 ) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item6, viewGroup, false);
            return new Item6ViewHolder(v);
        }else if (viewType == item7 ) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item7, viewGroup, false);
            return new Item7ViewHolder(v);
        }else if (viewType == item8 ) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item8, viewGroup, false);
            return new Item8ViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item9, viewGroup, false);
            return new Item9ViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == item1) {
            Item1ViewHolder holder = (Item1ViewHolder) viewHolder;
            holder.temp.setText(mDataSet[position]);
        }
        else if (viewHolder.getItemViewType() == item2) {
            Item2ViewHolder holder = (Item2ViewHolder) viewHolder;
            holder.temp2.setText(mDataSet[position]);
        }else if (viewHolder.getItemViewType() == item3) {
            Item3ViewHolder holder = (Item3ViewHolder) viewHolder;
            holder.temp3.setText(mDataSet[position]);
        }else if (viewHolder.getItemViewType() == item4) {
            Item4ViewHolder holder = (Item4ViewHolder) viewHolder;
            holder.temp4.setText(mDataSet[position]);
        }else if (viewHolder.getItemViewType() == item5) {
            Item5ViewHolder holder = (Item5ViewHolder) viewHolder;
            holder.temp5.setText(mDataSet[position]);
        }else if (viewHolder.getItemViewType() == item6) {
            Item6ViewHolder holder = (Item6ViewHolder) viewHolder;
            holder.temp6.setText(mDataSet[position]);
        }else if (viewHolder.getItemViewType() == item7) {
            Item7ViewHolder holder = (Item7ViewHolder) viewHolder;
            holder.temp7.setText(mDataSet[position]);
        }else if (viewHolder.getItemViewType() == item8) {
            Item8ViewHolder holder = (Item8ViewHolder) viewHolder;
            holder.temp8.setText(mDataSet[position]);
        }else {
            Item9ViewHolder holder = (Item9ViewHolder) viewHolder;
            holder.temp9.setText(mDataSet[position]);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }
}