package com.trendingproduct.config;

public interface OnTaskDoneListener {
    void onTaskDone(String responseData);

    void onError();
    void onError(String responseData);
}