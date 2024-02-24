package com.example.goldsignalpro.home;

import android.content.Context;

import com.example.goldsignalpro.model.AppVersionModel;
import com.example.goldsignalpro.model.LatestSignal;
import com.example.goldsignalpro.model.SignalsModel;

public interface HomeContact {
    interface View{
        void need_to_update_app(AppVersionModel appVersionModel);
        void app_already_updated();
        void renderSignalList(SignalsModel signalsModel);
        void renderNestedSignalList(SignalsModel signalsModel);
        void signal_fetch_error(String message);
        void showLatestSignal(LatestSignal latest_signal);
    }

    interface Presenter{
        void check_app_version(Context context, String installed_app_version);
        void getSignalList(Context context,String page_number);
        void getNextPageList(Context context,String page_number);
        void getLatest(Context context);
    }
}
