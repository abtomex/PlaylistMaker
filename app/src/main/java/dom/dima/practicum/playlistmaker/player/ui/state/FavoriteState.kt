package dom.dima.practicum.playlistmaker.player.ui.state

sealed class FavoriteState {

    class AddToFavorite : FavoriteState()
    class RemoveFromFavorite : FavoriteState()

}