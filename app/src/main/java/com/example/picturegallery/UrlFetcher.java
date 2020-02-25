package com.example.picturegallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlFetcher {
    private static final String TAG ="UrlFetcher";

    public List<PictureItem> fetchItems(){
        List<PictureItem> items =new ArrayList<>();
        try{
            for(int i=0;i<40;i++){
                String url = Uri.parse("https://bing.ioliu.cn/v1/rand")
                        .buildUpon()
                        .appendQueryParameter("type","json")
                        .build().toString();
                String jsonString = new String(getUrlString(url));
                JSONObject jsonBody = new JSONObject(jsonString);
                parseIFtems(items,jsonBody);
            }
        }catch (IOException ioe){
            Log.e(TAG,"@______@:ailed to fetch items");
        }catch (JSONException je){
            Log.e(TAG,"@______@:Failed to parse JSON ",je);
        }
        return items;
    }

    private void parseIFtems(List<PictureItem> items, JSONObject jsonBody)throws IOException,JSONException {
        JSONObject jsonData = jsonBody.getJSONObject("data");

        PictureItem item =new PictureItem();
        item.setDate(jsonData.getString("enddate"));
        item.setDescription(jsonData.getString("copyright"));

        if(!jsonData.has("url")){
            return ;
        }
        item.setUrl(jsonData.getString("url"));
        Log.i(TAG,"@______@:"+item.getUrl());
        items.add(item);
    }

    private byte[] getUrlString(String urlString) throws IOException{
        URL url = new URL(urlString);
        //openConnection返回默认URLConnection对象
        HttpURLConnection connection =(HttpURLConnection)url.openConnection();
        try{
            ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();//此时才真正开始连接网站
            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+" : with"+urlString);
            }
            int bytesRead =0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer))>0){
                outputStream.write(buffer,0,bytesRead);
            }
            outputStream.close();
            return outputStream.toByteArray();
        }finally {
            connection.disconnect();
        }
    }


}
