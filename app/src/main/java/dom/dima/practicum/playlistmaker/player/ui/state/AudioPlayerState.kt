package dom.dima.practicum.playlistmaker.player.ui.state

sealed class AudioPlayerState(val stateData: StateData) {

    data class Prepared (val data: StateData) : AudioPlayerState(data)
    data class Completion (val data: StateData) : AudioPlayerState(data)
    data class Start (val data: StateData) : AudioPlayerState(data)
    data class Pause (val data: StateData) : AudioPlayerState(data)

}