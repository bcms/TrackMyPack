package com.brunocesar.trackmypack.http;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by BrunoCesar on 28/04/2015.
 */



public abstract class BaseHttp<Result> {

    private GetAllAsync getAllAsync;
    private TaskListener<List<Result>> taskListener;

    protected abstract Result jsonToResult(JSONObject jsonObject) throws JSONException;

    protected abstract String getUrl();

    public List<Result> getAll() throws ExecutionException, InterruptedException {
        this.getAllAsync = new GetAllAsync();
        return jsonArrayToResultList(getAllAsync.execute().get());
    }

    public void getAllAsync(TaskListener<List<Result>> taskListener){
        this.getAllAsync = new GetAllAsync();
        this.taskListener = taskListener;
        getAllAsync.execute();
    }

    private List<Result> jsonArrayToResultList(JSONArray jsonArray){

        List<Result> results = new ArrayList<Result>();

        for (int i = 0; i < jsonArray.length(); i++)
        {
            try {
                results.add(jsonToResult(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    private class GetAllAsync extends AsyncTask<Void, Void, JSONArray> {

        JSONArray jsonArray;

        public GetAllAsync(){
            this.jsonArray = new JSONArray();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            StringBuffer jsonString = new StringBuffer("");

            try {

                URL url = new URL(getUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    jsonString.append(line);
                }

                jsonArray = new JSONArray(jsonString.toString());

            } catch (Exception e) {
                return jsonArray;
            }

            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            List<Result> results = jsonArrayToResultList(jsonArray);

            taskListener.onFinished(results);

            super.onPostExecute(jsonArray);
        }
    }
}
