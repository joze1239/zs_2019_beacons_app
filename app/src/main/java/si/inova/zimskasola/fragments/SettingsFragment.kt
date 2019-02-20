package si.inova.zimskasola.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.settings_fragment.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.adapters.FloorAdapter
import si.inova.zimskasola.util.InternetCheck
import si.inova.zimskasola.viewmodels.MainViewModel


class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        init()
        initUI()
        updateUI()


    }

    private fun init() {
        // Check internet connectiom
        InternetCheck { internet ->
            if(!internet) {
                findNavController().navigate(R.id.action_no_connection)
            }
        }
    }

    private fun initUI() {
        (activity as MainActivity).showLogout()
    }


    private fun updateUI() {
        // Show user profile image
        var imgUrl = "https://lh3.googleusercontent.com" + viewModel.getUserPhotoUrl()?.path
        imgUrl = imgUrl.replace("s96-c", "s200-c")
        Glide.with(context!!)
            .load(imgUrl)
            .circleCrop()
            .into(iv_user_photo)

        // Show user email and name
        tv_user_email.text = viewModel.getUserEmail()
        tv_user_name.text = viewModel.getUserName()


        // Show rooms list
        viewModel.getLocationData().observe(this, Observer { location ->
            run {
                viewManager = LinearLayoutManager(context)
                viewAdapter = FloorAdapter(location.floors, context!!)

                rv_floors.apply {
                    layoutManager = viewManager
                    adapter = viewAdapter
                }

            }

        })


    }


}
