package si.inova.zimskasola.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.fragment_find_person.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.adapters.FloorAdapter
import si.inova.zimskasola.util.InternetCheck
import si.inova.zimskasola.viewmodels.MainViewModel
import android.content.DialogInterface
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.layout_room_details.view.*
import kotlinx.android.synthetic.main.my_location_fragment.*


class FindPersonFragment : Fragment() {

    companion object {
        fun newInstance() = FindPersonFragment()
        private const val TAG = "FindPersonFragment"
    }

    private lateinit var viewModel: MainViewModel
    var checkedItem = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_person, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        init()
        initUI()


    }

    private fun init() {
        // Check internet connectiom
        InternetCheck { internet ->
            if (!internet) {
                findNavController().navigate(R.id.action_no_connection)
            }
        }

    }

    private fun initUI() {
        (activity as MainActivity).hideLogout()
        (activity as MainActivity).hideToolbarActionBack()
        l_find_result.visibility = View.INVISIBLE
        anim_loading.visibility = View.INVISIBLE


        btn_find.setOnClickListener {

            showSelectPersonDialog()
        }
    }

    private fun showLoading() {
        anim_loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        anim_loading.visibility = View.INVISIBLE
    }



    private fun showSelectPersonDialog() {
        hideLoading()
        l_find_result.visibility = View.INVISIBLE

        val builder = AlertDialog.Builder(context)
        builder.setTitle(resources.getString(R.string.select_person))
        val persons =
            arrayOf("Jason Ruiz", "Lily Burton", "Stanley Smith", "Derek Lambert", "Leah Berry", "Bobby Tucker")

        builder.setSingleChoiceItems(persons, checkedItem, DialogInterface.OnClickListener { dialog, which ->
            // user checked an item
            Log.d(TAG, which.toString())
            checkedItem = which
        })

        builder.setPositiveButton(getString(R.string.option_ok), { dialog, which ->
            Log.d(TAG, which.toString())
            showPersonLocation(persons[checkedItem])
        })
        builder.setNegativeButton(getString(R.string.option_cancel), { dialog, which ->
        })

        val dialog = builder.create()
        dialog.show()
    }

    private fun showPersonLocation(person: String) {
        showLoading()
        Handler().postDelayed({
            var location = viewModel.location.value
            if (location != null) {
                var floor = location.floors.random()
                var room = floor.rooms.random()

                tv_pearson_name.text = person
                tv_room_name.text = room.name
                tv_floor_name.text = floor.name

                Glide.with(context!!)
                    .load(room.image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            hideLoading()
                            l_find_result.visibility = View.VISIBLE
                            return false
                        }


                    })
                    .into(iv_room_image)


            }
        }, 1000)
    }


}
