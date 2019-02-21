package si.inova.zimskasola.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.zimskasola.R
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.adapters.StuffAdapter
import si.inova.zimskasola.models.Room
import si.inova.zimskasola.viewmodels.MainViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.fragment_room_details.*
import kotlinx.android.synthetic.main.layout_room_details.view.*


class RoomDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = RoomDetailsFragment()
        private const val TAG = "RoomDetailsFragment"
    }


    private lateinit var viewModel: MainViewModel

    private lateinit var room: Room
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val SHIMMER_DURATION = 800L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_room_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        init()
        initUI()
    }

    private fun init() {
        room = viewModel.selectedRoom;
        Log.d(TAG, room.toString())

    }


    private fun initUI() {
        startShimmerEffect()

        Handler().postDelayed({
            stopShimmerEffect()
        }, SHIMMER_DURATION)

        (activity as MainActivity).showToolbarActionBack()

        l_details.tv_locationg_at.visibility = View.INVISIBLE
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
                    // hideSearching()
                    return false
                }


            })
            .into(l_details.iv_room_image)
    }


    private fun startShimmerEffect() {
        shimmer_view_container.startShimmerAnimation()
        l_details.visibility = View.INVISIBLE
    }

    private fun stopShimmerEffect() {
        shimmer_view_container.stopShimmerAnimation()
        l_details.visibility = View.VISIBLE
        shimmer_view_container.visibility = View.GONE
    }


}
