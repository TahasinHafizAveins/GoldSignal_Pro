package com.example.goldsignalpro.signal_details;

import android.content.Context;

import com.example.goldsignalpro.model.SignalsModel;

import java.util.List;

public interface SignalDetailContact {
    interface View{
        void renderSignalDetails(SignalsModel.Data details_data);
        void renderSignalList(SignalsModel signalsModel);
        void showDetailsError();
        void showListError();
    }

    interface Presenter{
        void getSignalDetails(Context context,String signal_id);
        void getSignalList(Context context, String page);
    }
}
