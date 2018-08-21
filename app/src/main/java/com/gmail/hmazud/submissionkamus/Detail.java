package com.gmail.hmazud.submissionkamus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Detail extends AppCompatActivity {

    public static final String ITEM_WORD = "ITEM_WORD";
    public static final String ITEM_TRANSLATE = "ITEM_TRANSLATE";

    TextView textWord, textTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textWord = findViewById(R.id.textWord);
        textTranslate = findViewById(R.id.textTranslate);

        textWord.setText(getIntent().getStringExtra(ITEM_WORD));
        textTranslate.setText(getIntent().getStringExtra(ITEM_TRANSLATE));
    }
}
