package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.example.myapplication.MainActivity

class MyBroadCastReceiver: BroadcastReceiver() {

    //this fucntion will run when some event has occured (when time is up)
    override fun onReceive(context: Context?, intent: Intent?) {
        var i = Intent(context, MainActivity::class.java)

        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(i)
    }
}