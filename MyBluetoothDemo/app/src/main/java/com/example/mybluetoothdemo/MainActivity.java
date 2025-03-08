package com.example.mybluetoothdemo;//package com.example.mybluetoothdemo;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothProfile;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.Toolbar;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity {
//
//    private final String TAG = "yf";
//
//    private BlueToothController blueToothController = new BlueToothController();
//
//    private static final int REQUEST_ENABLE_BT = 1;
//
//    private ListView listView;
//    private DeviceAdapter blueToothDeviceAdapter,bondBlueToothDeviceAdapter;
//
//    private List<BluetoothDevice> deviceList = new ArrayList<>();
//    private List<BluetoothDevice> bondedDeviceList = new ArrayList<>();
//
//    private BluetoothDevice device;
//
//    private BluetoothGatt bluetoothGatt;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //检查蓝牙是否可用
//        initUI();
//
//        //判断是否有访问位置的权限，没有权限，直接申请位置权限
//        isPermission();
//
//
//        registerBluetoothReceiver();
//    }
//
//
//
//    @SuppressLint("MissingPermission")
//    private void initUI(){
//        listView = findViewById(R.id.device_list);
//        blueToothDeviceAdapter = new DeviceAdapter(deviceList,this);
//        bondBlueToothDeviceAdapter = new DeviceAdapter(bondedDeviceList,this);
//
//        findViewById(R.id.btn_write_1).setOnClickListener(view -> {
//            if (null == mWriter){
//                Log.e("cx12345","ble：发送失败：null == writer !!!!");
//            }else {
//                mWriter.setValue(new byte[]{
//                        (byte)0x0c,
//                        (byte)0x11,
//                        (byte)0x09,
//                        (byte)0x41,
//                        (byte)0x23,
//                        (byte)0x00,
//                        (byte)0x01,
//                        (byte)0x03,
//                        (byte)0xFF
//                });
//
//                mGatt.writeCharacteristic(mWriter);
//            }
//        });
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        return true;
////        MenuInflater menuInflater = getMenuInflater();
////        menuInflater.inflate(R.menu.menu_main, menu);
////        return true;
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.is_bluetooth_support) {
//            // 处理“是否支持蓝牙”
//            if (blueToothController.isBluetoothSupport()) {
//                Toast.makeText(MainActivity.this, "该设备支持蓝牙功能", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(MainActivity.this, "该设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
//            }
//        } else if (id == R.id.is_bluetooth_enabled) {
//            // 处理“蓝牙是否开启”
//            if (blueToothController.isBluetoothEnabled()) {
//                Toast.makeText(MainActivity.this, "蓝牙已开启", Toast.LENGTH_SHORT).show();
//            } else {
//                blueToothController.turnOnBlueTooth(this, REQUEST_ENABLE_BT);
//            }
//        } else if (id == R.id.bonded_device) {
//            // 处理“已配对设备”
//            setTitle("已配对的设备");
//            bondedDeviceList = blueToothController.getBondedDeviceList();
//            listView.setAdapter(bondBlueToothDeviceAdapter);
//            bondBlueToothDeviceAdapter.refresh(bondedDeviceList);
//        } else if (id == R.id.find_device) {
//            // 处理“搜索设备”
//            setTitle("可用设备");
//            if (blueToothController.isStartDiscovering()) {
//                blueToothController.cancelFindDevice();
//            }
//            blueToothController.findDevice();
//            listView.setAdapter(blueToothDeviceAdapter);
//            blueToothDeviceAdapter.refresh(deviceList);
//            listView.setOnItemClickListener(deviceBluetooth);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private AdapterView.OnItemClickListener deviceBluetooth = new AdapterView.OnItemClickListener() {
//        @SuppressLint("MissingPermission")
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            device = deviceList.get(i);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                //蓝牙绑定
////                device.createBond();
//
//                //Gatt协议连接蓝牙
//                bluetoothGatt = device.connectGatt(MainActivity.this,true,mGattCallback);
//                bluetoothGatt.connect();
//            }
//        }
//    };
//
//
//    private void showToast(String message) {
//        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
//    }
//
//    //动态获取位置权限
//    private void isPermission(){
//        if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                || (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//        }
//    }
//
//
//
//
//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (activeGatt != null) {
//            activeGatt.disconnect();
//            activeGatt.close();
//        }
//        unregisterReceiver(receiver);
//        blueToothController.getBluetoothAdapter().cancelDiscovery();
//    }
//
//
//
//    private void registerBluetoothReceiver(){
//        //filter注册广播接收器
//        IntentFilter filter = new IntentFilter();
//
//
//        //蓝牙当前状态
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//
//
//        //开始扫描蓝牙设备广播
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//
//        //找到蓝牙设备广播
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//
//        //扫描蓝牙设备结束广播
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//
//        //蓝牙设备配对状态改变广播
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//
//        //设备扫描模式改变广播
//        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//
//        registerReceiver(receiver, filter);
//    }
//
//    // 权限检查
//    private void checkPermissions() {
//        List<String> missingPermissions = new ArrayList<>();
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
//                missingPermissions.add(permission);
//            }
//        }
//        if (!missingPermissions.isEmpty()) {
//            requestPermissions(missingPermissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            for (int result : grantResults) {
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    showToast("需要权限才能使用蓝牙功能");
//                    finish();
//                    return;
//                }
//            }
//        }
//    }
//    //处理找到蓝牙设备和搜索完成的广播消息
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            //开始查找设备
//            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
//                //初始化适配器列表
//                deviceList.clear();
//                bondedDeviceList.clear();
//                blueToothDeviceAdapter.refresh(deviceList);
//                bondBlueToothDeviceAdapter.refresh((bondedDeviceList));
//            }
//            //找到蓝牙设备
//            else if(BluetoothDevice.ACTION_FOUND.equals(action)){
//                //搜到蓝牙设备
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//                //把搜索到的设备添加到已找到列表中，显示它的信息
//                deviceList.add(device);
//                blueToothDeviceAdapter.refresh(deviceList);
//
//            }
//            //查找设备结束
//            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
//                //搜索完毕
//                Toast.makeText(MainActivity.this, "选择要配对的蓝牙设备", Toast.LENGTH_SHORT).show();
//                blueToothDeviceAdapter.refresh(deviceList);
//            }
//            //配对状态
//            else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if(device == null){
//                    Toast.makeText(MainActivity.this, "无设备", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,0);
//                if(state == BluetoothDevice.BOND_BONDED){
//                    Toast.makeText(MainActivity.this, "已配对", Toast.LENGTH_SHORT).show();
//                }else if(state == BluetoothDevice.BOND_BONDING){
//                    Toast.makeText(MainActivity.this, "正在配对", Toast.LENGTH_SHORT).show();
//                }else if(state == BluetoothDevice.BOND_NONE){
//                    Toast.makeText(MainActivity.this, "未配对", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
//    };
//
//    private BluetoothGatt mGatt;
//    private BluetoothGattCharacteristic mWriter;
//
//    /**
//     * @param gatt     返回连接建立的gatt对象
//     * @param status   返回的是此次gatt操作的结果，成功了返回0
//     * @param newState 每次client连接或断开连接状态变化，
//     *                 STATE_CONNECTED 0，
//     *                 STATE_CONNECTING 1,
//     *                 STATE_DISCONNECTED 2,
//     *                 STATE_DISCONNECTING 3
//     */
//
//    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//
//            //连接成功
//            if(newState == BluetoothProfile.STATE_CONNECTED){
//                //进行服务发现
//                gatt.discoverServices();
//                Log.d(TAG,"连接成功");
//            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
//                //连接断开，处理断开逻辑
//                Log.d(TAG,"连接断开");
//            }
//        }
//
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            Log.d(TAG,"onServicesDiscovered : " + status + " ==>> " + gatt.toString());
//
//            //发现服务成功，处理服务和特征值
//            if(status == BluetoothGatt.GATT_SUCCESS){
//                //发送消息
//                mGatt = gatt;
//                BluetoothGattService service = gatt.getService(UUID.fromString("0000180a-0000-1000-8000-00805F9B34FB"));
//                mWriter = service.getCharacteristic(UUID.fromString("00002ad9-0000-1000-8000-00805F9B34FB"));
//
//                //打开消息通知
//                mGatt.setCharacteristicNotification(mWriter,true);
//                BluetoothGattDescriptor descriptor = mWriter.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                mGatt.writeDescriptor(descriptor);
//            }else {
//                Log.d(TAG,"发现服务失败");
//            }
//
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            Log.e(TAG,"onCharacteristicRead " + status);
//            //读取特征成功，处理特征值
//            if(status == BluetoothGatt.GATT_SUCCESS){
//
//            }
//        }
//
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            Log.e(TAG,"onCharacteristicWrite " + status);
//            //写入特征成功
//            if(status == BluetoothGatt.GATT_SUCCESS){
//                Log.d(TAG,"发送成功");
//            }else {
//                Log.d(TAG,"发送失败");
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//
//            //接收到数据
//            byte[] data = characteristic.getValue();
//
//            //处理接收到的数据
//            Log.d(TAG,"Received data: " + bytesToHexFun2(data));
//        }
//
//        @Override
//        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorRead(gatt, descriptor, status);
//        }
//
//        @Override
//        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorWrite(gatt, descriptor, status);
//        }
//
//        @Override
//        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
//            super.onReliableWriteCompleted(gatt, status);
//        }
//
//        @Override
//        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//            super.onReadRemoteRssi(gatt, rssi, status);
//        }
//
//        @Override
//        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
//            super.onMtuChanged(gatt, mtu, status);
//        }
//
//        @Override
//        public void onServiceChanged(@NonNull BluetoothGatt gatt) {
//            super.onServiceChanged(gatt);
//        }
//
//        @Override
//        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
//            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
//        }
//
//        @Override
//        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
//            super.onPhyRead(gatt, txPhy, rxPhy, status);
//        }
//    };
//
//    private char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
//            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//
//    private  String bytesToHexFun2(byte[] bytes) {
//        char[] buf = new char[bytes.length * 2];
//        int index = 0;
//        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
//            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
//            buf[index++] = HEX_CHAR[b & 0xf];
//        }
//
//        return new String(buf);
//    }
//
//}
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothProfile;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity {
//
//    private final String TAG = "yf";
//    private BlueToothController blueToothController = new BlueToothController();
//    private static final int REQUEST_ENABLE_BT = 1;
//
//    private ListView listView;
//    private DeviceAdapter blueToothDeviceAdapter, bondBlueToothDeviceAdapter;
//    private List<BluetoothDevice> deviceList = new ArrayList<>();
//    private List<BluetoothDevice> bondedDeviceList = new ArrayList<>();
//    private BluetoothDevice device;
//    private BluetoothGatt bluetoothGatt;
//
//    // BLE相关
//    private BluetoothGatt mGatt;
//    private BluetoothGattCharacteristic mWriter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initUI();
//        isPermission();
//        registerBluetoothReceiver();
//    }
//
//    @SuppressLint("MissingPermission")
//    private void initUI() {
//        listView = findViewById(R.id.device_list);
//        blueToothDeviceAdapter = new DeviceAdapter(deviceList, this);
//        bondBlueToothDeviceAdapter = new DeviceAdapter(bondedDeviceList, this);
//
//        findViewById(R.id.btn_write_1).setOnClickListener(view -> {
//            if (mWriter == null) {
//                Log.e("cx12345", "ble：发送失败：null == writer !!!!");
//            } else {
//                mWriter.setValue(new byte[]{
//                        (byte) 0x0c,
//                        (byte) 0x11,
//                        (byte) 0x09,
//                        (byte) 0x41,
//                        (byte) 0x23,
//                        (byte) 0x00,
//                        (byte) 0x01,
//                        (byte) 0x03,
//                        (byte) 0xFF
//                });
//                mGatt.writeCharacteristic(mWriter);
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.is_bluetooth_support) {
//            if (blueToothController.isBluetoothSupport()) {
//                showToast("该设备支持蓝牙功能");
//            } else {
//                showToast("该设备不支持蓝牙功能");
//            }
//        } else if (id == R.id.is_bluetooth_enabled) {
//            if (blueToothController.isBluetoothEnabled()) {
//                showToast("蓝牙已开启");
//            } else {
//                blueToothController.turnOnBlueTooth(this, REQUEST_ENABLE_BT);
//            }
//        } else if (id == R.id.bonded_device) {
//            setTitle("已配对的设备");
//            bondedDeviceList = blueToothController.getBondedDeviceList();
//            listView.setAdapter(bondBlueToothDeviceAdapter);
//            bondBlueToothDeviceAdapter.refresh(bondedDeviceList);
//        } else if (id == R.id.find_device) {
//            setTitle("可用设备");
//            if (blueToothController.isStartDiscovering()) {
//                blueToothController.cancelFindDevice();
//            }
//            blueToothController.findDevice();
//            listView.setAdapter(blueToothDeviceAdapter);
//            blueToothDeviceAdapter.refresh(deviceList);
//            listView.setOnItemClickListener(deviceBluetooth);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    private AdapterView.OnItemClickListener deviceBluetooth = new AdapterView.OnItemClickListener() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            device = deviceList.get(position);
//            showToast("正在连接：" + device.getName());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                bluetoothGatt = device.connectGatt(MainActivity.this, false, mGattCallback);
//            }
//        }
//    };
//
//    private void isPermission() {
//        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            }, 200);
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        blueToothController.getBluetoothAdapter().cancelDiscovery();
//        unregisterReceiver(receiver);
//        if (mGatt != null) {
//            mGatt.disconnect();
//            mGatt.close();
//        }
//    }
//
//    private void registerBluetoothReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        registerReceiver(receiver, filter);
//    }
//
//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
//                deviceList.clear();
//                bondedDeviceList.clear();
//                blueToothDeviceAdapter.refresh(deviceList);
//                bondBlueToothDeviceAdapter.refresh(bondedDeviceList);
//            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                deviceList.add(device);
//                blueToothDeviceAdapter.refresh(deviceList);
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                showToast("设备搜索完成");
//            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
//                if (state == BluetoothDevice.BOND_BONDED) {
//                    showToast(device.getName() + " 已配对");
//                } else if (state == BluetoothDevice.BOND_BONDING) {
//                    showToast("正在配对...");
//                }
//            }
//        }
//    };
//
//    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                runOnUiThread(() -> showToast("蓝牙连接成功"));
//                gatt.discoverServices();
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                runOnUiThread(() -> showToast("蓝牙连接断开"));
//                gatt.close();
//            }
//        }
//
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                mGatt = gatt;
//                BluetoothGattService service = gatt.getService(UUID.fromString("0000180a-0000-1000-8000-00805F9B34FB"));
//                if (service != null) {
//                    mWriter = service.getCharacteristic(UUID.fromString("00002ad9-0000-1000-8000-00805F9B34FB"));
//                    if (mWriter != null) {
//                        mGatt.setCharacteristicNotification(mWriter, true);
//                        BluetoothGattDescriptor descriptor = mWriter.getDescriptor(
//                                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//                        if (descriptor != null) {
//                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                            mGatt.writeDescriptor(descriptor);
//                        }
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.d(TAG, "写入成功");
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            byte[] data = characteristic.getValue();
//            Log.d(TAG, "收到数据: " + bytesToHexFun2(data));
//        }
//    };
//
//    private String bytesToHexFun2(byte[] bytes) {
//        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7',
//                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//        char[] buf = new char[bytes.length * 2];
//        int index = 0;
//        for (byte b : bytes) {
//            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
//            buf[index++] = HEX_CHAR[b & 0xf];
//        }
//        return new String(buf);
//    }
//
//    private void showToast(String message) {
//        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
//    }
//}

//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothProfile;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity {
//
//    private final String TAG = "yf";
//    private BlueToothController blueToothController = new BlueToothController();
//    private static final int REQUEST_ENABLE_BT = 1;
//
//    private ListView listView;
//    private DeviceAdapter blueToothDeviceAdapter, bondBlueToothDeviceAdapter;
//    private List<BluetoothDevice> deviceList = new ArrayList<>();
//    private List<BluetoothDevice> bondedDeviceList = new ArrayList<>();
//
//    // BLE相关
//    private BluetoothGatt mGatt;
//    private BluetoothGattCharacteristic mWriter;
//
//    // 连接状态跟踪
//    private String currentConnectedDeviceAddress = "";
//    private boolean isConnected = false;
//    private static final int LIST_TYPE_SEARCHED = 0;
//    private static final int LIST_TYPE_BONDED = 1;
//    private int currentListType = LIST_TYPE_SEARCHED;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initUI();
//        isPermission();
//        registerBluetoothReceiver();
//    }
//
//    @SuppressLint("MissingPermission")
//    private void initUI() {
//        listView = findViewById(R.id.device_list);
//        blueToothDeviceAdapter = new DeviceAdapter(deviceList, this);
//        bondBlueToothDeviceAdapter = new DeviceAdapter(bondedDeviceList, this);
//
//        findViewById(R.id.btn_write_1).setOnClickListener(view -> {
//            if (mWriter == null) {
//                Log.e("cx12345", "ble：发送失败：null == writer !!!!");
//            } else {
//                mWriter.setValue(new byte[]{
//                        (byte) 0x0c,
//                        (byte) 0x11,
//                        (byte) 0x09,
//                        (byte) 0x41,
//                        (byte) 0x23,
//                        (byte) 0x00,
//                        (byte) 0x01,
//                        (byte) 0x03,
//                        (byte) 0xFF
//                });
//                mGatt.writeCharacteristic(mWriter);
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.is_bluetooth_support) {
//            if (blueToothController.isBluetoothSupport()) {
//                showToast("该设备支持蓝牙功能");
//            } else {
//                showToast("该设备不支持蓝牙功能");
//            }
//        } else if (id == R.id.is_bluetooth_enabled) {
//            if (blueToothController.isBluetoothEnabled()) {
//                showToast("蓝牙已开启");
//            } else {
//                blueToothController.turnOnBlueTooth(this, REQUEST_ENABLE_BT);
//            }
//        } else if (id == R.id.bonded_device) {
//            setTitle("已配对的设备");
//            currentListType = LIST_TYPE_BONDED;
//            bondedDeviceList = blueToothController.getBondedDeviceList();
//            listView.setAdapter(bondBlueToothDeviceAdapter);
//            bondBlueToothDeviceAdapter.refresh(bondedDeviceList);
//        } else if (id == R.id.find_device) {
//            setTitle("可用设备");
//            currentListType = LIST_TYPE_SEARCHED;
//            if (blueToothController.isStartDiscovering()) {
//                blueToothController.cancelFindDevice();
//            }
//            blueToothController.findDevice();
//            listView.setAdapter(blueToothDeviceAdapter);
//            blueToothDeviceAdapter.refresh(deviceList);
//        }
//
//        listView.setOnItemClickListener(deviceBluetooth);
//        return super.onOptionsItemSelected(item);
//    }
//
//    private AdapterView.OnItemClickListener deviceBluetooth = new AdapterView.OnItemClickListener() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            BluetoothDevice clickedDevice;
//            if (currentListType == LIST_TYPE_SEARCHED) {
//                clickedDevice = deviceList.get(position);
//            } else {
//                clickedDevice = bondedDeviceList.get(position);
//            }
//            String clickedAddress = clickedDevice.getAddress();
//
//            if (clickedAddress.equals(currentConnectedDeviceAddress) && isConnected) {
//                // 断开连接
//                if (mGatt != null) {
//                    showToast("正在断开连接：" + clickedDevice.getName());
//                    mGatt.disconnect();
//                }
//            } else {
//                // 连接新设备
//                if (mGatt != null) {
//                    mGatt.disconnect();
//                    mGatt.close();
//                    mGatt = null;
//                    currentConnectedDeviceAddress = "";
//                    isConnected = false;
//                }
//                showToast("正在连接：" + clickedDevice.getName());
//                currentConnectedDeviceAddress = clickedAddress;
//                mGatt = clickedDevice.connectGatt(MainActivity.this, false, mGattCallback);
//            }
//        }
//    };
//
//    private void isPermission() {
//        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            }, 200);
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        blueToothController.getBluetoothAdapter().cancelDiscovery();
//        unregisterReceiver(receiver);
//        if (mGatt != null) {
//            mGatt.disconnect();
//            mGatt.close();
//            mGatt = null;
//        }
//    }
//
//    private void registerBluetoothReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        registerReceiver(receiver, filter);
//    }
//
//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
//                deviceList.clear();
//                bondedDeviceList.clear();
//                blueToothDeviceAdapter.refresh(deviceList);
//                bondBlueToothDeviceAdapter.refresh(bondedDeviceList);
//            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                deviceList.add(device);
//                blueToothDeviceAdapter.refresh(deviceList);
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                showToast("设备搜索完成");
//            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
//                if (state == BluetoothDevice.BOND_BONDED) {
//                    showToast(device.getName() + " 已配对");
//                } else if (state == BluetoothDevice.BOND_BONDING) {
//                    showToast("正在配对...");
//                }
//            }
//        }
//    };
//
//    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                runOnUiThread(() -> showToast("蓝牙连接成功"));
//                currentConnectedDeviceAddress = gatt.getDevice().getAddress();
//                isConnected = true;
//                gatt.discoverServices();
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                runOnUiThread(() -> showToast("蓝牙连接断开"));
//                currentConnectedDeviceAddress = "";
//                isConnected = false;
//                gatt.close();
//                mGatt = null;
//            }
//        }
//
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                mGatt = gatt;
//                BluetoothGattService service = gatt.getService(UUID.fromString("0000180a-0000-1000-8000-00805F9B34FB"));
//                if (service != null) {
//                    mWriter = service.getCharacteristic(UUID.fromString("00002ad9-0000-1000-8000-00805F9B34FB"));
//                    if (mWriter != null) {
//                        mGatt.setCharacteristicNotification(mWriter, true);
//                        BluetoothGattDescriptor descriptor = mWriter.getDescriptor(
//                                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//                        if (descriptor != null) {
//                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                            mGatt.writeDescriptor(descriptor);
//                        }
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.d(TAG, "写入成功");
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            byte[] data = characteristic.getValue();
//            Log.d(TAG, "收到数据: " + bytesToHexFun2(data));
//        }
//    };
//
//    private String bytesToHexFun2(byte[] bytes) {
//        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7',
//                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//        char[] buf = new char[bytes.length * 2];
//        int index = 0;
//        for (byte b : bytes) {
//            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
//            buf[index++] = HEX_CHAR[b & 0xf];
//        }
//        return new String(buf);
//    }
//
//    private void showToast(String message) {
//        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
//    }
//}

//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothProfile;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity {
//
//    private final String TAG = "yf";
//    private BlueToothController blueToothController = new BlueToothController();
//    private static final int REQUEST_ENABLE_BT = 1;
//
//    private ListView listView;
//    private DeviceAdapter blueToothDeviceAdapter, bondBlueToothDeviceAdapter;
//    private List<BluetoothDevice> deviceList = new ArrayList<>();
//    private List<BluetoothDevice> bondedDeviceList = new ArrayList<>();
//
//    // BLE相关
//    private BluetoothGatt mGatt;
//    private BluetoothGattCharacteristic mWriter;
//
//    // 连接状态跟踪
//    private String currentConnectedDeviceAddress = "";
//    private boolean isConnected = false;
//    private static final int LIST_TYPE_SEARCHED = 0;
//    private static final int LIST_TYPE_BONDED = 1;
//    private int currentListType = LIST_TYPE_SEARCHED;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initUI();
//        isPermission();
//        registerBluetoothReceiver();
//
//        findViewById(R.id.btn_write_1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isConnected) {
//                    showToast("请先连接蓝牙设备");
//                    return;
//                }
//                // 要发送的字符串
//                String message = "Hello Bluetooth!";
//                byte[] data = message.getBytes();
//                // 设置特征值并发送
//                if ((mWriter.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) != 0) {
//                    mWriter.setValue(data);
//                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        showToast("没有权限");
//                        return;
//                    }
//                    if (mGatt.writeCharacteristic(mWriter)) {
//                        showToast("发送成功: " + message);
//                        Log.d(TAG, "已发送数据: " + bytesToHexFun2(data));
//                    }
//                }
//            }
//        });
//    }
//
//
//
//    @SuppressLint("MissingPermission")
//    private void initUI() {
//        listView = findViewById(R.id.device_list);
//        blueToothDeviceAdapter = new DeviceAdapter(deviceList, this);
//        bondBlueToothDeviceAdapter = new DeviceAdapter(bondedDeviceList, this);
//
//        // 发送按钮点击事件
////        findViewById(R.id.btn_write_1).setOnClickListener(view -> {
////            if (!isConnected || mGatt == null || mWriter == null) {
////                showToast("请先连接蓝牙设备");
////                return;
////            }
////
////            // 要发送的字符串
////            String message = "Hello Bluetooth!";
////            byte[] data = message.getBytes();
////
////            // 设置特征值并发送
////            if ((mWriter.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) != 0) {
////                mWriter.setValue(data);
////                if (mGatt.writeCharacteristic(mWriter)) {
////                    showToast("发送成功: " + message);
////                    Log.d(TAG, "已发送数据: " + bytesToHexFun2(data));
////                } else {
////                    showToast("发送失败");
////                }
////            } else {
////                showToast("该特征不支持写入");
////            }
////        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.is_bluetooth_support) {
//            if (blueToothController.isBluetoothSupport()) {
//                showToast("该设备支持蓝牙功能");
//            } else {
//                showToast("该设备不支持蓝牙功能");
//            }
//        } else if (id == R.id.is_bluetooth_enabled) {
//            if (blueToothController.isBluetoothEnabled()) {
//                showToast("蓝牙已开启");
//            } else {
//                blueToothController.turnOnBlueTooth(this, REQUEST_ENABLE_BT);
//            }
//        } else if (id == R.id.bonded_device) {
//            setTitle("已配对的设备");
//            currentListType = LIST_TYPE_BONDED;
//            bondedDeviceList = blueToothController.getBondedDeviceList();
//            listView.setAdapter(bondBlueToothDeviceAdapter);
//            bondBlueToothDeviceAdapter.refresh(bondedDeviceList);
//        } else if (id == R.id.find_device) {
//            setTitle("可用设备");
//            currentListType = LIST_TYPE_SEARCHED;
//            if (blueToothController.isStartDiscovering()) {
//                blueToothController.cancelFindDevice();
//            }
//            blueToothController.findDevice();
//            listView.setAdapter(blueToothDeviceAdapter);
//            blueToothDeviceAdapter.refresh(deviceList);
//        }
//
//        listView.setOnItemClickListener(deviceBluetooth);
//        return super.onOptionsItemSelected(item);
//    }
//
//    private AdapterView.OnItemClickListener deviceBluetooth = new AdapterView.OnItemClickListener() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            BluetoothDevice clickedDevice;
//            if (currentListType == LIST_TYPE_SEARCHED) {
//                clickedDevice = deviceList.get(position);
//            } else {
//                clickedDevice = bondedDeviceList.get(position);
//            }
//            String clickedAddress = clickedDevice.getAddress();
//
//            if (clickedAddress.equals(currentConnectedDeviceAddress) && isConnected) {
//                // 断开连接
//                if (mGatt != null) {
//                    showToast("正在断开连接：" + clickedDevice.getName());
//                    mGatt.disconnect();
//                }
//            } else {
//                // 连接新设备
//                if (mGatt != null) {
//                    mGatt.disconnect();
//                    mGatt.close();
//                    mGatt = null;
//                    currentConnectedDeviceAddress = "";
//                    isConnected = false;
//                }
//                showToast("正在连接：" + clickedDevice.getName());
//                currentConnectedDeviceAddress = clickedAddress;
//                mGatt = clickedDevice.connectGatt(MainActivity.this, false, mGattCallback);
//            }
//        }
//    };
//
//    private void isPermission() {
//        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            }, 200);
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        blueToothController.getBluetoothAdapter().cancelDiscovery();
//        unregisterReceiver(receiver);
//        if (mGatt != null) {
//            mGatt.disconnect();
//            mGatt.close();
//            mGatt = null;
//        }
//    }
//
//    private void registerBluetoothReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        registerReceiver(receiver, filter);
//    }
//
//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
//                deviceList.clear();
//                bondedDeviceList.clear();
//                blueToothDeviceAdapter.refresh(deviceList);
//                bondBlueToothDeviceAdapter.refresh(bondedDeviceList);
//            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                deviceList.add(device);
//                blueToothDeviceAdapter.refresh(deviceList);
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                showToast("设备搜索完成");
//            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
//                if (state == BluetoothDevice.BOND_BONDED) {
//                    showToast(device.getName() + " 已配对");
//                } else if (state == BluetoothDevice.BOND_BONDING) {
//                    showToast("正在配对...");
//                }
//            }
//        }
//    };
//
//    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                runOnUiThread(() -> showToast("蓝牙连接成功"));
//                currentConnectedDeviceAddress = gatt.getDevice().getAddress();
//                isConnected = true;
//                gatt.discoverServices();
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                runOnUiThread(() -> showToast("蓝牙连接断开"));
//                currentConnectedDeviceAddress = "";
//                isConnected = false;
//                gatt.close();
//                mGatt = null;
//            }
//        }
//
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                mGatt = gatt;
//                BluetoothGattService service = gatt.getService(UUID.fromString("0000180a-0000-1000-8000-00805F9B34FB"));
//                if (service != null) {
//                    mWriter = service.getCharacteristic(UUID.fromString("00002ad9-0000-1000-8000-00805F9B34FB"));
//                    if (mWriter != null) {
//                        // 启用通知
//                        mGatt.setCharacteristicNotification(mWriter, true);
//                        BluetoothGattDescriptor descriptor = mWriter.getDescriptor(
//                                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//                        if (descriptor != null) {
//                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                            mGatt.writeDescriptor(descriptor);
//                        }
//                        // 检查写权限
//                        int properties = mWriter.getProperties();
//                        if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE) == 0) {
//                            showToast("该特征不支持写入");
//                        }
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                Log.d(TAG, "数据写入成功");
//            } else {
//                Log.e(TAG, "数据写入失败");
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            byte[] data = characteristic.getValue();
//            String received = new String(data);
//            Log.d(TAG, "收到数据: " + received);
//            runOnUiThread(() -> Toast.makeText(MainActivity.this, "收到: " + received, Toast.LENGTH_SHORT).show());
//        }
//    };
//
//    private String bytesToHexFun2(byte[] bytes) {
//        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7',
//                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//        char[] buf = new char[bytes.length * 2];
//        int index = 0;
//        for (byte b : bytes) {
//            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
//            buf[index++] = HEX_CHAR[b & 0xf];
//        }
//        return new String(buf);
//    }
//
//    private void showToast(String message) {
//        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
//    }
//}


//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCallback;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothProfile;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.Toast;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class MainActivity extends AppCompatActivity {
//
//    private final String TAG = "yf";
//
//    private BlueToothController blueToothController = new BlueToothController();
//
//    private static final int REQUEST_ENABLE_BT = 1;
//
//    private ListView listView;
//    private DeviceAdapter blueToothDeviceAdapter,bondBlueToothDeviceAdapter;
//
//    private List<BluetoothDevice> deviceList = new ArrayList<>();
//    private List<BluetoothDevice> bondedDeviceList = new ArrayList<>();
//
//    private BluetoothDevice device;
//
//    private BluetoothGatt bluetoothGatt;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initUI();
//
//        //判断是否有访问位置的权限，没有权限，直接申请位置权限
//        isPermission();
//
//
//        registerBluetoothReceiver();
//    }
//
//
//
//    @SuppressLint("MissingPermission")
//    private void initUI(){
//        listView = findViewById(R.id.device_list);
//        blueToothDeviceAdapter = new DeviceAdapter(deviceList,this);
//        bondBlueToothDeviceAdapter = new DeviceAdapter(bondedDeviceList,this);
//
//        findViewById(R.id.btn_write_1).setOnClickListener(view -> {
//            if (null == mWriter){
//                Log.e("cx12345","ble：发送失败：null == writer !!!!");
//            }else {
//                mWriter.setValue(new byte[]{
//                        (byte)0x0c,
//                        (byte)0x11,
//                        (byte)0x09,
//                        (byte)0x41,
//                        (byte)0x23,
//                        (byte)0x00,
//                        (byte)0x01,
//                        (byte)0x03,
//                        (byte)0xFF
//                });
//
//                mGatt.writeCharacteristic(mWriter);
//                showToast("发送成功");
//                Log.d(TAG,"ble：发送成功");
//            }
//        });
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        return true;
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.is_bluetooth_support) {
//            if (blueToothController.isBluetoothSupport()) {
//                showToast("该设备支持蓝牙功能");
//            } else {
//                showToast("该设备不支持蓝牙功能");
//            }
//        } else if (id == R.id.is_bluetooth_enabled) {
//            if (blueToothController.isBluetoothEnabled()) {
//                showToast("蓝牙已开启");
//            } else {
//                blueToothController.turnOnBlueTooth(this, REQUEST_ENABLE_BT);
//            }
//        } else if (id == R.id.bonded_device) {
//            setTitle("已配对的设备");
//           // currentListType = LIST_TYPE_BONDED;
//            bondedDeviceList = blueToothController.getBondedDeviceList();
//            listView.setAdapter(bondBlueToothDeviceAdapter);
//            bondBlueToothDeviceAdapter.refresh(bondedDeviceList);
//        } else if (id == R.id.find_device) {
//            setTitle("可用设备");
//           // currentListType = LIST_TYPE_SEARCHED;
//            if (blueToothController.isStartDiscovering()) {
//                blueToothController.cancelFindDevice();
//            }
//            blueToothController.findDevice();
//            listView.setAdapter(blueToothDeviceAdapter);
//            blueToothDeviceAdapter.refresh(deviceList);
//        }
//
//        listView.setOnItemClickListener(deviceBluetooth);
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private AdapterView.OnItemClickListener deviceBluetooth = new AdapterView.OnItemClickListener() {
//        @SuppressLint("MissingPermission")
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            device = deviceList.get(i);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                //蓝牙绑定
////                device.createBond();
//
//                //Gatt协议连接蓝牙
//                bluetoothGatt = device.connectGatt(MainActivity.this,true,mGattCallback);
//                bluetoothGatt.connect();
//            }
//        }
//    };
//
//
//
//
//    //动态获取位置权限
//    private void isPermission(){
//        if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                || (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//        }
//    }
//
//
//
//
//    @SuppressLint("MissingPermission")
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // 停止设备搜索
//        blueToothController.getBluetoothAdapter().cancelDiscovery();
//        //注销广播
//        unregisterReceiver(receiver);
//    }
//
//
//    private void registerBluetoothReceiver(){
//        //filter注册广播接收器
//        IntentFilter filter = new IntentFilter();
//
//
//        //蓝牙当前状态
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//
//
//        //开始扫描蓝牙设备广播
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//
//        //找到蓝牙设备广播
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//
//        //扫描蓝牙设备结束广播
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//
//        //蓝牙设备配对状态改变广播
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//
//        //设备扫描模式改变广播
//        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//
//        registerReceiver(receiver, filter);
//    }
//
//    //处理找到蓝牙设备和搜索完成的广播消息
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            //开始查找设备
//            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
//                //初始化适配器列表
//                deviceList.clear();
//                bondedDeviceList.clear();
//                blueToothDeviceAdapter.refresh(deviceList);
//                bondBlueToothDeviceAdapter.refresh((bondedDeviceList));
//            }
//            //找到蓝牙设备
//            else if(BluetoothDevice.ACTION_FOUND.equals(action)){
//                //搜到蓝牙设备
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//                //把搜索到的设备添加到已找到列表中，显示它的信息
//                deviceList.add(device);
//                blueToothDeviceAdapter.refresh(deviceList);
//
//            }
//            //查找设备结束
//            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
//                //搜索完毕
//                Toast.makeText(MainActivity.this, "选择要配对的蓝牙设备", Toast.LENGTH_SHORT).show();
//                blueToothDeviceAdapter.refresh(deviceList);
//            }
//            //配对状态
//            else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if(device == null){
//                    Toast.makeText(MainActivity.this, "无设备", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,0);
//                if(state == BluetoothDevice.BOND_BONDED){
//                    Toast.makeText(MainActivity.this, "已配对", Toast.LENGTH_SHORT).show();
//                }else if(state == BluetoothDevice.BOND_BONDING){
//                    Toast.makeText(MainActivity.this, "正在配对", Toast.LENGTH_SHORT).show();
//                }else if(state == BluetoothDevice.BOND_NONE){
//                    Toast.makeText(MainActivity.this, "未配对", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
//    };
//
//    private BluetoothGatt mGatt;
//    private BluetoothGattCharacteristic mWriter;
//
//    /**
//     * @param gatt     返回连接建立的gatt对象
//     * @param status   返回的是此次gatt操作的结果，成功了返回0
//     * @param newState 每次client连接或断开连接状态变化，
//     *                 STATE_CONNECTED 0，
//     *                 STATE_CONNECTING 1,
//     *                 STATE_DISCONNECTED 2,
//     *                 STATE_DISCONNECTING 3
//     */
//
//    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//
//            //连接成功
//            if(newState == BluetoothProfile.STATE_CONNECTED){
//                //进行服务发现
//                gatt.discoverServices();
//                Log.d(TAG,"连接成功");
//            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
//                //连接断开，处理断开逻辑
//                Log.d(TAG,"连接断开");
//            }
//        }
//
//        @SuppressLint("MissingPermission")
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            Log.d(TAG,"onServicesDiscovered : " + status + " ==>> " + gatt.toString());
//
//            //发现服务成功，处理服务和特征值
//            if(status == BluetoothGatt.GATT_SUCCESS){
//                //发送消息
//                mGatt = gatt;
//                BluetoothGattService service = gatt.getService(UUID.fromString("0000180a-0000-1000-8000-00805F9B34FB"));
//                mWriter = service.getCharacteristic(UUID.fromString("00002ad9-0000-1000-8000-00805F9B34FB"));
//
//                //打开消息通知
//                mGatt.setCharacteristicNotification(mWriter,true);
//                BluetoothGattDescriptor descriptor = mWriter.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                mGatt.writeDescriptor(descriptor);
//            }else {
//                Log.d(TAG,"发现服务失败");
//            }
//
//        }
//
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            Log.e(TAG,"onCharacteristicRead " + status);
//            //读取特征成功，处理特征值
//            if(status == BluetoothGatt.GATT_SUCCESS){
//
//            }
//        }
//
//        @Override
//        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            Log.e(TAG,"onCharacteristicWrite " + status);
//            //写入特征成功
//            if(status == BluetoothGatt.GATT_SUCCESS){
//                Log.d(TAG,"发送成功");
//            }else {
//                Log.d(TAG,"发送失败");
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//
//            //接收到数据
//            byte[] data = characteristic.getValue();
//
//            //处理接收到的数据
//            Log.d(TAG,"Received data: " + bytesToHexFun2(data));
//        }
//
//        @Override
//        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorRead(gatt, descriptor, status);
//        }
//
//        @Override
//        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//            super.onDescriptorWrite(gatt, descriptor, status);
//        }
//
//        @Override
//        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
//            super.onReliableWriteCompleted(gatt, status);
//        }
//
//        @Override
//        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//            super.onReadRemoteRssi(gatt, rssi, status);
//        }
//
//        @Override
//        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
//            super.onMtuChanged(gatt, mtu, status);
//        }
//
//        @Override
//        public void onServiceChanged(@NonNull BluetoothGatt gatt) {
//            super.onServiceChanged(gatt);
//        }
//
//        @Override
//        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
//            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
//        }
//
//        @Override
//        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
//            super.onPhyRead(gatt, txPhy, rxPhy, status);
//        }
//    };
//
//    private char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
//            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//
//    private  String bytesToHexFun2(byte[] bytes) {
//        char[] buf = new char[bytes.length * 2];
//        int index = 0;
//        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
//            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
//            buf[index++] = HEX_CHAR[b & 0xf];
//        }
//
//        return new String(buf);
//    }
//
//    private void showToast(String message) {
//        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
//    }
//}



import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "yf";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_WRITE_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private static final UUID DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    // 蓝牙相关组件
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic writeCharacteristic;

    // 界面组件
    private ListView listView;
    private DeviceAdapter deviceAdapter, bondedDeviceAdapter;
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    private List<BluetoothDevice> bondedDeviceList = new ArrayList<>();
    private int currentListType = 0; // 0: 搜索设备 1: 已配对设备

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        checkPermissions();
        initBluetooth();
        registerBluetoothReceiver();
    }

    @SuppressLint("MissingPermission")
    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            showToast("设备不支持蓝牙");
            finish();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            refreshBondedDevices();
        }
    }

    private void initUI() {
        listView = findViewById(R.id.device_list);
        deviceAdapter = new DeviceAdapter(deviceList, this);
        bondedDeviceAdapter = new DeviceAdapter(bondedDeviceList, this);
        listView.setAdapter(deviceAdapter);

        // 设备点击监听
        listView.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice device = currentListType == 0 ?
                    deviceList.get(position) : bondedDeviceList.get(position);
            connectToDevice(device);
        });

        // 发送按钮
        findViewById(R.id.btn_write_1).setOnClickListener(v -> {
            if (bluetoothGatt == null || writeCharacteristic == null) {
                showToast("请先连接设备");
                return;
            }

//            byte[] data = new byte[]{
//                    (byte) 0x0c, (byte) 0x11, (byte) 0x09, (byte) 0x41,
//                    (byte) 0x23, (byte) 0x00, (byte) 0x01, (byte) 0x03, (byte) 0xFF
//            };

            // 要发送的字符串
            String message = "open_led";
            byte[] data = message.getBytes();

            writeCharacteristic.setValue(data);
            if (!bluetoothGatt.writeCharacteristic(writeCharacteristic)) {
                showToast("发送失败");
                return;
            }
            showToast("发送成功");
        });
    }

    // 菜单初始化
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 菜单项处理
    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.is_bluetooth_support) {
            handleBluetoothSupport();
            return true;
        } else if (id == R.id.is_bluetooth_enabled) {
            handleBluetoothStatus();
            return true;
        } else if (id == R.id.bonded_device) {
            showBondedDevices();
            return true;
        } else if (id == R.id.find_device) {
            startDiscovery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleBluetoothSupport() {
        showToast(bluetoothAdapter != null ?
                "支持蓝牙" : "不支持蓝牙");
    }

    private void handleBluetoothStatus() {
        if (bluetoothAdapter.isEnabled()) {
            showToast("蓝牙已启用");
        } else {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @SuppressLint("MissingPermission")
    private void showBondedDevices() {
        currentListType = 1;
        refreshBondedDevices();
        listView.setAdapter(bondedDeviceAdapter);
        setTitle("已配对设备 (" + bondedDeviceList.size() + ")");
    }

    @SuppressLint("MissingPermission")
    private void startDiscovery() {
        currentListType = 0;
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        deviceList.clear();
        deviceAdapter.notifyDataSetChanged();
        bluetoothAdapter.startDiscovery();
        listView.setAdapter(deviceAdapter);
        setTitle("正在搜索设备...");
    }

    @SuppressLint("MissingPermission")
    private void refreshBondedDevices() {
        bondedDeviceList.clear();
        bondedDeviceList.addAll(bluetoothAdapter.getBondedDevices());
        bondedDeviceAdapter.notifyDataSetChanged();
    }

    @SuppressLint("MissingPermission")
    private void connectToDevice(BluetoothDevice device) {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
        bluetoothGatt = device.connectGatt(this, false, gattCallback);
        showToast("连接中: " + device.getName());
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                showToast("连接成功，发现服务...");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                showToast("连接断开");
                gatt.close();
                bluetoothGatt = null;
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService service = gatt.getService(SERVICE_UUID);
                if (service != null) {
                    writeCharacteristic = service.getCharacteristic(CHARACTERISTIC_WRITE_UUID);
                    if (writeCharacteristic != null) {
                        enableNotifications(gatt, writeCharacteristic);
                        showToast("服务发现完成");
                    } else {
                        showToast("未找到写入特征");
                    }
                } else {
                    showToast("未找到服务");
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            runOnUiThread(() -> {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "写入成功");
                    showToast("发送成功");
                } else {
                    Log.e(TAG, "写入失败，状态码: " + status);
                    showToast("发送失败");
                }
            });
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            Log.d(TAG, "收到数据: " + bytesToHex(data));
        }
    };

    @SuppressLint("MissingPermission")
    private void enableNotifications(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        gatt.setCharacteristicNotification(characteristic, true);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(DESCRIPTOR_UUID);
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> neededPermissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN)
                        != PackageManager.PERMISSION_GRANTED) {
                    neededPermissions.add(Manifest.permission.BLUETOOTH_SCAN);
                }
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {
                    neededPermissions.add(Manifest.permission.BLUETOOTH_CONNECT);
                }
            }
            if (!neededPermissions.isEmpty()) {
                requestPermissions(neededPermissions.toArray(new String[0]), 1);
            }
        }
    }

    private void registerBluetoothReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothReceiver, filter);
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !deviceList.contains(device)) {
                    deviceList.add(device);
                    deviceAdapter.notifyDataSetChanged();
                    setTitle("发现设备 (" + deviceList.size() + ")");
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                setTitle("正在搜索设备...");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle("搜索完成 (" + deviceList.size() + "设备)");
            }
        }
    };

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
        unregisterReceiver(bluetoothReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                showToast("蓝牙已启用");
                refreshBondedDevices();
            } else {
                showToast("蓝牙未启用");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    showToast("需要权限才能使用蓝牙功能");
                    finish();
                    return;
                }
            }
        }
    }
}