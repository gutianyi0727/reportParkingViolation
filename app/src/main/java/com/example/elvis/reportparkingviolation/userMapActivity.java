package com.example.elvis.reportparkingviolation;

/**
 * Elvis Gu, May 2018
 * The following code contain some from Baidu Map Demo
 */
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;
/**
 * users main page with a Baidu map view
 * */
public class userMapActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private String locationData;
    private double locationLatitude;
    private double locationLongitude;
    private TextView mTextMessage;
    private String theReporterID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_map);

//        mTextMessage = (TextView) findViewById(R.id.message);

        mLocationClient= new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_user_map);

        Intent getIntent = getIntent();
        theReporterID = getIntent.getStringExtra("useID");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set up map
        mapView = (MapView) findViewById(R.id.reportMap);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
//        positionText = (TextView) findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(userMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(userMapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(userMapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(userMapActivity.this,permissions,1);
        }else{
            requestLocation();
        }

    }

    //set two cases button value.
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    return true;
                case R.id.navigation_dashboard:
                    Intent intentSendLocation = new Intent(userMapActivity.this,imageRecog.class);
//                    Bundle allLocation=new Bundle();
//                    allLocation.putDouble("locationLatitude",locationLatitude);
//                    allLocation.putDouble("locationLongitude",locationLongitude);
//                    allLocation.putString("location_data",locationData);
//                    intentSendLocation.putExtra("allKindslocationData",allLocation);
                    intentSendLocation.putExtra("locationLatitude",locationLatitude);
                    intentSendLocation.putExtra("locationLongitude",locationLongitude);
                    intentSendLocation.putExtra("location_data",locationData);
                    intentSendLocation.putExtra("ID",theReporterID);
//                    Toast.makeText(userMapActivity.this,theReporterID,Toast.LENGTH_SHORT).show();
                    startActivity(intentSendLocation);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    Intent intentHistory = new Intent(userMapActivity.this,HistoryTable.class);
                    intentHistory.putExtra("userObjectID",theReporterID);
                    startActivity(intentHistory);
                    return true;
            }
            return false;
        }

    };

    //move the map to location
    private void navigateTo(BDLocation location){
        //if (isFirstLocate){
        LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,16);
        baiduMap.animateMapStatus(update);
        // update = MapStatusUpdateFactory.zoomTo(16f);
        //baiduMap.animateMapStatus(update);
        /*判断baiduMap是已经移动到指定位置*/
            /*if (baiduMap.getLocationData()!=null)
                if (baiduMap.getLocationData().latitude==location.getLatitude()
                        &&baiduMap.getLocationData().longitude==location.getLongitude()) {
                    isFirstLocate = false;
                }*/
        //isFirstLocate = false;
        //Toast.makeText(MainActivity.this,"zhuanyi",Toast.LENGTH_SHORT).show();测试该方法是否用到
        // }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
//        Toast.makeText(this,locationLatitude+"   "+locationLongitude,Toast.LENGTH_LONG).show();


    }
    private  void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        //option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
    }


    @Override
    protected  void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected  void onPause(){
        super.onPause();
        mapView.onPause();
    }

    protected  void  onDestory(){
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }



    public void onRequestPermissionResult(int requestcode,String[] permissions,int[] grantResults){
        switch (requestcode){
            case 1:
                if (grantResults.length > 0 ){
                    for (int result : grantResults){
                        if (result !=PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this,"必须同意所有权限",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"未知error",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public  void onReceiveLocation(BDLocation location) {
            StringBuilder currentPosition = new StringBuilder();
//            currentPosition.append("纬度： ").append(location.getLatitude()).append("\n");
//            currentPosition.append("经度： ").append(location.getLongitude()).append("\n");
            currentPosition.append("").append(location.getCountry()).append(location.getProvince()).append(location.getCity()).append(location.getDistrict()).append(location.getStreet());
            locationLongitude = location.getLongitude();
            locationLatitude = location.getLatitude();
            /*if (location.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else {
                currentPosition.append("network");
            }*/
            // positionText.setText(currentPosition);
            locationData = currentPosition.toString();
//            Toast.makeText(userMapActivity.this, locationData,Toast.LENGTH_LONG).show();
            //if (location.getLocType() ==BDLocation.TypeGpsLocation ||location.getLocType() ==BDLocation.TypeNetWorkLocation){
            navigateTo(location);
            //}
        }
    }



}
