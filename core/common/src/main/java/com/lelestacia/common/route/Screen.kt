package com.lelestacia.common.route

sealed class Screen(val route: String) {
    object Explore : Screen("explore")
    object Collection : Screen("collection")
    object More : Screen("more")
    object DetailAnimeScreen : Screen("anime/{mal_id}") {
        fun createRoute(animeID: Int): String {
            return this.route.replace(
                oldValue = "{mal_id}",
                newValue = animeID.toString()
            )
        }
    }
}
