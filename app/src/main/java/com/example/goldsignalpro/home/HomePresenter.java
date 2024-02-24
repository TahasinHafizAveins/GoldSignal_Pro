package com.example.goldsignalpro.home;

import static com.example.goldsignalpro.utils.utils.compareVersionNames;

import android.content.Context;
import android.util.Log;

import com.example.goldsignalpro.BuildConfig;
import com.example.goldsignalpro.model.AppVersionModel;
import com.example.goldsignalpro.model.LatestSignal;
import com.example.goldsignalpro.model.SignalsModel;
import com.example.goldsignalpro.utils.ApiEndPoints;
import com.example.goldsignalpro.utils.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter implements HomeContact.Presenter{

    HomeContact.View mView;

    public HomePresenter(HomeContact.View mView) {
        this.mView = mView;
    }

    @Override
    public void check_app_version(Context context, String installed_app_version) {
       ApiEndPoints call = RetrofitService.createService(context,ApiEndPoints.class);
       call.getAppVersion().enqueue(new Callback<AppVersionModel>() {
           @Override
           public void onResponse(Call<AppVersionModel> call, Response<AppVersionModel> response) {
               if (response.isSuccessful()){
                   if (response.body() != null){
                       AppVersionModel appVersionModel = new AppVersionModel();
                       appVersionModel.setVersion(response.body().getVersion());
                       appVersionModel.setApp_link(response.body().getApp_link());

                       int version_diff = compareVersionNames(installed_app_version,appVersionModel.getVersion().toString().trim() );
                       if (version_diff == -1) {
                           mView.need_to_update_app(appVersionModel);
                       } else {
                           mView.app_already_updated();
                       }
                   }
               }else {
                   mView.app_already_updated();
               }
           }

           @Override
           public void onFailure(Call<AppVersionModel> call, Throwable t) {
               mView.app_already_updated();
           }
       });



    }

    @Override
    public void getSignalList(Context context,String page_number) {
        Log.d("^^&&&&%%%%%5","-------getSignalList---------");
        ApiEndPoints call = RetrofitService.createService(context,ApiEndPoints.class);
        call.getSignals(page_number).enqueue(new Callback<SignalsModel>() {
            @Override
            public void onResponse(Call<SignalsModel> call, Response<SignalsModel> response) {
                if (response.isSuccessful()){
                    Log.d("%%%%%%%%%%%%%%%","-------getSignalList-----isSuccessful----");
                    if (response.body() != null){
                        Log.d("%%%%%%%%%%%%%%%","-------getSignalList-----isSuccessful----"+response.body().toString());
                        mView.renderSignalList(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<SignalsModel> call, Throwable t) {
                Log.d("%%%%%%%%%%%%%%%","-------getSignalList-onFailure--------");
            }
        });

    }

    @Override
    public void getNextPageList(Context context, String page_number) {
        Log.d("^^&&&&%%%%%5","-------getNextPageList---------");
        ApiEndPoints call = RetrofitService.createService(context,ApiEndPoints.class);
        call.getSignals(page_number).enqueue(new Callback<SignalsModel>() {
            @Override
            public void onResponse(Call<SignalsModel> call, Response<SignalsModel> response) {
                if (response.isSuccessful()){
                    Log.d("%%%%%%%%%%%%%%%","-------getNextPageList-----isSuccessful----");
                    if (response.body() != null){
                        Log.d("%%%%%%%%%%%%%%%","-------getNextPageList-----isSuccessful----"+response.body().toString());
                        mView.renderNestedSignalList(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<SignalsModel> call, Throwable t) {
                Log.d("%%%%%%%%%%%%%%%","-------getNextPageList-onFailure--------");
            }
        });
    }

    @Override
    public void getLatest(Context context) {
        ApiEndPoints call = RetrofitService.createService(context,ApiEndPoints.class);
        call.getLatestSignal().enqueue(new Callback<LatestSignal>() {
            @Override
            public void onResponse(Call<LatestSignal> call, Response<LatestSignal> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mView.showLatestSignal(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<LatestSignal> call, Throwable t) {

            }
        });
    }
}
