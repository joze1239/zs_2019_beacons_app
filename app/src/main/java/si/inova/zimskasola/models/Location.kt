package si.inova.zimskasola.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Location(
    var address: String? = "",
    var name: String? = ""
)
