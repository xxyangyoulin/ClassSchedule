package com.mnnyang.gzuclassschedule.utils.spec;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.app;
import com.mnnyang.gzuclassschedule.utils.ColorUtil;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;

/**
 * Created by mnnyang on 17-11-4.
 */

public class ShowTermDialog {

    public interface TimeTermCallback {
        void onTimeChanged(String time);

        void onTermChanged(String term);

        void onPositive(DialogInterface dialog, int which);
    }

    public void showSelectTimeTermDialog(Context context, String[] times, final TimeTermCallback callback) {
        if (times.length == 0) {
            return;
        }

        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.layout_course_time_dialog, null);
        final RadioGroup rg = dialogView.findViewById(R.id.rg_course_time);

        int i = 1;
        for (String time : times) {
            AppCompatRadioButton tempButton = new AppCompatRadioButton(context);
            tempButton.setTextColor(ColorUtil.getColor(context, R.attr.colorPrimary));
            tempButton.setText(time);
            tempButton.setId(i);
            rg.addView(tempButton, LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            i++;
        }

        rg.invalidate();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AppCompatRadioButton arb = group.findViewById(checkedId);
                System.out.println("6:" + arb);
                callback.onTimeChanged(arb.getText().toString());
            }
        });

        RadioGroup termRg = dialogView.findViewById(R.id.rg_term);
        termRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AppCompatRadioButton arb = group.findViewById(checkedId);
                callback.onTermChanged(arb.getTag().toString());
            }
        });

        AppCompatRadioButton at = (AppCompatRadioButton) rg.getChildAt(0);
        if (at != null) at.setChecked(true);
        at = (AppCompatRadioButton) termRg.getChildAt(0);
        if (at != null) at.setChecked(true);


        DialogHelper helper = new DialogHelper();
        helper.showCustomDialog(context, dialogView,
                "", new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        callback.onPositive(dialog, which);
                    }
                });
    }
}
