package si.inova.zimskasola.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.row_room_settings.view.*
import kotlinx.android.synthetic.main.settings_fragment.*
import si.inova.zimskasola.models.Room
import org.eazegraph.lib.models.ValueLinePoint
import org.eazegraph.lib.models.ValueLineSeries
import org.eazegraph.lib.charts.ValueLineChart
import kotlin.random.Random

class SettingsRoomAdapter(private val myDataset: List<Room>, val context: Context, val currentRoom: Room?) :
    RecyclerView.Adapter<SettingsRoomAdapter.MyViewHolder>() {

    class MyViewHolder(val root_layout: LinearLayout) : RecyclerView.ViewHolder(root_layout)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingsRoomAdapter.MyViewHolder {
        // create a new view
        val rootLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_room_settings, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(rootLayout)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: SettingsRoomAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.root_layout.tv_room_name.text = myDataset[position].name
        holder.root_layout.chart_activity.setBackgroundColor(Color.WHITE)


        var activityImage = ContextCompat.getDrawable(context, R.mipmap.location_inactive)
        if(currentRoom != null && myDataset[position].beacon_id.equals(currentRoom.beacon_id)) {
            activityImage = ContextCompat.getDrawable(context, R.mipmap.location_active)

        }

        Glide.with(context!!)
            .load(activityImage)
            .into(holder.root_layout.iv_activity)


        initChart(holder.root_layout.chart_activity)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size




    override fun onViewRecycled(holder: MyViewHolder) {
        super.onViewRecycled(holder)
        Log.d("aa", "load")
    }

    private fun initChart(linechart: ValueLineChart) {
        var arrDays = arrayOf("ned","pon", "tor", "sre", "čet", "pet", "sob")
        val series = ValueLineSeries()
        series.color = ContextCompat.getColor(context, R.color.chart_background)

        val min = 4
        val max = 20
        for (day in arrDays) {
            val value = min + Random.nextFloat() * (max - min)
            series.addPoint(ValueLinePoint(day, value))
        }

        linechart.addSeries(series)
        linechart.startAnimation()
    }
}

