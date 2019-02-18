package si.inova.zimskasola.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.settings_fragment.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.viewmodels.MainViewModel
import si.inova.zimskasola.viewmodels.SettingsViewModel
import java.io.File


class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: MainViewModel

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


        updateUI()


        (activity as MainActivity).showLogout()

    }

    private fun updateUI() {
        // Show user email and name
        tv_email.text =
            viewModel.getUserEmail() + "\n" + viewModel.getUserName() + "\nPath = " + viewModel.getUserPhotoUrl()?.getPath()

        // Show user profile image
        var imgUrl = "https://lh3.googleusercontent.com" + viewModel.getUserPhotoUrl()?.path
        imgUrl = imgUrl.replace("s96-c", "s400-c")
        Glide.with(context!!)
            .load(imgUrl)
            .circleCrop()
            .into(iv_userPhoto)


    }


}
