package com.example.goldsignalpro.signal_details;

import android.content.Context;
import android.util.Log;

import com.example.goldsignalpro.model.SignalsModel;
import com.example.goldsignalpro.utils.ApiEndPoints;
import com.example.goldsignalpro.utils.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignalDetailsPresenter implements SignalDetailContact.Presenter{

    SignalDetailContact.View mView;

    public SignalDetailsPresenter(SignalDetailContact.View mView) {
        this.mView = mView;
    }

    @Override
    public void getSignalDetails(Context context, String signal_id) {
        ApiEndPoints call = RetrofitService.createService(context,ApiEndPoints.class);
        call.getSignalDetails(signal_id).enqueue(new Callback<SignalsModel.Data>() {
            @Override
            public void onResponse(Call<SignalsModel.Data> call, Response<SignalsModel.Data> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mView.renderSignalDetails(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<SignalsModel.Data> call, Throwable t) {

            }
        });


    }

    @Override
    public void getSignalList(Context context, String page_number) {
        Log.d("%%%%%%%%%%%%%%%","-------getSignalList---------");
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
}
