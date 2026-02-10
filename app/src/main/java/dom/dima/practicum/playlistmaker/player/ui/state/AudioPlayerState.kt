package dom.dima.practicum.playlistmaker.player.ui.state

sealed class AudioPlayerState() {

    class Prepared (val data: StateData) : AudioPlayerState()
    class Completion (val data: StateData) : AudioPlayerState()
    class Playing (val data: StateData, val progress: String) : AudioPlayerState()
    class Pause (val data: StateData, val progress: String) : AudioPlayerState()
    class Favorite() : AudioPlayerState()
    class NotFavorite() : AudioPlayerState()

}