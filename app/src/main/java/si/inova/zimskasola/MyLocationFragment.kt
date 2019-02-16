package si.inova.zimskasola

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import kotlinx.android.synthetic.main.my_location_fragment.*


class MyLocationFragment : Fragment() {

    companion object {
        fun newInstance() = MyLocationFragment()
    }

    private lateinit var viewModel: MyLocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.zimskasola.R.layout.my_location_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyLocationViewModel::class.java)
        // TODO: Use the ViewModel

        val handler = Handler()
        handler.postDelayed( {
            showLocationDetails()
        }, 2500)
    }

    private fun showLocationDetails() {
        l_details?.visibility = View.VISIBLE
        l_searching?.visibility = View.INVISIBLE


    }


}
