package com.raveltrips.contentcreator.async;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 29-06-2017.
 */

public class DeleteTripAsyncTask extends AsyncTask<String, Void, List<String>> {

    private AsyncComplete asyncComplete;
    private String bodyContent;

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    public DeleteTripAsyncTask(AsyncComplete async){
        this.asyncComplete = async;
    }

    public DeleteTripAsyncTask(){}

    public AsyncComplete getAsyncComplete() {
        return asyncComplete;
    }

    public void setAsyncComplete(AsyncComplete asyncComplete) {
        this.asyncComplete = asyncComplete;
    }


    @Override
    protected List<String> doInBackground(String... strings) {
        List<String> jsons = new ArrayList<>();
        for (String url : strings){
            try {
                String resp = MyUtility.deleteHttpRequest(url);
                if(resp!=null)jsons.add(resp);
            } catch (Exception e) {
                Log.d("DeleteTask","Exception:"+e);
            }
        }
        return jsons;
    }

    @Override
    protected void onPostExecute(List<String> jsons){
        asyncComplete.OnJsonAsyncCompleted(jsons);
    }
}
