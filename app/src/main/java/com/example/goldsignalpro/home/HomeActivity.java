package com.example.goldsignalpro.home;

import static com.example.goldsignalpro.utils.utils.calculateRemainingTime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.goldsignalpro.BuildConfig;
import com.example.goldsignalpro.R;
import com.example.goldsignalpro.model.AppVersionModel;
import com.example.goldsignalpro.model.LatestSignal;
import com.example.goldsignalpro.model.SignalsModel;
import com.example.goldsignalpro.signal_details.SignalDetailsActivity;
import com.example.goldsignalpro.utils.SaveSignalData;
import com.facebook.shimmer.ShimmerFrameLayout;

public class HomeActivity extends AppCompatActivity implements HomeContact.View {

    RecyclerView rv_signals;
    TextView tv_signal_title, tv_signal_time;
    CardView ll_latest_signal;
    SignalAdapter signalAdapter;
    NestedScrollView nsv_home;
    SwipeRefreshLayout srl_home;

    HomePresenter mPresenter;
    int page = 1, limit = 1;
    private ShimmerFrameLayout shimmerFrameLayout, shimmerLayout_latest;
    private boolean is_resume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new HomePresenter(HomeActivity.this);
        mPresenter.check_app_version(HomeActivity.this, BuildConfig.VERSION_NAME);

    }

    @Override
    protected void onPause() {
        super.onPause();
        is_resume = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (is_resume) {
            mPresenter = new HomePresenter(HomeActivity.this);
            mPresenter.check_app_version(HomeActivity.this, BuildConfig.VERSION_NAME);
        }
    }

    private void loadData() {
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerLayout_latest = findViewById(R.id.shimmerLayout_latest);

        ll_latest_signal.setVisibility(View.INVISIBLE);
        rv_signals.setVisibility(View.GONE);

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        shimmerLayout_latest.setVisibility(View.VISIBLE);
        shimmerLayout_latest.startShimmer();

        mPresenter.getLatest(HomeActivity.this);
    }

    private void initView() {
        nsv_home = findViewById(R.id.nsv_home);
        srl_home = findViewById(R.id.srl_home);
        rv_signals = findViewById(R.id.rv_signals);
        tv_signal_title = findViewById(R.id.tv_latest_signal_title);
        tv_signal_time = findViewById(R.id.tv_latest_signal_time);
        ll_latest_signal = findViewById(R.id.ll_latest_signal);

        signalAdapter = new SignalAdapter(HomeActivity.this);

        setupAdapter();

        loadData();
        // adding on scroll change listener method for our nested scroll view.
        nsv_home.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    if (page > limit) {
                        Toast.makeText(HomeActivity.this, "DO Nothing", Toast.LENGTH_SHORT);
                    } else {
                        mPresenter.getSignalList(HomeActivity.this, String.valueOf(page));
                    }
                }
            }
        });

        // SetOnRefreshListener on SwipeRefreshLayout
        srl_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_home.setRefreshing(false);
                page = 1;
                initView();
            }
        });

    }

    private void setupAdapter() {
        // setting layout manager to our recycler view.
        rv_signals.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

        // setting adapter to our recycler view.
        rv_signals.setAdapter(signalAdapter);
    }

    private void checkInstalledAppVersion() {

    }

    @Override
    public void need_to_update_app(AppVersionModel appVersionModel) {
        new android.app.AlertDialog.Builder(HomeActivity.this).setMessage(getString(R.string.app_update_notification)).setCancelable(false).setTitle("")
                .setPositiveButton(R.string.update, (dialog, which) -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appVersionModel.getApp_link())));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }).show();
    }

    @Override
    public void app_already_updated() {
        initView();
    }

    @Override
    public void renderSignalList(SignalsModel signalsModel) {
        limit = 1;
        limit = signalsModel.getLast_page() != null ? Integer.parseInt(signalsModel.getLast_page()) : 1;
        SaveSignalData.getInstance().setSignalsModel(signalsModel);
        signalAdapter.clear();

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        rv_signals.setVisibility(View.VISIBLE);


        if (signalsModel.getSignal_list() != null) {
            if (signalsModel.getSignal_list().size() != 0) {
                for (SignalsModel.Data signal : signalsModel.getSignal_list()) {
                    signalAdapter.addSignals(signal);
                }
            }
        }
    }

    @Override
    public void signal_fetch_error(String message) {

    }

    @Override
    public void showLatestSignal(LatestSignal latest_signal) {
        if (latest_signal != null) {
            shimmerLayout_latest.stopShimmer();
            shimmerLayout_latest.setVisibility(View.GONE);
            ll_latest_signal.setVisibility(View.VISIBLE);

            if (latest_signal.getTitle() != null) {
                tv_signal_title.setText(latest_signal.getTitle());
            }
            if (latest_signal.getCreated_at() != null) {
                tv_signal_time.setText(calculateRemainingTime(latest_signal.getCreated_at()) + " ago");
            }
        }

        mPresenter.getSignalList(HomeActivity.this, String.valueOf(page));
    }

    public void gotoDetailsActivity(SignalsModel.Data selected_signal) {
        SaveSignalData.getInstance().setData(selected_signal);
        startActivity(new Intent(HomeActivity.this, SignalDetailsActivity.class));
    }
}