package com.mnnyang.gzuclassschedule.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Config;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.mvp.course.CourseActivity;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;

public class MyWidget extends AppWidgetProvider {

    private ComponentName thisWidget;
    private RemoteViews remoteViews;

    /** AppWidgetProvider 继承自 BroadcastReceiver */
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(this, "onReceive");

        if (intent != null && intent.getAction() != null) {
            LogUtil.e(this, intent.getAction());
            if (intent.getAction().equals("com.mnnyang.action.UPDATE_WIDGET")) {
                updateAction(context);
            }
        }

        super.onReceive(context, intent);
    }

    /**
     * 根据 updatePeriodMillis 定义的定期刷新操作会调用该函数，此外当用户添加 Widget 时
     * 也会调用该函数，可以在这里进行必要的初始化操作。但如果在<appwidget-provider>
     * 中声明了 android:configure 的 Activity，在用户添加 Widget 时，不会调用 onUpdate()，
     * 需要由 configure Activity 去负责去调用
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        LogUtil.d(this, "onUpdate" + appWidgetIds[0]);

        updateAction(context);
    }

    private void updateAction(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetId = appWidgetManager.getAppWidgetIds(new ComponentName(context, MyWidget.class));

        thisWidget = new ComponentName(context, MyWidget.class);
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_all);

        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        //配置适配器
        remoteViews.setRemoteAdapter(R.id.widget_list, intent);

        Intent intent1 = new Intent(context, CourseActivity.class);
        PendingIntent pendingIntentTemplate = PendingIntent.getActivity(
                context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        ////拼接PendingIntent
        remoteViews.setPendingIntentTemplate(R.id.widget_list, pendingIntentTemplate);

        //更新remoteViews
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    /** onDeleted()：当 Widget 被删除时调用该方法。 */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        LogUtil.d(this, "onDeleted");

    }


    /**
     * 当 Widget 第一次被添加时调用，例如用户添加了两个你的 Widget，
     * 那么只有在添加第一个 Widget 时该方法会被调用。
     * 所以该方法比较适合执行你所有 Widgets 只需进行一次的操作。
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        LogUtil.d(this, "onEnabled");

    }

    /**
     * 与 onEnabled 恰好相反，当你的最后一个 Widget 被删除时调用该方法，
     * 所以这里用来清理之前在 onEnabled() 中进行的操作。
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        LogUtil.d(this, "onDisabled");

    }

    /**
     * 当 Widget 第一次被添加或者大小发生变化时调用该方法，
     * 可以在此控制 Widget 元素的显示和隐藏。
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        LogUtil.d(this, "onAppWidgetOptionsChanged");

    }
}

/**
 * 测试结果：
 * 第一次添加：
 * onEnabled    onUpdate
 * 第二次以后添加：
 * onUpdate
 * <p>
 * <p>
 * 当前有多个删除第一个：
 * onDeleted
 * 删除最后一个：
 * onDeleted   onDisabled
 */
