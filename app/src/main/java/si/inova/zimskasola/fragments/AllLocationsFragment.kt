package si.inova.zimskasola.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.all_locations_fragment.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.adapters.FloorAdapter
import si.inova.zimskasola.util.InternetCheck
import si.inova.zimskasola.viewmodels.MainViewModel


class AllLocationsFragment : Fragment() {

    companion object {
        fun newInstance() = AllLocationsFragment()
        private const val TAG = "AllLocationsFragment"
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    // Initializing an empty ArrayList to be filled with myDataset
    // val myDataset: ArrayList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_locations_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        init()
        initUI()


    }

    private fun initUI() {
        (activity as MainActivity).hideLogout()
        (activity as MainActivity).hideToolbarActionBack()


    }

    private fun init() {
        // Check internet connectiom
        InternetCheck { internet ->
            if (!internet) {
                try {
                    findNavController().navigate(R.id.action_no_connection)
                } catch (e: java.lang.Exception) {
                    Log.d(TAG, e.toString())
                }

            }
        }

        viewModel.getLocationData().observe(this, Observer { location ->
            run {
                viewManager = LinearLayoutManager(context)
                viewAdapter = FloorAdapter(location.floors, context!!, viewModel)

                rv_floors.apply {
                    layoutManager = viewManager
                    adapter = viewAdapter

                }
            }
        })


    }


}
