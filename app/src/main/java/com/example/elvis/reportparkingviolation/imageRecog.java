package com.example.elvis.reportparkingviolation;
/**
 * Elvis Gu, May 2018
 * The following code contain some from Baidu OCR Demo
 */
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class imageRecog extends AppCompatActivity {

    private static final int REQUEST_CODE_LICENSE_PLATE = 122;

    private boolean hasGotToken = false;

    private BmobGeoPoint reportLoc;
    private String reporterID;
    private double Lat;
    private double Lon;
    private String number;
    private String detailedLoc;

    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recog);
        Bmob.initialize(this, "f732e2fc958d8b5499561c171d8ca72d");
        //print out location which is from last activity's intent
        Intent intent = getIntent();
        TextView locationText =(TextView)findViewById(R.id.locationResultText);
        detailedLoc = intent.getStringExtra("location_data");
        locationText.setText(detailedLoc);
        reporterID = intent.getStringExtra("ID");
        Lat = intent.getDoubleExtra("locationLatitude",0.000000000000);
        Lon = intent.getDoubleExtra("locationLongitude",0.000000000000);
//        Toast.makeText(imageRecog.this, Lat+"", Toast.LENGTH_LONG).show();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.twoChoose);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        alertDialog = new AlertDialog.Builder(this);

        initAccessTokenWithAkSk();
    }

    //The following code is from Baidu OCR Demo
   private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "6sinItAc3rsC9bqVLybaKq9K", "kf3yL9pD1GIqt7WtPmA3y5VZu7dmGYBw");
    }

    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("sure", null)
                        .show();
            }
        });
    }


    //ensure the phone's permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Identify successful and deal with callbacks.
        if (requestCode == REQUEST_CODE_LICENSE_PLATE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recLicensePlate(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result){
                            try {
                                JSONObject jObject = new JSONObject(result);
                                number  = (String)jObject.getJSONObject("words_result").get("number");
                                TextView lblTitle=(TextView)findViewById(R.id.resultText);
                                lblTitle.setText(number);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
//End of the code
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();
    }



    //cases click the BottomNavigation burron
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //click button to take photo
                case R.id.navigation_photo:
                    Intent intent = new Intent(imageRecog.this, CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                            FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                            CameraActivity.CONTENT_TYPE_GENERAL);
                    startActivityForResult(intent, REQUEST_CODE_LICENSE_PLATE);
                    return true;

                //click button to send the report
                case R.id.navigation_report:
                    appUser testReporter1 = new appUser();
                    BmobGeoPoint point = new BmobGeoPoint(Lon,Lat);
                    testReporter1.setObjectId(reporterID);
                    reporterViolation testReport = new reporterViolation();
                    testReport.setLLlocation(point);
                    testReport.setDelatedLoc(detailedLoc);
                    testReport.setPlateNumber(number);
                    testReport.setReporter(testReporter1);
                    testReport.setDealt(false);
                    EditText ET=(EditText) findViewById(R.id.shortDescription);
                    testReport.setReportTitleandType(ET.getText().toString());
                    testReport.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(imageRecog.this,"success Send",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    return true;

                 //click th button to return back the reporter's map
                case R.id.back_reporttomap:
                    Intent intentRtM = new Intent(imageRecog.this, userMapActivity.class);
                    intentRtM.putExtra("useID",reporterID);
                    startActivity(intentRtM);
                    finish();
                    return true;
            }
            return false;
        }

    };
}

