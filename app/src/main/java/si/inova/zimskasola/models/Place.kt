package si.inova.zimskasola.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Place(
    var floor: String? = "",
    var image: String? = "",
    var name: String? = ""
)
