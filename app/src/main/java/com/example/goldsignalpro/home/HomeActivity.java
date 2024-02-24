package com.example.goldsignalpro.home;

import static com.example.goldsignalpro.utils.utils.calculateRemainingTime;
import static com.example.goldsignalpro.utils.utils_string.ADMOB_APP_ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.goldsignalpro.settings.SettingsActivity;
import com.example.goldsignalpro.signal_details.SignalDetailsActivity;
import com.example.goldsignalpro.utils.AdsManager;
import com.example.goldsignalpro.utils.SaveSignalData;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;


public class HomeActivity extends AppCompatActivity implements HomeContact.View {
    Toolbar toolbar;
    RecyclerView rv_signals;
    TextView tv_signal_title, tv_signal_time, tv_latest_signal_status;
    ImageView iv_settings, iv_rate_us, iv_share;
    AdView adView;
    InterstitialAd mInterstitialAd;
    CardView ll_latest_signal;
    SignalAdapter signalAdapter;
    NestedScrollView nsv_home;
    SwipeRefreshLayout srl_home;
    SignalsModel.Data selected_signal;
    HomePresenter mPresenter;
    int page = 1, limit = 1;
    LatestSignal latestSignal = new LatestSignal();
    private ShimmerFrameLayout shimmerFrameLayout, shimmerLayout_latest;
    private boolean is_resume = false;
    private boolean rate_us_showing = false;

    private FirebaseAnalytics mFirebaseAnalytics;
    private AdsManager adsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tool_bar_setup();
        loadAds();
        mPresenter = new HomePresenter(HomeActivity.this);
        mPresenter.check_app_version(HomeActivity.this, BuildConfig.VERSION_NAME);

    }

    private void loadAds() {
        adView = findViewById(R.id.adView);
        adsManager = new AdsManager(HomeActivity.this);
        adsManager.createAds(adView);
        createInterstitialAds();

    }

    private void createInterstitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(HomeActivity.this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                //Toast.makeText(HomeActivity.this, "failed: "+loadAdError.getCode(), Toast.LENGTH_SHORT).show();
                mInterstitialAd = null;
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
                                startActivity();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("###$$$#!!!!!!!", "The ad failed to show.");
                                startActivity();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                Log.d("###$$$#!!!!!!!", "The ad was shown.");
                            }
                        });
            }
        });
    }

    private void tool_bar_setup() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        toolbar.setTitle("GOLD SIGNALS");
    }

    @Override
    protected void onPause() {
        super.onPause();
        is_resume = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        rate_us_showing = false;
        if (is_resume) {
            mPresenter = new HomePresenter(HomeActivity.this);
            mPresenter.check_app_version(HomeActivity.this, BuildConfig.VERSION_NAME);
        }
        firebaseInit();
    }

    private void firebaseInit() {
        FirebaseApp.initializeApp(HomeActivity.this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(HomeActivity.this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "screen class");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "custom screen name");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,bundle);
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
        tv_latest_signal_status = findViewById(R.id.tv_latest_signal_status);
        tv_signal_time = findViewById(R.id.tv_latest_signal_time);
        ll_latest_signal = findViewById(R.id.ll_latest_signal);
        iv_settings = findViewById(R.id.iv_settings);
        iv_rate_us = findViewById(R.id.iv_rate_us);
        iv_share = findViewById(R.id.iv_share);

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
                        mPresenter.getNextPageList(HomeActivity.this, String.valueOf(page));
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

        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });
        iv_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate_us();
            }
        });
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        ll_latest_signal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latestSignal != null) {
                    SignalsModel.Data selected_data = new SignalsModel.Data();

                    selected_data.setId(latestSignal.getId());
                    selected_data.setTitle(latestSignal.getTitle());

                    selected_data.setSignal_status(latestSignal.getSignal_status());
                    selected_data.setProfit_status(latestSignal.getProfit_status());
                    selected_data.setCreated_at(latestSignal.getCreated_at());
                    selected_data.setSignal_description(latestSignal.getSignal_description());
                    selected_data.setTp_1(latestSignal.getTp_1());
                    selected_data.setTp_2(latestSignal.getTp_2());
                    selected_data.setStop_loss(latestSignal.getStop_loss());
                    selected_data.setProfit_pips(latestSignal.getProfit_pips());
                    selected_data.setPips(latestSignal.getPips());

                    gotoDetailsActivity(selected_data);
                }
            }
        });
    }

    private void rate_us() {
        if (!rate_us_showing) {
            rate_us_showing = true;


            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);

            alert.setTitle("Rate Us!");
            alert.setMessage(getResources().getString(R.string.app_rate_us_title));
            alert.setPositiveButton(R.string.app_rate_us,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rate_us_showing = false;
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goldsignalpro.com/")));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        }
                    });
            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            rate_us_showing = false;
                            dialog.cancel();
                        }
                    });


            AlertDialog alertDialog = alert.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener(){

                @Override
                public void onShow(DialogInterface dialog) {

                    Button positive= alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setFocusable(true);
                    positive.setFocusableInTouchMode(true);
                    positive.requestFocus();
                }
            });

            alertDialog.show();
        }
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
    public void renderNestedSignalList(SignalsModel signalsModel) {
        limit = signalsModel.getLast_page() != null ? Integer.parseInt(signalsModel.getLast_page()) : 1;
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
            latestSignal = new LatestSignal();
            latestSignal = latest_signal;

            shimmerLayout_latest.stopShimmer();
            shimmerLayout_latest.setVisibility(View.GONE);
            ll_latest_signal.setVisibility(View.VISIBLE);

            if (latest_signal.getTitle() != null) {
                tv_signal_title.setText(latest_signal.getTitle());
            }
            if (latest_signal.getSignal_status() != null) {
                tv_latest_signal_status.setVisibility(View.VISIBLE);
                tv_latest_signal_status.setText(latest_signal.getSignal_status().toUpperCase());
            }
            if (latest_signal.getCreated_at() != null) {
                tv_signal_time.setText(calculateRemainingTime(latest_signal.getCreated_at()) + " ago");
            }
        }

        mPresenter.getSignalList(HomeActivity.this, String.valueOf(page));
    }

    public void gotoDetailsActivity(SignalsModel.Data selected_signal) {
        this.selected_signal = selected_signal;
        SaveSignalData.getInstance().setData(selected_signal);
        showAdsInt();
    }

    public void startActivity(){
        startActivity(new Intent(HomeActivity.this, SignalDetailsActivity.class));
    }

    public void showAdsInt(){

        if (mInterstitialAd != null){
            mInterstitialAd.show(HomeActivity.this);
        }
    }
}