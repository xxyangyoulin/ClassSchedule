package com.mnnyang.gzuclassschedule.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 对话框工具
 * Created by mnnyang on 17-4-14.
 */

public class DialogHelper {

    private ProgressDialog progressDialog;
    /**
     * 不确定不可关闭等待对话框<br>
     */
    public void showProgressDialog(Context context, String title, String msg, boolean canceable) {
        progressDialog = ProgressDialog.show(context, title, msg, true, canceable);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 一般对话框
     */
    public void showNormalDialog(@NonNull AppCompatActivity activity, @NonNull String title,
                                 @NonNull String massage, @NonNull final DialogListener listener) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(massage)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onPositive(dialog, which);
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onNegative(dialog, which);
                            }
                        })
                .show();
    }

    /**
     * List对话框
     */
    public void showListDialog(@NonNull Activity activity, @NonNull String title,
                               @NonNull String[] items, @NonNull final DialogListener listener) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onItemClick(dialog, which);
                    }
                })
                .show();
    }

}