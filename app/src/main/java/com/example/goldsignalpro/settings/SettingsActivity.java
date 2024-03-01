package com.example.goldsignalpro.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goldsignalpro.BuildConfig;
import com.example.goldsignalpro.R;
import com.example.goldsignalpro.about_us.AboutUSActivity;
import com.example.goldsignalpro.utils.SaveAppInfo;

public class SettingsActivity extends AppCompatActivity {

    TextView tv_about_us, tv_privacy_policy, tv_share_app, tv_rate_us;
    private boolean rate_us_showing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tv_about_us = findViewById(R.id.tv_about_us);
        tv_privacy_policy = findViewById(R.id.tv_privacy_policy);
        tv_share_app = findViewById(R.id.tv_share_app);
        tv_rate_us = findViewById(R.id.tv_rate_us);

        tv_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAboutUs();
            }
        });

        tv_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPrivacyPolicy();
            }
        });

        tv_share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_app();
            }
        });

        tv_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate_us();
            }
        });


    }

    private void share_app() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
        if (SaveAppInfo.getInstance().getApp_update_link() == null || SaveAppInfo.getInstance().getApp_update_link().equals("")) {
            intent.putExtra(Intent.EXTRA_TEXT, "https://goldsignalpro.com/");
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, SaveAppInfo.getInstance().getApp_update_link());
        }
        intent.setType("text/plain");
        startActivity(intent);

    }

    private void openPrivacyPolicy() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goldsignalpro.com/privacy-policy")));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void loadAboutUs() {
        startActivity(new Intent(SettingsActivity.this, AboutUSActivity.class));
    }

    private void rate_us() {
        if (!rate_us_showing) {
            rate_us_showing = true;


            AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);

            alert.setTitle("Rate Us!");
            alert.setMessage(getResources().getString(R.string.app_rate_us_title));
            alert.setPositiveButton(R.string.app_rate_us,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rate_us_showing = false;

                            if (SaveAppInfo.getInstance().getApp_update_link() == null || SaveAppInfo.getInstance().getApp_update_link().equals("")) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goldsignalpro.com/")));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            } else {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SaveAppInfo.getInstance().getApp_update_link())));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }

                        }
                    });
            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            rate_us_showing = false;
                            dialog.cancel();
                        }
                    });


            AlertDialog alertDialog = alert.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setFocusable(true);
                    positive.setFocusableInTouchMode(true);
                    positive.requestFocus();
                }
            });

            alertDialog.show();
        }
    }
}