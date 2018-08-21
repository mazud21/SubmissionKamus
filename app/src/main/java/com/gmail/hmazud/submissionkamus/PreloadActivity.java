package com.gmail.hmazud.submissionkamus;

import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.hmazud.submissionkamus.db.KmsHelper;
import com.gmail.hmazud.submissionkamus.model.KmsModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PreloadActivity extends AppCompatActivity {

    TextView textView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_load);

        textView = findViewById(R.id.tv_load);
        progressBar = findViewById(R.id.progressBar);
        //new LoadDataAsync(this).execute();
        new LoadData().execute();
    }

    private void loadDummyProcess() {
        final int countDown = 1000;
        progressBar.setMax(countDown);
        CountDownTimer countDownTimer = new CountDownTimer(countDown, (countDown / 100)) {
            @Override
            public void onTick(long l) {
                progressBar.setProgress((int) (countDown - l));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void> {
        KmsHelper kmsHelper;
        AppPreference appPreference;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kmsHelper = new KmsHelper(PreloadActivity.this);
            appPreference = new AppPreference(PreloadActivity.this);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = appPreference.getFirstRun();
            if (firstRun) {
                ArrayList<KmsModel> kmsEnglish = preLoadRaw(R.raw.english_indonesia);
                ArrayList<KmsModel> kmsIndonesia = preLoadRaw(R.raw.indonesia_english);

                publishProgress((int) progress);

                try {
                    kmsHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (kmsEnglish.size() + kmsIndonesia.size());

                kmsHelper.insertTransaction(kmsEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                kmsHelper.insertTransaction(kmsIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                kmsHelper.close();
                appPreference.setFirstRun(false);

                publishProgress((int) maxprogress);
            } else {
                textView.setVisibility(View.INVISIBLE);
                try {
                    synchronized (this) {
                        this.wait(1000);
                        publishProgress(50);

                        this.wait(300);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(PreloadActivity.this, MainActivity.class);
            startActivity(i);

            finish();
        }
    }

    public ArrayList<KmsModel> preLoadRaw(int data) {
        ArrayList<KmsModel> kmsModels = new ArrayList<>();
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            String line = null;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                KmsModel kmsModel;
                kmsModel = new KmsModel(splitstr[0], splitstr[1]);
                kmsModels.add(kmsModel);
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kmsModels;
    }
}
