package si.inova.zimskasola.activities

import android.Manifest
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.zimskasola.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.master.permissionhelper.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import si.inova.zimskasola.util.BeaconScanner
import si.inova.zimskasola.viewmodels.MainViewModel
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.ui.NavigationUI
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import si.inova.zimskasola.services.BeaconBackgroundService
import java.util.*

class MainActivity : AppCompatActivity(), BeaconScanner.Listener {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: MainViewModel
    private var scanner = BeaconScanner(this, this)
    private lateinit var permissionHelper: PermissionHelper

    private lateinit var serviceClass: Class<BeaconBackgroundService>
    private lateinit var intentService: Intent

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navController: NavController


    companion object {
        private const val TAG = "MainActivity"
        private const val CHANNEL_ID = "1239"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(true)
            .build()
        Fabric.with(fabric)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.init()

        init()
        initUI()
        checkPermissions()

        createNotificationChannel()

    }


    private fun init() {
        // Variable to hold service class name
        serviceClass = BeaconBackgroundService::class.java
        // Initialize a new Intent instance
        intentService = Intent(applicationContext, serviceClass)

        startBeaconService()


    }


    fun checkPermissions() {
        permissionHelper = PermissionHelper(
            this,
            arrayOf(
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            100
        )
        permissionHelper?.denied {
            if (it) {
                Log.d(TAG, "Permission denied by system")
                permissionHelper?.openAppDetailsActivity()
            } else {
                Log.d(TAG, "Permission denied")
            }
        }

        //Request all permission
        permissionHelper?.requestAll {
            Log.d(TAG, "All permission granted")
        }

        //Request individual permission
        permissionHelper?.requestIndividual {
            Log.d(TAG, "Individual Permission Granted")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        scanner.start()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
        scanner.stop()
    }

    override fun onBeaconFound(data: String) {
        viewModel.updateCurrentRoom(data)
        var currentRoom = viewModel.currentRoom.value
        if (currentRoom != null)
            showNotification(currentRoom.name, getString(R.string.new_room_notication))
    }

    override fun onBeaconLost(data: String) {
        var currentRoom = viewModel.currentRoom.value
        if (currentRoom != null)
            showNotification(currentRoom.name, getString(R.string.exit_room_notification))

        viewModel.updateCurrentRoomLeaved()
    }


    private fun initUI() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        navController = host.navController
        setupBottomNavMenu(navController)

        setupBackButtonAction()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Init onSignOut
        toolbar_logout.setOnClickListener {
            signOut()
        }

        // on toolbar address click -> open maps
        toolbar_subtitle.setOnClickListener {
            openMaps()
        }

    }


    private fun openMaps() {
        var lat = 46.6011867
        var lon = 15.6745545
        var label = "INOVA IT"

        val gmmIntentUri =
            Uri.parse("geo:<$lat>,<$lon>?q=<$lat>,<$lon>($label)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun signOut() {
        // Stop beacon background service
        stopBeaconService()

        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }
    }


    fun showLogout() {
        toolbar_logout.visibility = View.VISIBLE
    }

    fun hideLogout() {
        toolbar_logout.visibility = View.INVISIBLE
    }


    private fun setupBottomNavMenu(navController: NavController) {
        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    private fun setupBackButtonAction() {
        hideToolbarActionBack()
        toolbar_btn_back.setOnClickListener {
            navController.popBackStack()
        }
    }

    fun showToolbarActionBack() {
        toolbar_btn_back.visibility = View.VISIBLE
    }

    fun hideToolbarActionBack() {
        toolbar_btn_back.visibility = View.INVISIBLE
    }


    private fun startBeaconService() {
        // If the service is not running then start it
        if (!isServiceRunning(serviceClass)) {
            // Start the service
            startService(intentService)
            Log.d(TAG, "startBeaconService(): Service started")
        } else {
            Log.d(TAG, "startBeaconService(): Service already runing")
        }


    }

    private fun stopBeaconService() {
        if (isServiceRunning(serviceClass)) {
            // Stop the service
            stopService(intentService)
        }
    }

    // Custom method to determine whether a service is running
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        @Suppress("DEPRECATION")
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, content: String) {
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.notification_icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            var notification_id = Random().nextInt()
            notify(notification_id, builder.build())
        }
    }


}


