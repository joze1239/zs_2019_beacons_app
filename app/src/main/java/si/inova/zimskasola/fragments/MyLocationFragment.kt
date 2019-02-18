package si.inova.zimskasola.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.my_location_fragment.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.viewmodels.MainViewModel
import androidx.lifecycle.Observer
import com.google.firebase.database.DatabaseReference


class MyLocationFragment : Fragment() {

    companion object {
        fun newInstance() = MyLocationFragment()
        private const val TAG = "MyLocationFragment"
    }

    private lateinit var viewModel: MainViewModel
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.zimskasola.R.layout.my_location_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        initUI()
        init()



    }

    private fun init() {
        viewModel.getLocationData().observe(this, Observer { location ->
            run {
                Log.d(TAG, location.toString())
            }
        })


    }

    private fun initUI() {
        (activity as MainActivity).hideLogout()

        val handler = Handler()
        handler.postDelayed({
            showLocationDetails()
        }, 2500)
    }

    private fun showLocationDetails() {
        l_details?.visibility = View.VISIBLE
        l_searching?.visibility = View.INVISIBLE


    }


}
