package com.example.widgetnote;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
/* Настройка виджета */
public class Config extends Activity {

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    public final static String WIDGET_PREF = "WIDGET_PREF";
    public final static String WIDGET_TEXT = "WIDGET_TEXT_";
    public final static String WIDGET_COLOR = "WIDGET_COLOR_";

    @Override
    public void onCreate(Bundle params) {
        super.onCreate(params);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_CANCELED, resultValue);
        setContentView(R.layout.config);
    }

    public void onClick(View view) { /* по клике на кнопку "Сохранить" */
        EditText editText = findViewById(R.id.editText); /* получаем текст */
        RadioGroup radio = findViewById(R.id.radio);
        int color = 0; /* получаем цвет */
        switch (radio.getCheckedRadioButtonId()) {
            case R.id.radioGrey:
                color = R.color.grey;
                break;
            case R.id.radioSepia:
                color = R.color.sepia;
                break;
            case R.id.radioWhite:
                color = R.color.white;
                break;
        }

        SharedPreferences pref = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(WIDGET_TEXT + widgetID, editText.getText().toString());
        edit.putInt(WIDGET_COLOR + widgetID, getResources().getColor(color));
        edit.apply();

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        Widget.updateWidget(this, manager, pref, widgetID);

        setResult(RESULT_OK, resultValue);
        finish();

    }
}
