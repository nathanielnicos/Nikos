package id.nns.nikos.utils

import java.text.DecimalFormat

object FormattedPrice {

    fun getPrice(price: Long) : String {
        val format = DecimalFormat("#,###,###")
        return "Rp${format.format(price).replace(",".toRegex(), ".")},00"
    }

}
