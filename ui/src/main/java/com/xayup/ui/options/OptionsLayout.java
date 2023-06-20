package com.xayup.ui.options;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.xayup.ui.R;

import java.util.ArrayList;
import java.util.List;

public class OptionsLayout {
    protected Context context;
    protected final ViewGroup layout;
    protected List<OptionsPage> flipper_pages;
    protected ViewFlipper flipper;
    public OptionsLayout(Activity context){
        super();
        this.context = context;
        this.layout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.flutuant_options_layout, null);
        this.flipper = this.layout.findViewById(R.id.floating_options_flipper);
        this.flipper_pages = new ArrayList<>();
    }

    public ViewGroup getLayout(){
        return layout;
    }

    public void setTitle(String title){
        ((TextView) layout.findViewById(R.id.floating_options_top_bar_title)).setText((title == null) ? "" : title);
    }


    public void switchTo(int page_index, boolean back) {
        if (back) {
            flipper.setInAnimation(context, R.anim.move_in_to_right);
            flipper.setOutAnimation(context, R.anim.move_out_to_right);
        } else {
            flipper.setInAnimation(context, R.anim.move_in_to_left);
            flipper.setOutAnimation(context, R.anim.move_out_to_left);
        }
        setTitle(flipper_pages.get(page_index).getTitle());
        flipper.setDisplayedChild(page_index);
    }

    /**
     * Adicione uma nova página limpa no Flipper
     * @return Retorna o index da página, do contrario retorna -1 se houver falha
     */
    public int newPage(String title){
        OptionsPage page = new OptionsPage(context);
        if(flipper_pages.add(page)){
            page.setTitle(title);
            flipper.addView(page.getPageView());
            setTitle(title);
            return flipper_pages.size()-1;
        }
        return -1;
    }

    /**
     * Adicione uma nova página no Flipper
     * @return Retorna o index da página, do contrario retorna -1 se houver falha
     */

    public int addPage(OptionsPage page){
        if(flipper_pages.add(page)){
            flipper.addView(page.getPageView());
            return flipper_pages.size()-1;
        }
        return -1;
    }

    public OptionsPage getPage(int index){
        return flipper_pages.get(index);
    }

    public int getCurrentPageIndex(){ return flipper.getDisplayedChild(); }

    public Button getBackButton(){
        return layout.findViewById(R.id.floating_options_top_bar_button_prev);
    }

    public void addViewToBottomBar(View view){
        int size = (int) context.getResources().getDimension(R.dimen.floating_options_button_size);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        ((ViewGroup) layout.findViewById(R.id.floating_options_bottom_bar)).addView(view, params);
    }

    public void addViewToBottomBar(View view, int index){
        int size = (int) context.getResources().getDimension(R.dimen.floating_options_button_size);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        ((ViewGroup) layout.findViewById(R.id.floating_options_bottom_bar)).addView(view, index, params);
    }

    public void addViewToBottomBar(View view, int index, ViewGroup.LayoutParams params){
        ((ViewGroup) layout.findViewById(R.id.floating_options_bottom_bar)).addView(view, index, params);
    }
    public void addViewToBottomBar(View view, ViewGroup.LayoutParams params){
        ((ViewGroup) layout.findViewById(R.id.floating_options_bottom_bar)).addView(view, params);
    }

    public void addViewToBottomBar(View view, int width, int height){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        ((ViewGroup) layout.findViewById(R.id.floating_options_bottom_bar)).addView(view, width, height);
    }
}
