package com.example.goldsignalpro.home;

import static com.example.goldsignalpro.utils.utils.calculateRemainingTime;
import static com.example.goldsignalpro.utils.utils.getFormattedTime;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goldsignalpro.R;
import com.example.goldsignalpro.model.SignalsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SignalAdapter extends RecyclerView.Adapter<SignalAdapter.SignalAdapterHolder> {

    private LayoutInflater inflater;
    private List<SignalsModel.Data> signalList;
    private HomeActivity activity;

    public SignalAdapter(HomeActivity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.signalList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SignalAdapter.SignalAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.signal_item,parent,false);
        return new SignalAdapter.SignalAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignalAdapter.SignalAdapterHolder holder, int position) {

        SignalsModel.Data signal = signalList.get(position);
        holder.bind(signal);
    }

    public void addSignals(SignalsModel.Data signal){
        signalList.add(signal);
        int position = signalList.indexOf(signal);
        notifyItemInserted(position);
    }

    public void clear(){
        int size = signalList.size();
        signalList.clear();
        notifyItemRangeRemoved(0,size);
    }

    @Override
    public int getItemCount() {
        return signalList.size();
    }

    public class SignalAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView  tv_signal_title,tv_signal_status_active, tv_signal_status_close, tv_profit_status,tv_signal_time;
        public SignalAdapterHolder(View view) {
            super(view);

            tv_signal_title = view.findViewById(R.id.tv_signal_title);
            tv_signal_status_active = view.findViewById(R.id.tv_signal_status_active);
            tv_signal_status_close = view.findViewById(R.id.tv_signal_status_close);
            tv_profit_status = view.findViewById(R.id.tv_profit_status);
            tv_signal_time = view.findViewById(R.id.tv_signal_time);

            view.setOnClickListener(this);
        }

        public void bind(SignalsModel.Data signal) {
            if (signal != null) {
                if (signal.getTitle()!= null) {
                    tv_signal_title.setText(signal.getTitle());
                }
                if (signal.getSignal_status()!= null) {
                    if (signal.getSignal_status().equals("Closed")) {
                        tv_signal_status_active.setVisibility(View.GONE);
                        tv_signal_status_close.setVisibility(View.VISIBLE);
                    }else if (signal.getSignal_status().equals("Active")){
                        tv_signal_status_active.setVisibility(View.VISIBLE);
                        tv_signal_status_close.setVisibility(View.GONE);
                    }else {
                        tv_signal_status_active.setVisibility(View.GONE);
                        tv_signal_status_close.setVisibility(View.GONE);
                    }
                }
                if (signal.getProfit_status() != null){
                    tv_profit_status.setVisibility(View.VISIBLE);
                    tv_profit_status.setText(String.format("Profit Status: %s", signal.getProfit_status()));
                }else {
                    tv_profit_status.setVisibility(View.GONE);
                }
                if (signal.getCreated_at()!= null) {
                    tv_signal_time.setText(String.format("%s ago", calculateRemainingTime(signal.getCreated_at())));
                }


            }

        }

        @Override
        public void onClick(View view) {
                activity.gotoDetailsActivity(signalList.get(getAdapterPosition()));
        }
    }


}
