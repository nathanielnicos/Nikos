package id.nns.nikos.data

data class Location(
    val id: String? = "",
    val lat: Double? = 0.0,
    val long: Double? = 0.0,
    val address: String? = "",
    val timestamp: Long? = 0
)
