package com.example.goldsignalpro.signal_details;

import static com.example.goldsignalpro.utils.utils.calculateRemainingTime;

import android.content.Intent;
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

import com.example.goldsignalpro.R;
import com.example.goldsignalpro.home.HomeActivity;
import com.example.goldsignalpro.model.SignalsModel;
import com.example.goldsignalpro.utils.SaveSignalData;
import com.facebook.shimmer.ShimmerFrameLayout;

public class SignalDetailsActivity extends AppCompatActivity implements SignalDetailContact.View {

    SignalsModel.Data data;
    SignalDetailsAdapter adapter;
    ShimmerFrameLayout shimmerLayout_details_top, shimmerLayout_details_list;
    SwipeRefreshLayout srl_detail;
    NestedScrollView nsv_detail;
    CardView ll_details_signal;
    RecyclerView rv_signals_details;
    TextView tv_signal_title, tv_signal_tp_1, tv_signal_tp_2, tv_signal_stop_lose, tv_signal_profit_status, tv_signal_profit_pips, tv_signal_time;

    int page = 1, limit = 1;
    SignalDetailsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_details);
        data = new SignalsModel.Data();

        initView();

        if (SaveSignalData.getInstance().getData() != null) {
            data = SaveSignalData.getInstance().getData();
            loadData();
        }

    }

    private void loadData() {

        ll_details_signal.setVisibility(View.INVISIBLE);
        rv_signals_details.setVisibility(View.GONE);

        shimmerLayout_details_top.setVisibility(View.VISIBLE);
        shimmerLayout_details_top.startShimmer();
        shimmerLayout_details_list.setVisibility(View.VISIBLE);
        shimmerLayout_details_list.startShimmer();

        if (data != null) {
            renderSignalDetails(data);
        }
        renderSignalList(SaveSignalData.getInstance().getSignalsModel());
    }

    private void reloadData() {
        ll_details_signal.setVisibility(View.INVISIBLE);
        rv_signals_details.setVisibility(View.GONE);

        shimmerLayout_details_top.setVisibility(View.VISIBLE);
        shimmerLayout_details_top.startShimmer();
        shimmerLayout_details_list.setVisibility(View.VISIBLE);
        shimmerLayout_details_list.startShimmer();

        mPresenter.getSignalDetails(SignalDetailsActivity.this,data.getId());
        mPresenter.getSignalList(SignalDetailsActivity.this,"1");
    }

    private void initView() {
        shimmerLayout_details_top = findViewById(R.id.shimmerLayout_details_top);
        shimmerLayout_details_list = findViewById(R.id.shimmerLayout_details);
        ll_details_signal = findViewById(R.id.ll_details_signal);
        rv_signals_details = findViewById(R.id.rv_signals_details);
        tv_signal_title = findViewById(R.id.tv_signal_details_title);
        tv_signal_tp_1 = findViewById(R.id.tv_signal_details_tp_1);
        tv_signal_tp_2 = findViewById(R.id.tv_signal_details_tp_2);
        tv_signal_stop_lose = findViewById(R.id.tv_signal_details_stop_lose);
        tv_signal_profit_status = findViewById(R.id.tv_signal_details_profit_status);
        tv_signal_profit_pips = findViewById(R.id.tv_signal_details_profit_pips);
        tv_signal_time = findViewById(R.id.tv_signal_details_time);
        srl_detail = findViewById(R.id.srl_detail);
        nsv_detail = findViewById(R.id.nsv_detail);

        mPresenter = new SignalDetailsPresenter(SignalDetailsActivity.this);
        adapter = new SignalDetailsAdapter(SignalDetailsActivity.this);
        setupAdapter();


        // adding on scroll change listener method for our nested scroll view.
        nsv_detail.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    if (page > limit){
                        Toast.makeText(SignalDetailsActivity.this,"DO Nothing",Toast.LENGTH_SHORT);
                    }else {
                        mPresenter.getSignalList(SignalDetailsActivity.this, String.valueOf(page));
                    }
                }
            }
        });

        // SetOnRefreshListener on SwipeRefreshLayout
        srl_detail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_detail.setRefreshing(false);
                reloadData();
            }
        });
    }


    private void setupAdapter() {
        // setting layout manager to our recycler view.
        rv_signals_details.setLayoutManager(new LinearLayoutManager(SignalDetailsActivity.this));

        // setting adapter to our recycler view.
        rv_signals_details.setAdapter(adapter);
    }

    public void gotoDetailsActivity(SignalsModel.Data data) {
        SaveSignalData.getInstance().setData(data);
        startActivity(new Intent(SignalDetailsActivity.this,SignalDetailsActivity.class));
        finish();
    }

    @Override
    public void renderSignalDetails(SignalsModel.Data details_data) {
        Log.d("&*&*&^%%%%^^&*&^","----------renderSignalDetails---------------");
        if (details_data != null){
            Log.d("&*&*&^%%%%^^&*&^","-------------renderSignalDetails------------"+details_data.toString());
            shimmerLayout_details_top.stopShimmer();
            shimmerLayout_details_top.setVisibility(View.GONE);
            ll_details_signal.setVisibility(View.VISIBLE);

            if (details_data.getTitle() != null) {
                Log.d("&*&*&^%%%%^^&*&^","-------------renderSignalDetails------------"+details_data.getTitle());
                tv_signal_title.setText(details_data.getTitle());
            }
            if (details_data.getTp_1() != null) {
                Log.d("&*&*&^%%%%^^&*&^","-------------renderSignalDetails------------"+details_data.getTp_1());
                tv_signal_tp_1.setText(String.format("TP_1: %s", details_data.getTp_1()));
            }
            if (details_data.getTp_2() != null) {
                Log.d("&*&*&^%%%%^^&*&^","-------------renderSignalDetails------------"+details_data.getTp_2());
                tv_signal_tp_2.setText(String.format("TP_2: %s", details_data.getTp_2()));
            }
            if (details_data.getStop_loss()!= null) {
                Log.d("&*&*&^%%%%^^&*&^","-------------renderSignalDetails------------"+details_data.getStop_loss());
                tv_signal_stop_lose.setText(String.format("Stop Loss: %s", details_data.getStop_loss()));
            }

            if (details_data.getProfit_status() != null) {
                Log.d("&*&*&^%%%%^^&*&^","-------------renderSignalDetails------------"+details_data.getProfit_status());
                tv_signal_profit_status.setText(String.format("Profit Status: %s", details_data.getProfit_status()));
            }

            if (details_data.getProfit_pips() != null) {
                Log.d("&*&*&^%%%%^^&*&^","-------------renderSignalDetails------------"+details_data.getProfit_pips());
                tv_signal_profit_pips.setText(String.format("Profit pips: %s", details_data.getProfit_pips()));
            }
            if (details_data.getCreated_at() != null) {
                Log.d("&*&*&^%%%%^^&*&^","-------------renderSignalDetails------------"+details_data.getCreated_at());
                tv_signal_time.setText(String.format("%s ago", calculateRemainingTime(details_data.getCreated_at())));
            }
        }
    }

    @Override
    public void renderSignalList(SignalsModel signalsModel) {
        limit = 1;
        limit = signalsModel.getLast_page() != null ? Integer.parseInt(signalsModel.getLast_page()) : 1;

        SaveSignalData.getInstance().setSignalsModel(signalsModel);
        adapter.clear();

        shimmerLayout_details_list.stopShimmer();
        shimmerLayout_details_list.setVisibility(View.GONE);
        rv_signals_details.setVisibility(View.VISIBLE);


        if (signalsModel.getSignal_list() != null){
            if (signalsModel.getSignal_list().size() != 0){
                for (SignalsModel.Data signal : signalsModel.getSignal_list()){
                    adapter.addSignals(signal);
                }
            }
        }
    }

    @Override
    public void showDetailsError() {
        shimmerLayout_details_top.stopShimmer();
        shimmerLayout_details_top.setVisibility(View.GONE);
    }

    @Override
    public void showListError() {
        shimmerLayout_details_list.stopShimmer();
        shimmerLayout_details_list.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignalDetailsActivity.this, HomeActivity.class));
        finish();
    }
}