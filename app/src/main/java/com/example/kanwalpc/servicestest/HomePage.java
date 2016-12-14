package com.example.kanwalpc.servicestest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static com.example.kanwalpc.servicestest.FileService.TRANSACTION_DONE;

public class HomePage extends AppCompatActivity {
    private EditText display_download_file;
    private Button start_download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        display_download_file=(EditText)this.findViewById(R.id.display_eBook);
        start_download=(Button)this.findViewById(R.id.download_service);
        display_download_file.setVisibility(View.GONE);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(TRANSACTION_DONE);
        //register receiver

        HomePage.this.registerReceiver(new DownloadReceiver(),intentFilter);

    }

    public void start_service_function(View view) {
        Intent intent=new Intent(HomePage.this, FileService.class);
        intent.putExtra("url","http://archive.org/stream/draculabr00stokuoft/draculabr00stokuoft_djvu.txt");
        HomePage.this.startService(intent);
    }

    public class DownloadReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("FileService", "Service Received");
            showFileData();
        }
    }

    public void showFileData(){
        FileInputStream fileInputStream=null;
        try{
            fileInputStream=HomePage.this.openFileInput("myOwnFile");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);;
            StringBuilder stringBuilder=new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line).append("\n");
            }
            display_download_file.setVisibility(View.VISIBLE);
            display_download_file.setText(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
