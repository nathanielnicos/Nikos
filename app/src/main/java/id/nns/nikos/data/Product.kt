package id.nns.nikos.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val imgUrl: String = "",
    val favorite: Boolean = false,
    val product: String = "",
    val price: Long = 0,
    val timestamp: Long = 0,
    val detail: String = ""
) : Parcelable
