package com.cyanogenmod.stats;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {
    private static final String PREF_NAME = "CMStats";

    private CheckBox mCheckbox;
    private Button mSaveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCheckbox = (CheckBox) findViewById(R.id.main_optin);
        mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("optin", isChecked);
                editor.putBoolean("firstboot", false);
                editor.commit();
                startReportingService();
            }
        });

        mSaveButton = (Button) findViewById(R.id.main_btn_save);
        mSaveButton.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                finish();
            }

        });

        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        boolean optin = settings.getBoolean("optin", false);

        mCheckbox.setChecked(optin);
    }

    private void startReportingService(){
        ComponentName cmp = new ComponentName(getPackageName(), ReportingService.class.getName());
        startService(new Intent().setComponent(cmp));
    }
}