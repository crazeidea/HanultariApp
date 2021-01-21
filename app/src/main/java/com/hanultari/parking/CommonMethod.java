package com.hanultari.parking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hanultari.parking.DTO.MemberDTO;
import com.naver.maps.geometry.LatLng;

import org.jetbrains.annotations.NotNull;

public class CommonMethod {

  public static String  ipConfig = "http://192.168.0.113:8080";

  public static MemberDTO loginDTO = null;


  // 네트워크에 연결되어 있는가
  public static boolean isNetworkConnected(Context context) {
    ConnectivityManager cm = (ConnectivityManager)
            context.getSystemService( Context.CONNECTIVITY_SERVICE );
    NetworkInfo info = cm.getActiveNetworkInfo();
    if(info != null){
      if(info.getType() == ConnectivityManager.TYPE_WIFI){
        Log.d("isconnected : ", "WIFI 로 설정됨");
      }else if(info.getType() == ConnectivityManager.TYPE_MOBILE){
        Log.d("isconnected : ", "일반망으로 설정됨");
      }
      Log.d("isconnected : ", "OK => " + info.isConnected());
      return true;
    }else {
      Log.d("isconnected : ", "False => 데이터 통신 불가!!!" );
      return false;
    }

  }

  /* 두 지점의 거리를 m 단위로 출력 */
  public static int getDistance(@NotNull LatLng current, @NotNull LatLng target){
    double currentLat = current.latitude;
    double currentLng = current.longitude;
    double targetLat = target.latitude;
    double targetLng = target.longitude;
    double theta = currentLng - targetLng;

    double dist = Math.sin(deg2rad(currentLat))
            * Math.sin(deg2rad(targetLat))
            + Math.cos(deg2rad(currentLat))
            * Math.cos(deg2rad(targetLat))
            * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515 / 0.62137;
    return (int) Math.floor(dist * 1000);
  }

  public static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  public static double rad2deg(double rad) {
    return (rad * 180.0 / Math.PI);
  }




}
