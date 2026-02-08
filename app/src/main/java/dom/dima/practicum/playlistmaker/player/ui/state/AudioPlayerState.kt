package dom.dima.practicum.playlistmaker.player.ui.state

sealed class AudioPlayerState(val stateData: StateData, val progress: String) {

    class Prepared (val data: StateData) : AudioPlayerState(data, "00:00")
    class Completion (val data: StateData) : AudioPlayerState(data, "00:00")
    class Playing (val data: StateData, progress: String) : AudioPlayerState(data, progress)
    class Pause (val data: StateData, progress: String) : AudioPlayerState(data, progress)

}