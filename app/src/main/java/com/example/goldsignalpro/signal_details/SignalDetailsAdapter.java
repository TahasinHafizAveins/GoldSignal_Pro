package com.example.goldsignalpro.signal_details;

import static com.example.goldsignalpro.utils.utils.calculateRemainingTime;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goldsignalpro.R;
import com.example.goldsignalpro.model.SignalsModel;

import java.util.ArrayList;
import java.util.List;

public class SignalDetailsAdapter extends RecyclerView.Adapter<SignalDetailsAdapter.SignalDetailsAdapterHolder> {

    private LayoutInflater inflater;
    private List<SignalsModel.Data> signalList;
    private SignalDetailsActivity activity;

    public SignalDetailsAdapter(SignalDetailsActivity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.signalList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SignalDetailsAdapter.SignalDetailsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.signal_item,parent,false);
        return new SignalDetailsAdapter.SignalDetailsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignalDetailsAdapter.SignalDetailsAdapterHolder holder, int position) {

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

    public class SignalDetailsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView tv_signal_title,tv_signal_status_active, tv_signal_status_close, tv_profit_status,tv_signal_open_profit_pips_label,tv_signal_open_profit_pips,tv_signal_time;
        RelativeLayout rl_pips;

        public SignalDetailsAdapterHolder(View view) {
            super(view);

            tv_signal_title = view.findViewById(R.id.tv_signal_title);
            tv_signal_status_active = view.findViewById(R.id.tv_signal_status_active);
            tv_signal_status_close = view.findViewById(R.id.tv_signal_status_close);
            tv_profit_status = view.findViewById(R.id.tv_profit_status);
            tv_signal_open_profit_pips_label = view.findViewById(R.id.tv_signal_open_profit_pips_label);
            tv_signal_open_profit_pips = view.findViewById(R.id.tv_signal_open_profit_pips);
            tv_signal_time = view.findViewById(R.id.tv_signal_time);
            rl_pips = view.findViewById(R.id.rl_pips);

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
                    rl_pips.setVisibility(View.GONE);
                    if (signal.getProfit_status().trim().equals("Open")) {
                        tv_profit_status.setTextColor(Color.parseColor("#FFAAAAAA"));
                        tv_profit_status.setText(String.format(" %s", signal.getProfit_status()));


                    } else if (signal.getProfit_status().trim().equals("Profit")) {
                        tv_profit_status.setTextColor(Color.parseColor("#5FAD56"));
                        tv_profit_status.setText(String.format(" %s", signal.getProfit_status().toUpperCase()));
                        if (signal.getProfit_pips() != null) {
                            rl_pips.setVisibility(View.VISIBLE);
                            tv_signal_open_profit_pips_label.setText("Trade Profit:");
                            tv_signal_open_profit_pips.setTextColor(Color.parseColor("#5FAD56"));
                            tv_signal_open_profit_pips.setText(String.format(" %s", signal.getProfit_pips()));
                        }
                    } else if (signal.getProfit_status().trim().equals("Loss")) {
                        tv_profit_status.setTextColor(Color.parseColor("#D0312D"));
                        tv_profit_status.setText(String.format(" %s", signal.getProfit_status().toUpperCase()));
                        if (signal.getProfit_pips() != null) {
                            rl_pips.setVisibility(View.VISIBLE);
                            tv_signal_open_profit_pips_label.setText("Trade Loss:");
                            tv_signal_open_profit_pips.setTextColor(Color.parseColor("#D0312D"));
                            tv_signal_open_profit_pips.setText(String.format(" %s", signal.getProfit_pips()));
                        }
                    }
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
