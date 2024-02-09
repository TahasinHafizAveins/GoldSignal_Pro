package com.example.goldsignalpro.model;

import java.io.Serializable;
import java.util.List;

public class SignalsModel implements Serializable {
    String current_page;
    String last_page;
    String total;
    List<Data> data;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getSignal_list() {
        return data;
    }

    public void setSignal_list(List<Data> signal_list) {
        this.data = signal_list;
    }

    public static class Data {
        String id;
        String title;
        String signal_status;
        String profit_status;
        String created_at;
        String signal_description;
        String tp_1;
        String tp_2;
        String stop_loss;
        String profit_pips;
        String pips;

        public Data() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getProfit_status() {
            return profit_status;
        }

        public void setProfit_status(String profit_status) {
            this.profit_status = profit_status;
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

        public String getTp_1() {
            return tp_1;
        }

        public void setTp_1(String tp_1) {
            this.tp_1 = tp_1;
        }

        public String getTp_2() {
            return tp_2;
        }

        public void setTp_2(String tp_2) {
            this.tp_2 = tp_2;
        }

        public String getStop_loss() {
            return stop_loss;
        }

        public void setStop_loss(String stop_loss) {
            this.stop_loss = stop_loss;
        }

        public String getProfit_pips() {
            return profit_pips;
        }

        public void setProfit_pips(String profit_pips) {
            this.profit_pips = profit_pips;
        }

        public String getPips() {
            return pips;
        }

        public void setPips(String pips) {
            this.pips = pips;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", signal_status='" + signal_status + '\'' +
                    ", profit_status='" + profit_status + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", signal_description='" + signal_description + '\'' +
                    ", tp_1='" + tp_1 + '\'' +
                    ", tp_2='" + tp_2 + '\'' +
                    ", stop_loss='" + stop_loss + '\'' +
                    ", profit_pips='" + profit_pips + '\'' +
                    ", pips='" + pips + '\'' +
                    '}'+'\n';
        }
    }

    @Override
    public String toString() {
        return "SignalsModel{" +
                "current_page='" + current_page + '\'' +
                ", last_page='" + last_page + '\'' +
                ", total='" + total + '\'' +
                ", data=" + data +
                '}';
    }
}
