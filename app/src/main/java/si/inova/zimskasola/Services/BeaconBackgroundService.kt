package si.inova.zimskasola.Services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.R.string.cancel
import android.content.Context
import android.os.Handler
import android.util.Log
import si.inova.zimskasola.fragments.MyLocationFragment
import java.util.*


class BeaconBackgroundService : Service() {

    companion object {
        private const val TAG = "BeaconBackgroundService"
    }


    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
        Log.d(TAG, "Service started.")

        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable { showRandomNumber() }
        mHandler.postDelayed(mRunnable, 5000)

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"Service destroyed.")
        mHandler.removeCallbacks(mRunnable)
    }

    // Custom method to do a task
    private fun showRandomNumber() {
        val rand = Random()
        val number = rand.nextInt(100)
        Log.d(TAG,"Random Number : $number")
        mHandler.postDelayed(mRunnable, 5000)
    }
}
