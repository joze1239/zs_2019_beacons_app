package si.inova.zimskasola.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.all_locations_fragment.*
import kotlinx.android.synthetic.main.row_floor.view.*
import si.inova.zimskasola.models.Floor
import si.inova.zimskasola.models.Room

class SettingsFloorAdapter(private val myDataset: List<Floor>, val context: Context, val currentRoom: Room?) :
    RecyclerView.Adapter<SettingsFloorAdapter.MyViewHolder>() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    class MyViewHolder(val root_layout: LinearLayout) : RecyclerView.ViewHolder(root_layout)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): SettingsFloorAdapter.MyViewHolder {
        // create a new view
        val rootLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_floor_settings, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters


        return MyViewHolder(rootLayout)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: SettingsFloorAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.root_layout.tv_floor_name.text = myDataset[position].name

        viewManager = LinearLayoutManager(context)
        viewAdapter = SettingsRoomAdapter(myDataset[position].rooms, context, currentRoom)

        holder.root_layout.rv_rooms.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}

