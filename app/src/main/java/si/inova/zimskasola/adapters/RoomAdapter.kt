package si.inova.zimskasola.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.row_floor.view.*
import kotlinx.android.synthetic.main.row_room.view.*
import si.inova.zimskasola.fragments.AllLocationsFragment
import si.inova.zimskasola.fragments.RoomDetailsFragment
import si.inova.zimskasola.models.Room
import si.inova.zimskasola.viewmodels.MainViewModel

class RoomAdapter(private val myDataset: List<Room>, val context: Context, val viewModel: MainViewModel, val floorName: String) :
    RecyclerView.Adapter<RoomAdapter.MyViewHolder>() {

    class MyViewHolder(val root_layout: LinearLayout) : RecyclerView.ViewHolder(root_layout)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RoomAdapter.MyViewHolder {
        // create a new view
        val rootLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_room, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(rootLayout)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RoomAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.root_layout.tv_room_name.text = myDataset[position].name

        holder.root_layout.setOnClickListener {
            myDataset[position].floor = floorName
            viewModel.updateSelectedRoom(myDataset[position])
            findNavController(it).navigate(R.id.action_room_details)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}

