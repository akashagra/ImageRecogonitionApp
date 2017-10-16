package com.example.avma1997.androidvisionapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import java.io.InputStream;
import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;



public class MainActivity extends AppCompatActivity {
    public VisionServiceClient visionServiceClient=new VisionServiceRestClient("030216ac10ab424a85ac48b69fccfc98","https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap mBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.football);
        ImageView imageview=(ImageView)findViewById(R.id.imageView);
        TextView textView=(TextView)findViewById(R.id.textView);
        Button btnProcess=(Button)findViewById(R.id.button);
        imageview.setImageBitmap(mBitmap);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        final ByteArrayInputStream inputStream= new ByteArrayInputStream(outputStream.toByteArray());

        btnProcess.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {


                AsyncTask<InputStream, String, String> visionTask = new AsyncTask<InputStream, String, String>() {

                    @Override
                    protected String doInBackground(InputStream... params) {
                     try {
                        //    publishProgress("Recogonising....");
                            String[] features = {"Description"};
                            String[] details = {};
                            AnalysisResult analysisResult = visionServiceClient.analyzeImage(params[0], features, details);
                            String strResult = new Gson().toJson(analysisResult);
                            Log.i("myTag",strResult);
                            return strResult;
                        } catch (Exception e) {
                         Log.i("myTag","here");
                            return null;
                        }
                    }

                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    protected void onPostExecute(String s) {
                        Log.i("myTag",s);

                    AnalysisResult result=new Gson().fromJson(s,AnalysisResult.class);

                    TextView textView=(TextView)findViewById(R.id.textView);
                        StringBuilder stringBuilder=new StringBuilder();
                        for(Caption caption:result.description.captions)
                        {
                            stringBuilder.append(caption.text);

                        }
                        textView.setText(stringBuilder);


                    }


                };
                visionTask.execute(inputStream);

            }





    });
}
}
