package com.mnnyang.gzuclassschedule.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.course.CourseActivity;

/**
 * Created by xxyangyoulin on 2018/4/4.
 */

public class OneWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, CourseActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_one);

            views.setOnClickPendingIntent(R.id.iv, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
