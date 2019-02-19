package si.inova.zimskasola.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
import androidx.annotation.NonNull



class MainActivity : AppCompatActivity(), BeaconScanner.Listener {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: MainViewModel
    private var scanner = BeaconScanner(this, this)
    private lateinit var permissionHelper: PermissionHelper

    companion object {
        private const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.init()

        initUI()
        checkPermissions()
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
        val navController = host.navController
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
    }

    private fun signOut() {
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
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }


}


