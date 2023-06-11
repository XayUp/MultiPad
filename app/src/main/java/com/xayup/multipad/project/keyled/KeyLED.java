package com.xayup.multipad.project.keyled;

import android.app.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.xayup.debug.XLog;
import com.xayup.multipad.R;
import com.xayup.multipad.load.thread.LoadProject;
import com.xayup.multipad.pads.PadPressCallInterface;
import com.xayup.multipad.pads.Render.MakePads;
import com.xayup.multipad.project.MapData;
import com.xayup.multipad.project.keyled.KeyLEDReader;
import com.xayup.multipad.load.Project;
import com.xayup.multipad.load.ProjectMapData;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.*;

public class KeyLED extends Project implements MapData, Project.KeyLEDInterface, PadPressCallInterface, Runnable {
    protected Activity context;
    protected LedMap mLedMap;
    protected AtomicBoolean runnig;
    protected Thread led_thread;
    protected List<List<int[]>> leds_frames;
    protected List<int[]> delays;
    protected final int DELAYS_INDEX = 0;
    protected final int DELAYS_DELAY = 1;
    protected Colors colors;
    public KeyLED(Activity context){
        this.context = context;
        this.mLedMap = new LedMap();
        this.runnig = new AtomicBoolean(false);
        this.leds_frames = new ArrayList<>();
        this.delays = new ArrayList<>();
        this.colors = new Colors(context);
    }
    
    public void parse(File keyled_file, LoadProject.LoadingProject mLoadingProject){
        new KeyLEDReader().read(keyled_file, mLedMap, mLoadingProject);
    }

    @Override
    public boolean showLed(int chain, int x, int y) {
        XLog.v("Try show led", "");
        int[][] frames = mLedMap.getLedData(chain, x, y, 0);
        if(frames != null && frames.length > 0) {
            XLog.v("Led", Arrays.deepToString(frames));
            leds_frames.add(new ArrayList<>(List.of(frames)));
            if (led_thread == null || !led_thread.isAlive()) {
                runnig.set(true);
                delays.clear();
                (led_thread = new Thread(this)).start();
            }
        }
        return false;
    }

    @Override
    public boolean breakLed(int chain, int x, int y) {
        return false;
    }

    @Override
    public boolean breakAll() {
        return false;
    }

    @Override
    public void call(int chain, int x, int y) {
        showLed(chain, x, y);
    }

    @Override
    public void run() {
        XLog.v("Run thread led", "");
        while(!leds_frames.isEmpty()){
            if (!runnig.get()) break;
            int leds_count = leds_frames.size();
            for(int i = 0; i < leds_count; i++){
                if (leds_frames.get(i).isEmpty()) {
                    String tmp = String.valueOf(leds_frames.remove(i));
                    XLog.v("Frame", tmp);
                    leds_count--;
                } else {
                    int[] led_data;
                    while((led_data = leds_frames.get(i).remove(0))[FRAME_TYPE] != FRAME_TYPE_DELAY) {
                        XLog.v("led data", Arrays.toString(led_data));
                        onUi(led_data[FRAME_PAD_X], led_data[FRAME_PAD_Y], (byte) led_data[FRAME_VALUE]);
                    }
                    delays.add(new int[]{i, led_data[FRAME_VALUE]});
                }
            }
            
        }
        XLog.v("Finish thread led", "");
    }
    public void onUi(int x, int y, byte velocity){
        context.runOnUiThread(() -> {
            try {
                Log.v("ID", String.valueOf(MakePads.PadID.getId(x, y)));
                context.findViewById(MakePads.PadID.getId(x, y)).findViewById(R.id.led).setBackgroundColor(colors.colorFromVelocity(velocity));
            } catch (NullPointerException n) {
                Log.e("KeyLED", "Null led frame?");
            }
        });
    }
}
