package com.example.gps_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    private Context context = MainActivity.this;
    private final static String TAG = "Debugging";

    private String isGPSEnabled = null;
    private int isGPSPermitted = -1;
    final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean shouldShowRequestPermissionRationale;
//    private Snackbar snackbar;
    private View mainView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.textView);
        mainView = findViewById(R.id.activity_main);

//        // 권한 요청
//        // ~을 허용하시겠습니까?
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        isGPSEnabled = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        Log.d("Debug", "test, "+isGPSEnabled);

        setGPSEnabled();
        getGPSPermission();

//        snackbar = Snackbar.make(v, "Test", Snackbar.LENGTH_INDEFINITE);
//        snackbar.setAction();

//        clickHandler(mainView);
        alertCheckGPS();
    }

    public void clickHandler(View v) {
        Snackbar snackbar = Snackbar.make(v, "Snackbar Test", Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("OK", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }


/*
    private void getGPSPermission() {
        Log.d("Debug", ""+shouldShowRequestPermissionRationale);
        Log.d("Debug", ""+isGPSPermitted);
        isGPSPermitted = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        String str = null;
        switch (isGPSPermitted) {
            case PackageManager.PERMISSION_DENIED:
                str = "PERMISSION_DENIED";
                break;
            case PackageManager.PERMISSION_GRANTED:
                str = "PERMISSION_GRANTED";
                break;
        }
//        Toast.makeText(context, "isGPSPermitted : "+str, Toast.LENGTH_SHORT).show();
        Log.d("Debug", str+" "+isGPSPermitted);
        if (isGPSPermitted != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "권한 승인이 필요합니다.", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 다시 묻지 않음 체크 안함
                Log.d("Debug", "다시 묻지 않음 체크 안함");

                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                dlg.setMessage("위치권한 설정 필요 이유");
                dlg.setTitle("Permission");
                dlg.setIcon(R.drawable.ic_launcher_background);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            } else {
                // 최초 실행
                // 다시 묻지 않음 체크
                Log.d("Debug", "다시 묻지 않음 체크");
                Toast.makeText(context, "권한 승인 필요", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);

                dlg.setMessage("위치권한 설정 필요 이유");
                dlg.setTitle("다시 묻지 않음");
                dlg.setIcon(R.drawable.ic_launcher_background);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            }
        }
        Log.d("Debug", ""+isGPSPermitted);
    }

 */

    // GPS 권한 확인
    private void getGPSPermission() {
        isGPSPermitted = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (isGPSPermitted != PackageManager.PERMISSION_GRANTED) {
            // 권한 설정 안되어 있을 경우
            // 권한 설정
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // 권한 요청 다이얼로그 결과에 대한 액션
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Accept
//                    Toast.makeText(context, "권한 승인", Toast.LENGTH_SHORT).show();
                    Log.d("Debug 3", "권한 승인");
                } else {
                    // Deny
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // 다시 묻지 않음 체크 안함
                        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                        dlg.setTitle("권한 설정 필요");
                        dlg.setMessage("사용불가");
                        dlg.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dlg.show();
                    } else {
                        // 다시 묻지 않음 체크
                        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                        dlg.setTitle("권한 설정 필요");
                        dlg.setMessage("사용불가\n설정창 이동?");
                        dlg.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dlg.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dlg.show();
                    }
                }
        }
    }

    // GPS 활성 상태 확인
    private void setGPSEnabled() {
        isGPSEnabled = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        Log.d("Debug", "enabled : "+isGPSEnabled);

        if (!(isGPSEnabled.contains("gps")&&isGPSEnabled.contains("network"))) {
            Log.d(TAG, "GPS is not enabled");
            alertCheckGPS();
        } else {
            Log.d(TAG, "GPS is enabled");
        }
    }

    // GPS 사용 설정 다이얼로그
    private void alertCheckGPS() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("GPS 사용 설정")
                .setMessage("GPS 기능이 꺼져있습니다.\n" +
                        "GPS 기능을 켜시겠습니까?")
                .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setGPSConfig();
                    }
                })
                .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }

    // GPS 설정
    private void setGPSConfig() {
        Intent GPSOptIntent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(GPSOptIntent);
    }
}