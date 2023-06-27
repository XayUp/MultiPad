package com.xayup.multipad.project.keysounds;

import android.app.Activity;
import android.content.Context;
import com.xayup.debug.XLog;
import com.xayup.multipad.load.Project;
import com.xayup.multipad.load.thread.LoadProject;
import com.xayup.multipad.pads.PadPressCallInterface;

import java.io.File;
import java.util.List;

public class KeySounds extends SoundLoader implements Project.SoundInterface, PadPressCallInterface {
    protected Activity context;
    
    public KeySounds(Context context){
        super(context);
        this.context = (Activity) context;
    }
    
    public void parse(File keysound_path, File sample_path, LoadProject.LoadingProject mLoadingProject){
        new KeySoundsReader().read(keysound_path, sample_path, this, mLoadingProject);
    }
    
    public void clear(){
        release();
    }
    
    @Override
    public boolean playSound(int chain, int x, int y) {
        XLog.v("Try play sound", String.valueOf(chain)+x+y);
        return playSound(String.valueOf(chain)+x+y);
    }

    @Override
    public boolean stopSound(int chain, int x, int y) {
        stopAll();
        return false;
    }

    @Override
    public boolean call(int chain, int x, int y) {
        return playSound(chain, x, y);
    }
}
