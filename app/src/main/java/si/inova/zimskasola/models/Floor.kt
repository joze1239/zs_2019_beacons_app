package si.inova.zimskasola.models

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

data class Floor (

    @SerializedName("floor_id") val floor_id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("rooms") val rooms : List<Room>
)