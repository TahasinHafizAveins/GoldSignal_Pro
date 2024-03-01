package com.example.goldsignalpro.signal_details;

import static com.example.goldsignalpro.utils.utils.calculateRemainingTime;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.goldsignalpro.R;
import com.example.goldsignalpro.home.HomeActivity;
import com.example.goldsignalpro.model.SignalsModel;
import com.example.goldsignalpro.utils.AdsManager;
import com.example.goldsignalpro.utils.SaveSignalData;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class SignalDetailsActivity extends AppCompatActivity implements SignalDetailContact.View {

    SignalsModel.Data data;
    SignalDetailsAdapter adapter;
    ShimmerFrameLayout shimmerLayout_details_top, shimmerLayout_details_list;
    SwipeRefreshLayout srl_detail;
    NestedScrollView nsv_detail;
    CardView ll_details_signal;
    RecyclerView rv_signals_details;
    TextView tv_signal_title,tv_details_signal_status, tv_signal_tp_1, tv_signal_tp_2, tv_signal_stop_lose, tv_signal_profit_status, tv_signal_details_profit_pips_title, tv_signal_profit_pips, tv_signal_time;
    RelativeLayout rl_signal_details_profit_pips;

    //ads
    InterstitialAd mInterstitialAd;

    int page = 1, limit = 1;
    SignalDetailsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_details);
        data = new SignalsModel.Data();

        initView();

        createInterstitialAds();

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

        mPresenter.getSignalDetails(SignalDetailsActivity.this, data.getId());
        mPresenter.getSignalList(SignalDetailsActivity.this, "1");
    }

    private void initView() {
        shimmerLayout_details_top = findViewById(R.id.shimmerLayout_details_top);
        shimmerLayout_details_list = findViewById(R.id.shimmerLayout_details);
        ll_details_signal = findViewById(R.id.ll_details_signal);
        rv_signals_details = findViewById(R.id.rv_signals_details);
        tv_signal_title = findViewById(R.id.tv_signal_details_title);
        tv_details_signal_status = findViewById(R.id.tv_details_signal_status);
        tv_signal_tp_1 = findViewById(R.id.tv_signal_details_tp_1);
        tv_signal_tp_2 = findViewById(R.id.tv_signal_details_tp_2);
        tv_signal_stop_lose = findViewById(R.id.tv_signal_details_stop_lose);
        tv_signal_profit_status = findViewById(R.id.tv_signal_details_profit_status);
        tv_signal_profit_pips = findViewById(R.id.tv_signal_details_profit_pips);
        tv_signal_details_profit_pips_title = findViewById(R.id.tv_signal_details_profit_pips_title);
        rl_signal_details_profit_pips = findViewById(R.id.rl_signal_details_profit_pips);
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
                    if (page > limit) {
                        Toast.makeText(SignalDetailsActivity.this, "DO Nothing", Toast.LENGTH_SHORT);
                    } else {
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
        startActivity(new Intent(SignalDetailsActivity.this, SignalDetailsActivity.class));
        finish();
    }

    @Override
    public void renderSignalDetails(SignalsModel.Data details_data) {
        Log.d("&*&*&^%%%%^^&*&^", "----------renderSignalDetails---------------");
        if (details_data != null) {
            Log.d("&*&*&^%%%%^^&*&^", "-------------renderSignalDetails------------" + details_data.toString());
            shimmerLayout_details_top.stopShimmer();
            shimmerLayout_details_top.setVisibility(View.GONE);
            ll_details_signal.setVisibility(View.VISIBLE);

            if (details_data.getTitle() != null) {
                Log.d("&*&*&^%%%%^^&*&^", "-------------renderSignalDetails------------" + details_data.getTitle());
                tv_signal_title.setText(details_data.getTitle());
            }

            if (details_data.getSignal_status() != null){
                tv_details_signal_status.setVisibility(View.VISIBLE);
                tv_details_signal_status.setText(details_data.getSignal_status().toUpperCase());
            }

            if (details_data.getTp_1() != null) {
                Log.d("&*&*&^%%%%^^&*&^", "-------------renderSignalDetails------------" + details_data.getTp_1());
                tv_signal_tp_1.setText(String.format("TP 1: %s", details_data.getTp_1()));
            }
            if (details_data.getTp_2() != null) {
                Log.d("&*&*&^%%%%^^&*&^", "-------------renderSignalDetails------------" + details_data.getTp_2());
                tv_signal_tp_2.setText(String.format("TP 2: %s", details_data.getTp_2()));
            }
            if (details_data.getStop_loss() != null) {
                Log.d("&*&*&^%%%%^^&*&^", "-------------renderSignalDetails------------" + details_data.getStop_loss());
                tv_signal_stop_lose.setText(String.format("Stop Loss: %s", details_data.getStop_loss()));
            }

            if (details_data.getProfit_status() != null) {
                Log.d("&*&*&^%%%%^^&*&^", "-------------renderSignalDetails------------" + details_data.getProfit_status());

                if (details_data.getProfit_status().trim().equals("Open")) {
                    rl_signal_details_profit_pips.setVisibility(View.GONE);
                    tv_signal_profit_status.setTextColor(Color.parseColor("#020202"));
                    tv_signal_profit_status.setText(String.format(" %s", details_data.getProfit_status()));


                } else if (details_data.getProfit_status().trim().equals("Profit")) {
                    rl_signal_details_profit_pips.setVisibility(View.VISIBLE);
                    tv_signal_profit_status.setTextColor(Color.parseColor("#5FAD56"));
                    tv_signal_profit_status.setText(String.format(" %s", details_data.getProfit_status().toUpperCase()));
                    if (details_data.getProfit_pips() != null) {
                        tv_signal_details_profit_pips_title.setText("Trade Profit:");
                        tv_signal_profit_pips.setTextColor(Color.parseColor("#5FAD56"));
                        tv_signal_profit_pips.setText(String.format(" %s", details_data.getProfit_pips()));
                    }
                } else if (details_data.getProfit_status().trim().equals("Loss")) {
                    rl_signal_details_profit_pips.setVisibility(View.VISIBLE);
                    tv_signal_profit_status.setTextColor(Color.parseColor("#D0312D"));
                    tv_signal_profit_status.setText(String.format(" %s", details_data.getProfit_status().toUpperCase()));
                    if (details_data.getProfit_pips() != null) {
                        tv_signal_details_profit_pips_title.setText("Trade Loss:");
                        tv_signal_profit_pips.setTextColor(Color.parseColor("#D0312D"));
                        tv_signal_profit_pips.setText(String.format(" %s", details_data.getProfit_pips()));
                    }
                }
            }
            if (details_data.getCreated_at() != null) {
                Log.d("&*&*&^%%%%^^&*&^", "-------------renderSignalDetails------------" + details_data.getCreated_at());
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


        if (signalsModel.getSignal_list() != null) {
            if (signalsModel.getSignal_list().size() != 0) {
                for (SignalsModel.Data signal : signalsModel.getSignal_list()) {
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

    private void createInterstitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(SignalDetailsActivity.this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                //Toast.makeText(HomeActivity.this, "failed: "+loadAdError.getCode(), Toast.LENGTH_SHORT).show();
                mInterstitialAd = null;
                loadActivity();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                //Toast.makeText(HomeActivity.this, "Ad loaded Successfully ", Toast.LENGTH_SHORT).show();
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("###$$$#!!!!!!!", "The ad was dismissed.");
                                loadActivity();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("###$$$#!!!!!!!", "The ad failed to show.");
                                loadActivity();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                Log.d("###$$$#!!!!!!!", "The ad was shown.");
                            }
                        });
                showAdsInt();
            }
        });
    }

    private void loadActivity() {
        if (SaveSignalData.getInstance().getData() != null) {
            data = SaveSignalData.getInstance().getData();
            loadData();
        }
    }

    public void showAdsInt(){

        if (mInterstitialAd != null){
            mInterstitialAd.show(SignalDetailsActivity.this);
        }
    }
}