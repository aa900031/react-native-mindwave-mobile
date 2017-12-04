
package com.reactlibrary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.neurosky.connection.ConnectionStates;
import com.neurosky.connection.DataType.MindDataType;
import com.neurosky.connection.EEGPower;
import com.neurosky.connection.TgStreamHandler;
import com.neurosky.connection.TgStreamReader;

import javax.annotation.Nullable;

public class RNMindWaveMobileModule extends ReactContextBaseJavaModule {

    private final String TAG = "RNMindWaveMobile";

    private final ReactApplicationContext reactContext;
    private BluetoothAdapter mBluetoothAdapter;

    private TgStreamReader mTgStreamReader;

    // events
    private final String EVENT_DID_CONNECT = "didConnect";
    private final String EVENT_DISCONNECT = "didDisconnect";
    private final String EVENT_DEVICE_FOUND = "deviceFound";
    private final String EVENT_EEG_POWERLOWBETA = "eegPowerLowBeta";
    private final String EVENT_EEG_POWERDELTA = "eegPowerDelta";
    private final String EVENT_EEG_BLINK = "eegBlink";
    private final String EVENT_ESENSE = "eSense";
    private final String EVENT_MWMBAUDRATE = "mwmBaudRate";

    private final int DEFAULT_ESENSE = -1;

    public RNMindWaveMobileModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNMindWaveMobile";
    }

    @ReactMethod
    public void instance() {

    }

    @ReactMethod
    public void scan() {
        BluetoothAdapter bluetoothAdapter = getDefaultBluetoothAdapter();
        if (bluetoothAdapter == null) {
            Log.d(TAG, "======bluetooth not enabled");
            return;
        }
        Log.d(TAG, "=======start scan");
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        //register the receiver for scanning
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.reactContext.registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    @ReactMethod
    public void connect(String deviceId) {
        BluetoothAdapter bluetoothAdapter = getDefaultBluetoothAdapter();
        if (bluetoothAdapter == null) {
            return;
        }
        BluetoothDevice remoteDevice = bluetoothAdapter.getRemoteDevice(deviceId);
        if (mTgStreamReader == null) {
            mTgStreamReader = new TgStreamReader(remoteDevice, callback);
            mTgStreamReader.startLog();
        } else {
            mTgStreamReader.changeBluetoothDevice(remoteDevice);

        }
        mTgStreamReader.connectAndStart();
    }

    @ReactMethod
    public void disconnect() {
        if (mTgStreamReader != null) {
            mTgStreamReader.stop();
            mTgStreamReader.close();
        }
    }

    private void onDeviceFound(BluetoothDevice device) {
        WritableMap params = Arguments.createMap();
        params.putString("id", device.getAddress());
        params.putString("name", device.getName());
        params.putString("mfgId", device.getAddress());

        sendEvent(EVENT_DEVICE_FOUND, params);
    }

    private void sendEEGPower(EEGPower power) {
        // send EEGPowerLowBeta event
        WritableMap lowbeta_params = Arguments.createMap();
        lowbeta_params.putInt("lowBeta", power.lowBeta);
        lowbeta_params.putInt("highBeta", power.highBeta);
        lowbeta_params.putInt("lowGamma", power.lowGamma);
        lowbeta_params.putInt("midGamma", power.middleGamma);
        sendEvent(EVENT_EEG_POWERLOWBETA, lowbeta_params);
        // send EEGPowerDelta event
        WritableMap delta_params = Arguments.createMap();
        delta_params.putInt("delta", power.delta);
        delta_params.putInt("theta", power.theta);
        delta_params.putInt("lowAlpha", power.lowAlpha);
        delta_params.putInt("highAlpha", power.highAlpha);
        sendEvent(EVENT_EEG_POWERDELTA, delta_params);
    }

    private void sendESense(int poorSignal, int attention, int meditation) {
        WritableMap params = Arguments.createMap();
        params.putInt("poorSignal", poorSignal);
        params.putInt("attention", attention);
        params.putInt("meditation", meditation);
        sendEvent(EVENT_ESENSE, params);
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private BluetoothAdapter getDefaultBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mBluetoothAdapter;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "mReceiver()");
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onDeviceFound(device);
            }
        }
    };

    private TgStreamHandler callback = new TgStreamHandler() {

        @Override
        public void onStatesChanged(int connectionStates) {
            Log.d(TAG, "connectionStates change to: " + connectionStates);
            WritableMap params;
            switch (connectionStates) {
                case ConnectionStates.STATE_CONNECTED:
                    params = Arguments.createMap();
                    params.putBoolean("success", true);
                    sendEvent(EVENT_DID_CONNECT, params);
                    break;
                case ConnectionStates.STATE_DISCONNECTED:
                    params = Arguments.createMap();
                    params.putBoolean("success", true);
                    sendEvent(EVENT_DISCONNECT, params);
                    break;
                case ConnectionStates.STATE_ERROR:
                case ConnectionStates.STATE_FAILED:
                    Log.d(TAG, "Connect error, Please try again!");
                    params = Arguments.createMap();
                    params.putBoolean("success", false);
                    sendEvent(EVENT_DID_CONNECT, params);
                    break;
            }
        }

        @Override
        public void onChecksumFail(byte[] bytes, int i, int i1) {
            // pass
        }

        @Override
        public void onRecordFail(int i) {
            // pass
        }

        @Override
        public void onDataReceived(int datatype, int data, Object obj) {
            switch(datatype) {
            case MindDataType.CODE_EEGPOWER:
                EEGPower power = (EEGPower) obj;
                if (power.isValidate()) {
                    sendEEGPower(power);
                }
                break;
            case MindDataType.CODE_ATTENTION:
                sendESense(DEFAULT_ESENSE, data, DEFAULT_ESENSE);
                break;
            case MindDataType.CODE_MEDITATION:
                sendESense(DEFAULT_ESENSE, DEFAULT_ESENSE, data);
                break;
            case MindDataType.CODE_POOR_SIGNAL:
                sendESense(data, DEFAULT_ESENSE, DEFAULT_ESENSE);
                break;
            }
        }

    };
}