package dom.dima.practicum.playlistmaker.media.domain.state

sealed class AddToDbState<T>(t: T) {
    class Added<T>(t: T): AddToDbState<T>(t)
    class Removed<T>(t: T): AddToDbState<T>(t)
}