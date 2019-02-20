package si.inova.zimskasola.activities

import android.Manifest
import android.app.ActivityManager
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
import si.inova.zimskasola.Services.BeaconBackgroundService
import si.inova.zimskasola.util.InternetCheck

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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.init()

        init()
        initUI()
        checkPermissions()
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
    }

    override fun onBeaconLost(data: String) {
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

    private fun startBeaconService() {
        // If the service is not running then start it
        if (!isServiceRunning(serviceClass)) {
            // Start the service
            startService(intent)
            Log.d(TAG, "startBeaconService(): Service started")
        } else {
            Log.d(TAG, "startBeaconService(): Service already runing")
        }


    }

    private fun stopBeaconService() {
        if (isServiceRunning(serviceClass)) {
            // Stop the service
            stopService(intent)
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


}


