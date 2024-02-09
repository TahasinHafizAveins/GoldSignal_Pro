package com.example.goldsignalpro.utils;

import com.example.goldsignalpro.model.SignalsModel;

import java.util.List;

public class SaveSignalData {
    private static final SaveSignalData instance = new SaveSignalData();
    private SignalsModel.Data data;
    private SignalsModel signalsModel;

    public static SaveSignalData getInstance() {
        return instance;
    }

    public SignalsModel.Data getData() {
        return data;
    }

    public void setData(SignalsModel.Data data) {
        this.data = data;
    }

    public SignalsModel getSignalsModel() {
        return signalsModel;
    }

    public void setSignalsModel(SignalsModel signalsModel) {
        this.signalsModel = signalsModel;
    }
}
