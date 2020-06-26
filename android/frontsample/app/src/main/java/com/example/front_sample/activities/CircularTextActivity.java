package com.example.front_sample.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.widget.EditText;

import com.example.front_sample.R;
import com.example.front_sample.utils.textPaintView;

public class CircularTextActivity extends Activity {
    private com.example.front_sample.utils.textPaintView textPaintView;
    private EditText textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulartext);

        textInput = (EditText) findViewById(R.id.textBox);
        textPaintView = (textPaintView) findViewById(R.id.textPaintView);
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.heightPixels = metrics.widthPixels;
        textPaintView.init(metrics);
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textPaintView.foo(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
