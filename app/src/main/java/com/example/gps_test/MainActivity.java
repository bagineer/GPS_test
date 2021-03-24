package com.example.gps_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private final Context context = MainActivity.this;
    private final Activity activity = this;
    private final static String TAG = "Debugging";

//    private String isGPSEnabled = null;
//    private int isGPSPermitted = -1;
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final static int ACT_PERMISSION = 0;
    private final static int ACT_LOCATION = 1;
    private boolean isRepeated;
    private final MLocationListner mLocationListner = new MLocationListner();
    private LocationManager locationManager;

    private TextView textView;

    private double curLat, curLon;

    private int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);

        getGPSPermission();

    }

    /* GPS 권한 */
    // 0. Check GPS Permission
    private boolean checkGPSPermission() {
        int permission = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    // 1. GPS 권한 확인
    private void getGPSPermission() {
//        isGPSPermitted = ActivityCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_FINE_LOCATION);

        if (!checkGPSPermission()) {
            // 권한 설정 안되어 있을 경우
            // 권한 설정
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            setGPSEnabled();
        }
    }

    // 2. 권한 요청 다이얼로그 결과에 대한 액션
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Accept
                Log.d("Debug", "Permission accepted");
                setGPSEnabled();
            } else {
                // Deny
                Log.d(TAG, "Permission denied");
                isRepeated = ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (isRepeated) {
                    // 다시 묻지 않음 체크 안함
                    alertPermissionGPS(isRepeated,
                            "GPS 사용 권한이 설정되어있지 않아\n" +
                                    "앱 사용이 불가능합니다.\n" +
                                    "GPS 사용 권한을 설정하시겠습니까?");
                } else {
                    // 다시 묻지 않음 체크
                    alertPermissionGPS(isRepeated,
                            "GPS 사용 권한이 설정되어있지 않아\n" +
                                    "앱 사용이 불가능합니다.\n" +
                                    "GPS 사용 권한을 설정하시겠습니까?");
                }
            }
        }
    }

    // 3. GPS 권한 거부 이벤트
    private void alertPermissionGPS(boolean repeated, String msg) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("GPS 사용 권한")
                .setMessage(msg);
        if (repeated) {
            dlg.setPositiveButton("ACCEPT",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        }
                    })
                    .setNegativeButton("DENY",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            alertFinish();
                        }
                    }).show();
        } else {
            dlg.setPositiveButton("ACCEPT",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setGPSPermission();
                        }
                    })
                    .setNegativeButton("DENY",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    alertFinish();
                                }
                            })
                    .show();
        }
    }

    // #. 앱 종료 다이얼로그
    private void alertFinish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("앱 사용 불가")
                .setMessage("앱을 종료합니다.")
                .setPositiveButton("FINISH",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                finish();
                            }
                        }).show();
    }

    // 4. GPS 권한 설정 화면
    private void setGPSPermission() {
        Intent GPSPmsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        GPSPmsIntent.setData(uri);
        startActivityForResult(GPSPmsIntent, ACT_PERMISSION);
    }

    // 5. GPS 권한 설정 종료 이벤트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACT_PERMISSION:
                Log.d(TAG, "Setting activity finished");

//                isGPSPermitted = ActivityCompat.checkSelfPermission(context,
//                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (checkGPSPermission()) {
                    setGPSEnabled();
                    break;
                } else {
                    alertPermissionGPS(isRepeated,
                            "GPS 사용 권한이 설정되어있지 않아\n" +
                                    "앱 사용이 불가능합니다.\n" +
                                    "GPS 사용 권한을 설정하시겠습니까?");
                }
            case ACT_LOCATION:
                if (isGPSEnabled()) {
                    getLocation();
                } else {
                    setGPSEnabled();
                }
        }
    }

    /* GPS 활성화 */
    // 0. GPS 활성 상태 확인
    private boolean isGPSEnabled() {
        String activated = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return activated.contains("gps") && activated.contains("network");
    }

    // 1. GPS 활성화
    private void setGPSEnabled() {
//        isGPSEnabled = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//        Log.d("Debug", "enabled : " + isGPSEnabled);

        if (isGPSEnabled()) {
            Log.d(TAG, "GPS is enabled");
            getLocation();
        } else {
            Log.d(TAG, "GPS is not enabled");
            alertCheckGPS();
        }
    }

    // 2. GPS 사용 설정 다이얼로그
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
                        alertFinish();
                    }
                })
                .show();
    }

    // 3. GPS 설정
    private void setGPSConfig() {
        Intent GPSOptIntent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(GPSOptIntent, ACT_LOCATION);
    }

    /* LocationProvider */

    // Get Best Provider
    private String getBestProvider() {
        locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.NO_REQUIREMENT);
        crit.setPowerRequirement(Criteria.NO_REQUIREMENT);
        crit.setAltitudeRequired(false);
        crit.setCostAllowed(false);
        return locationManager.getBestProvider(crit, true);
    }

    // Location Listener
    public class MLocationListner implements LocationListener {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("TAG", getBestProvider());
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Log.d("TAG", provider+" enabled");
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Log.d("TAG", provider+" disabled");
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            cnt++;
            curLat = location.getLatitude();
            curLon = location.getLongitude();
            Log.d(TAG, "**"+cnt+"**"+curLat + "," + curLon);
            textView.setText(curLat + "," + curLon);
        }
    }

    private void getLocation() {
//        isGPSPermitted = checkGPSPermission();
//        if (isGPSPermitted != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        if (checkGPSPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, mLocationListner);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    300, 0, mLocationListner);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                    1000, 0, mLocationListner);
        }
    }
}