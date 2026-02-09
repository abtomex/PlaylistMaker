package dom.dima.practicum.playlistmaker.player.ui.state

sealed class FavoriteState {

    class IsFavorite : FavoriteState()
    class NotFavorite : FavoriteState()

}