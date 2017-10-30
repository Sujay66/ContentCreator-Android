package com.raveltrips.contentcreator.async;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akash Anjanappa on 24-03-2017.
 */

public class DownloadJsonAsyncTask  extends AsyncTask<String, Void, List<String>> {

    private AsyncComplete asyncComplete;
    private boolean isCustomHeaderSet = false;
    private String hdrKey;
    private String hdrVal;


    public DownloadJsonAsyncTask(AsyncComplete async){
        this.asyncComplete = async;
    }

    public DownloadJsonAsyncTask(){}

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
                String resp = null;
                if(isCustomHeaderSet){
                    resp = MyUtility.downloadJSONusingHTTPGetRequest(url,hdrKey,hdrVal);
                } else {
                    resp = MyUtility.downloadJSONusingHTTPGetRequest(url);
                }

               if(resp!=null) jsons.add(resp);
            } catch (Exception e) {
                Log.d("DownloadJsonAsyncTask","Exception downloading json:"+e);
            }
        }
        return jsons;
    }

    @Override
    protected void onPostExecute(List<String> jsons){
       // Log.d("DownloadJsonAsyncTask","Recieved jsons from server of size:"+jsons.size());
        asyncComplete.OnJsonAsyncCompleted(jsons);
    }

    public boolean isCustomHeaderSet() {
        return isCustomHeaderSet;
    }

    public void setCustomHeaderSet(boolean customHeaderSet) {
        isCustomHeaderSet = customHeaderSet;
    }

    public String getHdrKey() {
        return hdrKey;
    }

    public void setHdrKey(String hdrKey) {
        this.hdrKey = hdrKey;
    }

    public String getHdrVal() {
        return hdrVal;
    }

    public void setHdrVal(String hdrVal) {
        this.hdrVal = hdrVal;
    }
}
