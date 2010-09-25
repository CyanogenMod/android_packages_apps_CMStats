package com.cyanogenmod.stats;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReportingServiceManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.d("CMStats", "Got ACTION_BOOT_COMPLETED.");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ComponentName cmp = new ComponentName(ctx.getPackageName(), ReportingService.class.getName());
            ctx.startService(new Intent().setComponent(cmp));
            Log.d("CMStats", "Service starting from ReportingServiceManager");
        }
    }

}
