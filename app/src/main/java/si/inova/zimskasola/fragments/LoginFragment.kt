package si.inova.zimskasola.fragments

import android.animation.Animator
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import com.dx.dxloadingbutton.lib.AnimationType
import com.example.zimskasola.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.login_fragment.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment(), View.OnClickListener {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: LoginViewModel

    companion object {
        fun newInstance() = LoginFragment()
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.login_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        init()
        setOnClicks()
        initGoogleSignIn()


    }


    private fun initGoogleSignIn() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]
    }

    private fun init() {
        error_msg.visibility = View.INVISIBLE
        btn_g_logo.visibility = View.VISIBLE
        btn_login.resetAfterFailed = true
        btn_login.animationEndAction = fun(animationType: AnimationType): Unit {
            if (animationType == AnimationType.SUCCESSFUL) {
                startActivity(Intent(this.activity, MainActivity::class.java))
            }
            return Unit
        }
    }

    private fun setOnClicks() {
        btn_login!!.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                Log.d("My App", "login clicked")
                checkLogin()
                //lb.startLoading()
                //lb.setEnabled(false)
            }

        }
    }


    private fun checkLogin() {
        btn_g_logo.visibility = View.INVISIBLE
        error_msg.visibility = View.INVISIBLE
        btn_login.startLoading()
        signIn()
    }

    private fun showLoginStatus(success: Boolean) {
        btn_login.postDelayed({

            if (success) {
                //login success
                btn_login.loadingSuccessful()
                btn_login.animationEndAction = fun(animationType: AnimationType): Unit {

                    Handler().postDelayed(
                        {
                            toNextPage()
                        },
                        400
                    )
                }
            } else {
                btn_login.loadingFailed()

                showGoogleLogo()

            }
        }, 1000)
    }


    private fun showGoogleLogo() {
        Handler().postDelayed(
            {
                btn_g_logo.visibility = View.VISIBLE
                error_msg.visibility = View.VISIBLE
            },
            2000
        )
    }


    //add a demo activity transition animation,this is a demo implement
    private fun toNextPage() {

        val cx = (btn_login.getLeft() + btn_login.getRight()) / 2
        val cy = (btn_login.getTop() + btn_login.getBottom()) / 2

        val animator = ViewAnimationUtils.createCircularReveal(
            animate_view,
            cx,
            cy,
            0f,
            resources.displayMetrics.heightPixels * 1.2f
        )
        animator.duration = 500
        animator.interpolator = AccelerateInterpolator()
        animate_view.visibility = View.VISIBLE
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                btn_login.reset()
                activity?.finish()
                // animate_view.visibility = View.INVISIBLE

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "requestCode: " + requestCode + "; " + resultCode + "; " + data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                showLoginStatus(false)
                // ...
            }
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        //showProgressDialog()
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this.requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                    showLoginStatus(false)
                }

                // [START_EXCLUDE]
                //hideProgressDialog()
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]

    private fun updateUI(user: FirebaseUser?) {
        // hideProgressDialog()
        if (user != null) {
            //status.text = getString(R.string.google_status_fmt, user.email)
            //detail.text = getString(R.string.firebase_status_fmt, user.uid)

            //signInButton.visibility = View.GONE
            //signOutAndDisconnect.visibility = View.VISIBLE
            Log.d(TAG, "login success")
            showLoginStatus(true)
        } else {
            Log.d(TAG, "login error")
            showLoginStatus(false)
            //status.setText(R.string.signed_out)
            //detail.text = null

            //signInButton.visibility = View.VISIBLE
            //signOutAndDisconnect.visibility = View.GONE
        }
    }


}
