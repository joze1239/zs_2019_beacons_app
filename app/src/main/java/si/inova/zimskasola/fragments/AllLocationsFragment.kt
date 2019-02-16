package si.inova.zimskasola.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zimskasola.R
import si.inova.zimskasola.viewmodels.AllLocationsViewModel


class AllLocationsFragment : Fragment() {

    companion object {
        fun newInstance() = AllLocationsFragment()
    }

    private lateinit var viewModel: AllLocationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_locations_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AllLocationsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
