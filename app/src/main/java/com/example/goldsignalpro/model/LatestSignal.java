package com.example.goldsignalpro.model;

public class LatestSignal {
    String title;
    String signal_status;
    String profit_status;
    String created_at;
    String signal_description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getSignal_status() {
        return signal_status;
    }

    public void setSignal_status(String signal_status) {
        this.signal_status = signal_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSignal_description() {
        return signal_description;
    }

    public void setSignal_description(String signal_description) {
        this.signal_description = signal_description;
    }

    public String getProfit_status() {
        return profit_status;
    }

    public void setProfit_status(String profit_status) {
        this.profit_status = profit_status;
    }

    @Override
    public String toString() {
        return "LatestSignal{" +
                "title='" + title + '\'' +
                ", signal_status='" + signal_status + '\'' +
                ", profit_status='" + profit_status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", signal_description='" + signal_description + '\'' +
                '}';
    }
}
