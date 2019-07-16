package si.inova.zimskasola.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.util.Log
import si.inova.zimskasola.util.BeaconScanner
import java.util.*


class BeaconBackgroundService : Service(), BeaconScanner.Listener {

    companion object {
        private const val TAG = "BeaconBackgroundService"
        private const val DELAY = 5000L
    }


    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    private lateinit var scanner: BeaconScanner

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
        Log.d(TAG, "Service started.")

        // Do a periodic task
        /*mHandler = Handler()
        mRunnable = Runnable { showRandomNumber() }
        mHandler.postDelayed(mRunnable, DELAY)
        */
        scanner = BeaconScanner(this, this)
        scanner.start()

        return Service.START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed.")
        scanner.stop()
        mHandler.removeCallbacks(mRunnable)

    }

    override fun onBeaconFound(data: String) {
        //viewModel.updateCurrentRoom(data)
        Log.d(TAG, "onBeaconFound ${data}")
    }

    override fun onBeaconLost(data: String) {
        //viewModel.updateCurrentRoomLeaved()
        Log.d(TAG, "onBeaconLost ${data}")

    }


    // Custom method to do a task
    private fun showRandomNumber() {
        val rand = Random()
        val number = rand.nextInt(100)
        Log.d(TAG, "Random Number : $number")
        mHandler.postDelayed(mRunnable, DELAY)
    }
}
