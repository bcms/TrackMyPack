package com.brunocesar.trackmypack.http;

import com.brunocesar.trackmypack.models.History;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoryHttp extends BaseHttp<History> {

    private String trackCode;

    @Override
    protected History jsonToResult(JSONObject jsonObject) throws JSONException {

        History history = new History();

        history.setAction(jsonObject.getString("acao"));
        history.setDate(jsonObject.getString("data"));
        history.setDetails(jsonObject.getString("detalhes"));
        history.setPlace(jsonObject.getString("local"));

        return history;
    }

    @Override
    protected String getUrl() {
        return "http://developers.agenciaideias.com.br/correios/rastreamento/json/" + trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }
}
