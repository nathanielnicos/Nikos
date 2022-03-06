package id.nns.nikos.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val name: String?,
    val email: String?,
    val imgUri: Uri?
) : Parcelable
