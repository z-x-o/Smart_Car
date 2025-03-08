package com.example.mybluetoothdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class BlueToothController {

    private BluetoothAdapter mBluetoothAdapter;

    public BlueToothController(){
        //初始化蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }




    /**
     * 检查设备是否支持蓝牙
     */
    public boolean isBluetoothSupport(){
        if(mBluetoothAdapter == null){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 检查该设备蓝牙是否开启
     */
    public boolean isBluetoothEnabled(){
        if(mBluetoothAdapter.isEnabled()){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 打开蓝牙
     */
    @SuppressLint("MissingPermission")
    public void turnOnBlueTooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 打开蓝牙可见性
     */
    @SuppressLint("MissingPermission")
    public void enableVisibily(Context context){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        context.startActivity(intent);
    }


    /**
     * 停止查找设备
     */
    @SuppressLint("MissingPermission")
    public void cancelFindDevice(){
        mBluetoothAdapter.cancelDiscovery();
    }

    /**
     * 判断当前设备是否在查找蓝牙设备
     */
    @SuppressLint("MissingPermission")
    public boolean isStartDiscovering(){
        if(mBluetoothAdapter.isDiscovering()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断当前设备是否未在查找蓝牙设备
     */
    @SuppressLint("MissingPermission")
    public boolean isCancelDiscovering(){
        if(!mBluetoothAdapter.isDiscovering()){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 查找设备
     */
    @SuppressLint("MissingPermission")
    public void findDevice() {
        mBluetoothAdapter.startDiscovery();
    }


    /**
     * 获取已绑定设备
     */
    @SuppressLint("MissingPermission")
    public List<BluetoothDevice> getBondedDeviceList(){
        return new ArrayList<>(mBluetoothAdapter.getBondedDevices());
    }

    /**
     * 判断蓝牙是否连接
     */
    @SuppressLint("MissingPermission")
    public boolean isConnectBlue(BluetoothSocket bluetoothSocket){
        return bluetoothSocket !=null && bluetoothSocket.isConnected();
    }
}
