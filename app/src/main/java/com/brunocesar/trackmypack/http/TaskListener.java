package com.brunocesar.trackmypack.http;
/**
 * Created by BrunoCesar on 30/04/2015.
 */
public interface TaskListener<Result> {
    public void onFinished(Result result);
}
