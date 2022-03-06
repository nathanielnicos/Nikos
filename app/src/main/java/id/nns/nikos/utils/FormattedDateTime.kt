package id.nns.nikos.utils

import java.text.SimpleDateFormat
import java.util.*

object FormattedDateTime {

    fun convertDate(timestamp: Long) : String {
        return getSimpleDateFormat("dd MMMM yyyy").format(timestamp * 1000)
    }

    fun convertTime(timestamp: Long) : String {
        return getSimpleDateFormat("HH:mm:ss").format(timestamp * 1000)
    }

    private fun getSimpleDateFormat(pattern: String) : SimpleDateFormat {
        return SimpleDateFormat(pattern, Locale.getDefault())
    }

}
