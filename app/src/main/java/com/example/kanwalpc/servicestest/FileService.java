package com.example.kanwalpc.servicestest;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FileService extends IntentService{
    public static final String TRANSACTION_DONE="com.example.kanwalpc.TRANSACTION_DONE";

    public FileService(){
        super(FileService.class.getName());
    }

    public FileService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("FileService", "Service Start");
        String passedURL=intent.getExtras().getString("url");
        downloadFile(passedURL);
        Log.e("FileService", "Service Stopped");

        Intent i=new Intent(TRANSACTION_DONE);
        FileService.this.sendBroadcast(i);
    }

    protected void downloadFile(String passedURL){
        FileOutputStream fileOutputStream=null;
        String file="myOwnFile";
        try{
            fileOutputStream=openFileOutput(file, Context.MODE_PRIVATE);

            URL url=new URL(passedURL);
            HttpsURLConnection httpsURLConnection=(HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();

            InputStream inputStream=httpsURLConnection.getInputStream();
            byte[] buffer=new byte[1024];
            int buffer_length=-1;


            while((buffer_length=inputStream.read(buffer))>-1){
                fileOutputStream.write(buffer, 0, buffer_length);
                fileOutputStream.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
