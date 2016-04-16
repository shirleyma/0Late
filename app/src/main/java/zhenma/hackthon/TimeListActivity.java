package zhenma.hackthon;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.race604.flyrefresh.FlyRefreshLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.util.Log;

/**
 * Created by xuehanyu on 4/13/16.
 */
public class TimeListActivity extends AppCompatActivity implements FlyRefreshLayout.OnPullRefreshListener {
    private FlyRefreshLayout mFlylayout;
    private RecyclerView mListView;

    private ItemAdapter mAdapter;

    private ArrayList<ItemData> mDataSet = new ArrayList<>();
    private Handler mHandler = new Handler();
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initDataSet();
        setContentView(R.layout.time_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mFlylayout = (FlyRefreshLayout) findViewById(R.id.fly_layout);

        mFlylayout.setOnPullRefreshListener(this);

        mListView = (RecyclerView) findViewById(R.id.list);

        mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);
        mAdapter = new ItemAdapter(this);

        mListView.setAdapter(mAdapter);

        mListView.setItemAnimator(new TimeListItem());

        View actionButton = mFlylayout.getHeaderActionButton();
        if (actionButton != null) {
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFlylayout.startRefresh();
                }
            });
        }
    }

//    private void initDataSet() {
//        mDataSet.add(new ItemData(Color.parseColor("#76A9FC"), R.mipmap.ic_assessment_white_24dp, "Meeting Minutes", new Date(2014 - 1900, 2, 9)));
//        mDataSet.add(new ItemData(Color.GRAY, R.mipmap.ic_assessment_white_24dp, "Favorites Photos", new Date(2014 - 1900, 1, 3)));
//        mDataSet.add(new ItemData(Color.GRAY, R.mipmap.ic_assessment_white_24dp, "Photos", new Date(2014 - 1900, 0, 9)));
//    }

    private void addItemData() {
//        ItemData itemData = new ItemData(Color.parseColor("#FFC970"), R.mipmap.ic_assessment_white_24dp, "Magic Cube Show", new Date());
//        mDataSet.add(0, itemData);
//        mAdapter.notifyItemInserted(0);
//        mLayoutManager.scrollToPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onRefresh(FlyRefreshLayout view) {
        View child = mListView.getChildAt(0);
        if (child != null) {
            bounceAnimateView(child.findViewById(R.id.icon));
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlylayout.onRefreshFinish();
            }
        }, 2000);
    }

    private void bounceAnimateView(View view) {
        if (view == null) {
            return;
        }

        Animator swing = ObjectAnimator.ofFloat(view, "rotationX", 0, 30, -20, 0);
        swing.setDuration(400);
        swing.setInterpolator(new AccelerateInterpolator());
        swing.start();
    }

    @Override
    public void onRefreshAnimationEnd(FlyRefreshLayout view) {
        addItemData();
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private LayoutInflater mInflater;
        private DateFormat dateFormat;

        public ItemAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.time_list_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            final ItemData data = mDataSet.get(i);
            ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setColor(data.color);
            itemViewHolder.icon.setBackgroundDrawable(drawable);
            itemViewHolder.icon.setImageResource(data.icon);
            itemViewHolder.title.setText(data.title);
            itemViewHolder.subTitle.setText(dateFormat.format(data.time));
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        TextView subTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView) itemView.findViewById(R.id.subtitle);
        }

    }

    private void resetTrans(android.widget.LinearLayout l){
        ((android.widget.ImageView)l.getChildAt(0)).setImageResource(R.mipmap.walk_g);
        ((android.widget.ImageView)l.getChildAt(1)).setImageResource(R.mipmap.drive_g);
        ((android.widget.ImageView)l.getChildAt(2)).setImageResource(R.mipmap.bus_g);
    }

    public void clickWalk(View v){
        resetTrans((android.widget.LinearLayout)v.getParent());
        ((android.widget.ImageView)v).setImageResource(R.mipmap.walk_c);
        android.widget.RelativeLayout l = (android.widget.RelativeLayout)v.getParent().getParent();
        android.widget.TextView textView= (android.widget.TextView)l.getChildAt(1);
        Log.d("click", "walk:" + textView.getText());
    }

    public void clickDrive(View v){
        resetTrans((android.widget.LinearLayout)v.getParent());
        ((android.widget.ImageView)v).setImageResource(R.mipmap.drive_c);
        android.widget.RelativeLayout l = (android.widget.RelativeLayout)v.getParent().getParent();
        android.widget.TextView textView= (android.widget.TextView)l.getChildAt(1);
        Log.d("click", "Drive:" + textView.getText());
    }

    public void clickBus(View v){
        resetTrans((android.widget.LinearLayout)v.getParent());
        ((android.widget.ImageView)v).setImageResource(R.mipmap.bus_c);
        android.widget.RelativeLayout l = (android.widget.RelativeLayout) v.getParent().getParent();
        android.widget.TextView textView= (android.widget.TextView)l.getChildAt(1);
        Log.d("click", "Bus:" + textView.getText());
    }
}
