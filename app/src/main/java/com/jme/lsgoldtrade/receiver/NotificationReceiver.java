package com.jme.lsgoldtrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.jme.lsgoldtrade.ui.main.MainActivity;
import com.jme.lsgoldtrade.ui.market.MarketDetailActivity;
import com.jme.lsgoldtrade.ui.news.NewsDetailActivity;
import com.jme.lsgoldtrade.ui.transaction.ConditionSheetActivity;
import com.jme.lsgoldtrade.util.SystemUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/07/29 17:29
 * Desc   : description
 */
public class NotificationReceiver extends BroadcastReceiver {

    public static final String TAG = NotificationReceiver.class.getSimpleName();
    private List<Intent> intentList;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id", "");
        String contractId = bundle.getString("contractId", "");
        String sheet = bundle.getString("sheet", "");

        Intent destinationIntent;

        if (!TextUtils.isEmpty(id)) {
            destinationIntent = new Intent(context, NewsDetailActivity.class);
            destinationIntent.putExtra("ID", Long.parseLong(id));

            if (SystemUtils.isAppAlive(context, context.getPackageName())) {
                Log.i(TAG, "the app process is alive");
                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (intentList == null)
                    intentList = new ArrayList<>();

                intentList.add(mainIntent);

                if (destinationIntent != null)
                    intentList.add(destinationIntent);

                context.startActivities(intentList.toArray(new Intent[intentList.size()]));
            } else {
                Log.i(TAG, "the app process is dead");

                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                launchIntent.putExtras(bundle);

                context.startActivity(launchIntent);
            }
        } else if (!TextUtils.isEmpty(contractId)) {
            destinationIntent = new Intent(context, MarketDetailActivity.class);
            destinationIntent.putExtra("ContractId", contractId);

            if (SystemUtils.isAppAlive(context, context.getPackageName())) {
                Log.i(TAG, "the app process is alive");
                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (intentList == null)
                    intentList = new ArrayList<>();

                intentList.add(mainIntent);

                if (destinationIntent != null)
                    intentList.add(destinationIntent);

                context.startActivities(intentList.toArray(new Intent[intentList.size()]));
            } else {
                Log.i(TAG, "the app process is dead");

                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                launchIntent.putExtras(bundle);

                context.startActivity(launchIntent);
            }
        } else if (!TextUtils.isEmpty(sheet)) {
            destinationIntent = new Intent(context, ConditionSheetActivity.class);
            destinationIntent.putExtra("Type", sheet.contains("条件单") ? 1 : 2);

            if (SystemUtils.isAppAlive(context, context.getPackageName())) {
                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (intentList == null)
                    intentList = new ArrayList<>();

                intentList.add(mainIntent);

                if (destinationIntent != null)
                    intentList.add(destinationIntent);

                context.startActivities(intentList.toArray(new Intent[intentList.size()]));
            } else {
                Log.i(TAG, "the app process is dead");

                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                launchIntent.putExtras(bundle);

                context.startActivity(launchIntent);
            }
        }

    }

}
