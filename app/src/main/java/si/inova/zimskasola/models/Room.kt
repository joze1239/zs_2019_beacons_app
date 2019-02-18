package si.inova.zimskasola.models

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("room_id") val room_id: Int,
    @SerializedName("beacon_id") val beacon_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("stuff") val stuff: List<Stuff>
)