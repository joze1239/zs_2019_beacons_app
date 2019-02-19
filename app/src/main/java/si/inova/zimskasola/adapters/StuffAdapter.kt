package si.inova.zimskasola.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.row_stuff.view.*
import kotlinx.android.synthetic.main.settings_fragment.*
import si.inova.zimskasola.models.Stuff

class StuffAdapter(private val myDataset: List<Stuff>, val context: Context) :
    RecyclerView.Adapter<StuffAdapter.MyViewHolder>() {

    class MyViewHolder(val root_layout: LinearLayout) : RecyclerView.ViewHolder(root_layout)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): StuffAdapter.MyViewHolder {
        // create a new view
        val rootLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_stuff, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(rootLayout)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: StuffAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.root_layout.tv_room_name.text = myDataset[position].name
        holder.root_layout.tv_category.text = myDataset[position].category
        holder.root_layout.tv_name.text = myDataset[position].name
        holder.root_layout.tv_description.text = myDataset[position].description

        Glide.with(context)
            .load(myDataset[position].icon)
            .into(holder.root_layout.iv_icon)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}

