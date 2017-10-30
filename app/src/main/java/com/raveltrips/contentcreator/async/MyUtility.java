package com.raveltrips.contentcreator.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Revised by kevin on 3/9/2016.
 */
public class MyUtility {

    //timeout set to 15 secs
    private static final int HTTP_REQUEST_TIMEOUT = 15000;
    static java.net.CookieManager cookieManager = new java.net.CookieManager();
    static final String COOKIES_HEADER = "Set-Cookie";

    // Download an image using HTTP Get Request
    public static Bitmap downloadImageusingHTTPGetRequest(String urlString) {
        Bitmap image=null, line;

        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setReadTimeout(HTTP_REQUEST_TIMEOUT);
            populateCookieHeaders(httpConnection);
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                image = getImagefromStream(stream);
            }
            httpConnection.disconnect();
        }  catch (UnknownHostException e1) {
            Log.d("MyDebugMsg", "UnknownHostexception in sendHttpGetRequest");
            e1.printStackTrace();
        } catch (Exception ex) {
            Log.d("MyDebugMsg", "Exception in sendHttpGetRequest");
            ex.printStackTrace();
        }
        return image;
    }

    // Get an image from the input stream
    private static Bitmap getImagefromStream(InputStream stream) {
        Bitmap bitmap = null;
        if(stream!=null) {
            bitmap = BitmapFactory.decodeStream(stream);
            try {
                stream.close();
            }catch (IOException e1) {
                Log.d("MyDebugMsg", "IOException in getImagefromStream()");
                e1.printStackTrace();
            }
        }
        return bitmap;
    }

    // Download JSON data (string) using HTTP Get Request
    public static String downloadJSONusingHTTPGetRequest(String urlString,String hdrKey,String hdrVal) {
        String jsonString=null;


        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC; en-US; rv:1.3.1)");
            httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
            if(hdrKey!=null && hdrVal!=null){
                httpConnection.setRequestProperty(hdrKey, hdrVal);
            }
            populateCookieHeaders(httpConnection);
            httpConnection.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setReadTimeout(HTTP_REQUEST_TIMEOUT);
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                jsonString = getStringfromStream(stream);
                if(hdrKey!=null){
                    //recieve the headers from server response
                    loadResponseCookies(httpConnection);
                }
            } else {
                Log.d("MyDebugMsg", "received response code:"+httpConnection.getResponseCode());
            }
            httpConnection.disconnect();
        }  catch (UnknownHostException e1) {
            Log.d("MyDebugMsg", "UnknownHostexception in downloadJSONusingHTTPGetRequest");
            e1.printStackTrace();
        } catch (Exception ex) {
            Log.d("MyDebugMsg", "Exception in downloadJSONusingHTTPGetRequest:"+ex.getMessage());
        }
        return jsonString;

    }

    public static void loadResponseCookies(HttpURLConnection conn) {
        if ( conn == null){
            return;
        }
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
               cookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
            }
        }
    }

    public static void populateCookieHeaders(HttpURLConnection conn) {
        if (cookieManager != null) {
            //getting cookies(if any) and manually adding them to the request header
            if (cookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                conn.setRequestProperty("Cookie",
                        TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));
            }
        }
    }

    // Download JSON data (string) using HTTP Get Request
    public static String downloadJSONusingHTTPGetRequest(String urlString) {
       return downloadJSONusingHTTPGetRequest(urlString,null,null);
    }

    // Get a string from an input stream
    private static String getStringfromStream(InputStream stream){
        String line, jsonString=null;
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            try {
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();
                jsonString = out.toString();
            } catch (IOException ex) {
                Log.d("MyDebugMsg", "IOException in downloadJSON()");
                ex.printStackTrace();
            }
        }
        return jsonString;
    }

    // Load JSON string from a local file (in the asset folder)
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null, line;
        try {
            InputStream stream = context.getAssets().open(fileName);
            if (stream != null) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder out = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();
                json = out.toString();
            }
        } catch (IOException ex) {
            Log.d("MyDebugMsg", "IOException in loadJSONFromAsset()");
            ex.printStackTrace();
        }
        return json;
    }


    // Send json data via HTTP POST Request
    public static void sendHttPostRequest(String urlString, JSONObject json){
        HttpURLConnection httpConnection = null;
        try {
            URL url = new URL(urlString);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setReadTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setDoOutput(true);
            httpConnection.setChunkedStreamingMode(0);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            populateCookieHeaders(httpConnection);
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write(json.toString());
            out.close();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                /*while ((line = reader.readLine()) != null) {
                    //Log.d("MyDebugMsg:PostRequest", line);  // for debugging purpose
                }*/
                reader.close();
                Log.d("MyDebugMsg:PostRequest", "POST request returns ok");
            } else Log.d("MyDebugMsg:PostRequest", "POST request returns error");
        } catch (Exception ex) {
            Log.d("MyDebugMsg", "Exception in sendHttpPostRequest");
            ex.printStackTrace();
        }

        if (httpConnection != null) httpConnection.disconnect();
    }

    public static String deleteHttpRequest(String urlString){
        HttpURLConnection httpConnection = null;
        try {
            URL url = new URL(urlString);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setReadTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("DELETE");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpConnection.setChunkedStreamingMode(0);
            populateCookieHeaders(httpConnection);

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                String jsonString = getStringfromStream(stream);
                Log.d("MyDebugMsg:DelRequest", "DELETE request returns ok");
                if (httpConnection != null) httpConnection.disconnect();
                return jsonString;
            } else {
                Log.d("MyDebugMsg:DelRequest", "DELETE response code:"+httpConnection.getResponseCode());
                // Log.d("MyDebugMsg:PostRequest", "POST request returns error");
                if (httpConnection != null) httpConnection.disconnect();
                return null;
            }
        } catch (Exception ex) {
            Log.d("MyDebugMsg", "Exception in sendHttpPostRequest:"+ex.getMessage());
            if (httpConnection != null) httpConnection.disconnect();
            return null;
        }
        }


    // Send json data via HTTP POST Request
    public static String sendHttPostRequest(String urlString, String jsonData){
        HttpURLConnection httpConnection = null;
        try {
            URL url = new URL(urlString);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setReadTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpConnection.setChunkedStreamingMode(0);
            populateCookieHeaders(httpConnection);

            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write(jsonData);
            out.close();

           // Log.d("MyDebugMsg:PostRequest", "post url:"+urlString);

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                String jsonString = getStringfromStream(stream);
                Log.d("MyDebugMsg:PostRequest", "POST request returns ok");
                if (httpConnection != null) httpConnection.disconnect();
                return jsonString;
            } else {
                Log.d("MyDebugMsg:PostRequest", "POST response code:"+httpConnection.getResponseCode());
               // Log.d("MyDebugMsg:PostRequest", "POST request returns error");
                if (httpConnection != null) httpConnection.disconnect();
                return null;
            }
        } catch (Exception ex) {
            Log.d("MyDebugMsg", "Exception in sendHttpPostRequest:"+ex.getMessage());
            if (httpConnection != null) httpConnection.disconnect();
            return null;
        }
    }

    // Send json data via HTTP POST Request
    public static String sendHttPutRequest(String urlString, String jsonData){
        HttpURLConnection httpConnection = null;
        try {
            URL url = new URL(urlString);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setReadTimeout(HTTP_REQUEST_TIMEOUT);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpConnection.setChunkedStreamingMode(0);
            populateCookieHeaders(httpConnection);

            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write(jsonData);
            out.close();

            // Log.d("MyDebugMsg:PostRequest", "post url:"+urlString);

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                String jsonString = getStringfromStream(stream);
                Log.d("MyDebugMsg:PutRequest", "PUT request returns ok");
                if (httpConnection != null) httpConnection.disconnect();
                return jsonString;
            } else {
                Log.d("MyDebugMsg:PutRequest", "PUT response code:"+httpConnection.getResponseCode());
                // Log.d("MyDebugMsg:PostRequest", "POST request returns error");
                if (httpConnection != null) httpConnection.disconnect();
                return null;
            }
        } catch (Exception ex) {
            Log.d("MyDebugMsg", "Exception in sendHttpPutRequest:"+ex.getMessage());
            if (httpConnection != null) httpConnection.disconnect();
            return null;
        }
    }

    public String readUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
