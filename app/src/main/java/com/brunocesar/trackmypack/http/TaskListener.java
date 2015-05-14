package com.brunocesar.trackmypack.http;


public interface TaskListener<Result> {
    public void onFinished(Result result);
}
