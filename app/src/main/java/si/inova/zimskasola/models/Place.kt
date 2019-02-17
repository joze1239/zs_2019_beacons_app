package si.inova.zimskasola.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Place(
    var address: String? = "",
    var name: String? = ""
)
