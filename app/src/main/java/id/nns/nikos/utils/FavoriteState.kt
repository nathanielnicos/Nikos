package id.nns.nikos.utils

import id.nns.nikos.R

object FavoriteState {

    fun favoriteState(isFavorite: Boolean) : Int {
        return if (isFavorite) {
            R.drawable.ic_favorite
        } else {
            R.drawable.ic_unfavorite
        }
    }

}
