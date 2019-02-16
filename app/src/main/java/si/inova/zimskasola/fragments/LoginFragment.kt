package si.inova.zimskasola.fragments

import android.animation.Animator
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
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
import kotlinx.android.synthetic.main.login_fragment.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.viewmodels.LoginViewModel

class LoginFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel


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
        // TODO: Use the ViewModel
    }

    private fun init() {
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
        btn_login.startLoading()
        btn_login.postDelayed(Runnable {

            if (true) {
                //login success
                btn_login.loadingSuccessful()
                btn_login.animationEndAction = fun(animationType: AnimationType): Unit {
                    toNextPage()
                    return Unit
                }
            } else {
                btn_login.loadingFailed()
                Toast.makeText(
                    context,
                    "login failed,please check username and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, 1500)




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


}
