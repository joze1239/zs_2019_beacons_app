package si.inova.zimskasola.fragments

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.my_location_fragment.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.viewmodels.MainViewModel
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.zimskasola.R
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.layout_room_details.view.*
import si.inova.zimskasola.adapters.StuffAdapter
import si.inova.zimskasola.models.Room
import si.inova.zimskasola.util.InternetCheck


class MyLocationFragment : Fragment() {

    companion object {
        fun newInstance() = MyLocationFragment()
        private const val TAG = "MyLocationFragment"
    }

    private lateinit var viewModel: MainViewModel
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


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
        // Check internet connectiom
        InternetCheck { internet ->
            if(!internet) {
                findNavController().navigate(R.id.action_no_connection)
            }
        }

        viewModel.getLocationData().observe(this, Observer { location ->
            run {
                Log.d(TAG, location.toString())
            }
        })

        viewModel.getCurrentRoom().observe(this, Observer { room ->
            run {
                updateUI(room)
            }
        })


    }

    private fun initUI() {
        (activity as MainActivity).hideLogout()
        (activity as MainActivity).hideToolbarActionBack()
        showSearching()
    }

    private fun updateUI(room: Room?) {
        if (room != null)
            showRoomDetails(room)
        else
            showSearching()
    }

    private fun showRoomDetails(room: Room) {


        l_details.tv_room_name.text = room.name
        l_details.tv_floor_name.text = room.floor
        viewManager = LinearLayoutManager(context)
        viewAdapter = StuffAdapter(room.stuff, context!!)

        l_details.rv_stuffs.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        Glide.with(context!!)
            .load(room.image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d(TAG, "onLoadFailed ${e.toString()}")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    hideSearching()
                    return false
                }


            })
            .into(l_details.iv_room_image)


    }

    private fun hideSearching() {
        l_details?.visibility = View.VISIBLE
        l_searching?.visibility = View.INVISIBLE
    }

    private fun showSearching() {
        l_details?.visibility = View.INVISIBLE
        l_searching?.visibility = View.VISIBLE
    }


}
