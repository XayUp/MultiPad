package com.xayup.ui.options;

import android.view.View;

public interface OptionsItemInterface {
    int TYPE_SIMPLE = 0;
    int TYPE_SIMPLE_WITH_CHECKBOX = 0;
    int TYPE_CENTER_WITH_IMAGE = 2;

    View getItemView();
    int getType();
    void setTitle(String title);
    void setDescription(String desc);
    void setOnClick(View.OnClickListener onClick);
    void setOnLongClick(View.OnLongClickListener onClick);
    void setEnabled(boolean enable);
    boolean enabled();
}