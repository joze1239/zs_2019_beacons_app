package si.inova.zimskasola.models

import com.google.gson.annotations.SerializedName

data class Location (

    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("floors") val floors : List<Floor>
)