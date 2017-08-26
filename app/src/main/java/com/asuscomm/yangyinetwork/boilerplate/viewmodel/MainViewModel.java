package com.asuscomm.yangyinetwork.boilerplate.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.asuscomm.yangyinetwork.boilerplate.databinding.ActivityMainBinding;
import com.lge.hardware.IRBlaster.Device;
import com.lge.hardware.IRBlaster.IRAction;
import com.lge.hardware.IRBlaster.IRBlaster;
import com.lge.hardware.IRBlaster.IRBlasterCallback;
import com.lge.hardware.IRBlaster.IRFunction;
import com.lge.hardware.IRBlaster.LearnedIrData;
import com.lge.hardware.IRBlaster.ResultCode;

/**
 * Created by jaeyoung on 8/22/17.
 */

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";

    @NonNull
    public ObservableField<String> mTitle = new ObservableField<>("initial");
    private Context mContext;
    private IRBlaster mIR;
    private Device mDevice;
    private IRFunction mIrFunction;
    private int mLearningFuncId;
    private String mLearningFuncName = "12345";

    public MainViewModel() {
    }



    private void initIrblaster() {
        mIR = IRBlaster.getIRBlaster(mContext, new IRBlasterCallback() {
            @Override
            public void IRBlasterReady() {
                Log.d(TAG, "IRBlasterReady: ");


                Device[] devices = mIR.getDevices();
                mDevice = devices[0];
                send();

                Log.d(TAG, "IRBlasterReady: mIR.isLearningSupported()=" + mIR.isLearningSupported());
//                startLearning();
            }

            @Override
            public void learnIRCompleted(int i) {
                Log.d(TAG, "learnIRCompleted: ");
                LearnedIrData learnedData = mIR.getLearnedData();

                if (learnedData != null) {
                    Log.d(TAG, "learnIRCompleted: learnedData=" + learnedData.toString());
                } else {
                    Log.d(TAG, "learnIRCompleted: learnedData is null");
                }
                if (i == ResultCode.SUCCESS) {
                    mTitle.set("learn IR Success");
                    Log.d(TAG, "learnIRCompleted: ResultCode is SUCCESS");
                } else {
                    mTitle.set("learn IR Fail");
                    Log.d(TAG, "learnIRCompleted: ResultCode is not SUCCESS, code=" + i);
                }

                Log.d(TAG, "learnIRCompleted() called with: mDevice.Id = " +
                        "[" + mDevice.Id + "mLearningFuncId = [" + mLearningFuncId + "]");
                mLearningFuncId = mIR.addLearnedIrFunction(mDevice.Id, mLearningFuncName);
//                mIR.editIrFunctionWithLearnedData(mDevice.Id, mLearningFuncId);
                updateDevice();
//                mLearningFuncId = mIR.addLearnedIrFunction(mDevice.Id, mLearningFuncName);
            }

            @Override
            public void newDeviceId(int i) {
                Log.d(TAG, "newDeviceId: ");
            }

            @Override
            public void failure(int i) {
                Log.d(TAG, "failure: ");
            }
        });

        if (mIR != null) {
            Log.d(TAG, "initIrblaster: mIR initialize success");
        }
    }

    private void updateDevice() {
        if (mDevice != null) {
            Device device = mIR.getDeviceById(mDevice.Id);
            if (device != null) {
                Log.d(TAG, "Device with ID [" + mDevice.Id + "] is found." + " Update.");
                mDevice = device;
            }
        } else {
            Log.w(TAG, "Cannot find the device, instance is null.");
        }
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
        initIrblaster();
    }

    public void attachView(ActivityMainBinding binding) {
//        binding.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                send();
//            }
//        });

        binding.btnLearn.setOnClickListener(
                (v) -> {
                    startLearning();
                }
        );

        binding.btnSend.setOnClickListener(
                (v) -> {
                    send();
                }
        );
    }

    private void send() {
        mIrFunction = mDevice.KeyFunctions.get(mDevice.KeyFunctions.size() - 1);
        mTitle.set("Send function id=" + mIrFunction.Id);
        mIR.sendIR(new IRAction(mDevice.Id, mIrFunction.Id, 300));
    }

    private void startLearning() {
//        Log.d(TAG, "startLearning: sendIr");
//        String hexs = "0000 006D 0000 001E 0146 00A1 0018 003F 0018 0018 0018 0018 0018 0018 0018 003F 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 003F 0018 003F 0018 0018 0018 003F 0018 0018 0018 0018 0018 0018 0018 003F 0018 003F 0018 003F 0018 8BE8";
//        hexs = "0000 006D 0000 001E 0146 00A1 0018 003F 0018 0018 0018 0018 0018 0018 0018 003F 0018 0018 0018 0018 0018 0018 0018 003F 0018 003F 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 0018 003F 0018 0018 0018 003F 0018 0018 0018 0018 0018 0018 0018 003F 0018 8C0F";
//        hexs = "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 05F7 015B 0057 0016 0E6C";
//        hexs = "0000 006C 0022 0002 015B 00AD 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0041 0016 0622 015B 0057 0016 0E6C";
//        String hexsWithComma = "3398, 390, 82, 206, 18, 82, 166, 70, \n" +
//                "\t146, 710, 65506, 434, 270, 370, 1038, 614, \n" +
//                "\t18, 190, 106, 470, 26, 66, 162, 282, \n" +
//                "\t6, 726, 194, 454, 390, 1166, 150, 302, \n" +
//                "\t2058, 66, 1734, 1000";
//
//        hexs = hexsWithComma.replace(", ", " ").replace("\n", "").replace("\t", "");
//
//        String[] split = hexs.split(" ");
//        int[] codes = new int[split.length];
//
//        for (int idx = 0; idx < split.length; idx++) {
////            int decimal = Integer.parseInt(split[idx], 16);
//            int decimal = Integer.parseInt(split[idx]);
//            codes[idx] = decimal;
//        }
//
//        Log.d(TAG, "startLearning: codes=" + Arrays.toString(codes));
//
//        int[] LGTVPower = {9000, 4500, 578, 552, 578, 552, 578, 1684, 578, 552, 578, 552, 578, 552,
//                578, 552, 578, 552, 578, 1684, 578, 1684, 578, 552, 578, 1684, 578, 1684, 578,
//                1684, 578, 1684, 578, 1684, 578, 552, 578, 552, 578, 552, 578, 1684, 578, 552, 578,
//                552, 578, 552, 578, 552, 578, 1684, 578, 1684, 578, 1684, 578, 552, 578, 1684, 578,
//                1684, 578, 1684, 578, 1684, 578, 39342, 9000, 2236, 578, 96184, 9000, 2236, 578,
//                96184};
//        mIR.sendIRPattern(38000, codes);
        Log.d(TAG, "startLearning: ");
        mTitle.set("Start Learning");
        mLearningFuncId = mIrFunction.Id;
        mIR.startIrLearning();
    }
}
