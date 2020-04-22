package com.example.taskapp.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class BootReceiver : BroadcastReceiver() {


    //todo inject
    private val  workersCreator: WorkersInitializer = UpdateRemindersWorkerInitializer()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED" && context != null) {
            workersCreator.setUpWorkers(context)
        }
    }

}



