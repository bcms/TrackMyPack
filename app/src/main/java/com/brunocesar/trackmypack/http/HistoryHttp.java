package com.brunocesar.trackmypack.http;

import com.brunocesar.trackmypack.models.History;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BrunoCesar on 01/05/2015.
 */
public class HistoryHttp extends BaseHttp<History> {

    private final String trackUrl = "http://developers.agenciaideias.com.br/correios/rastreamento/json/";
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
        return trackUrl + trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }
}
