package si.inova.zimskasola.models

import com.google.gson.annotations.SerializedName

data class Stuff(
    @SerializedName("stuff_id") val stuff_id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)