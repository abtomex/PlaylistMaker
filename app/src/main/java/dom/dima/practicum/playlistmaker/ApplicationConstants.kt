package dom.dima.practicum.playlistmaker

interface ApplicationConstants {

    val DARK_THEME_KEY: String
        get() = "theme_preferences"
    val TRACK_HISTORY: String
        get() = "track_history"
    val CLICKED_TRACK_CONTENT: String
        get() = "clicked_track"
    val MAX_HISTORY_SIZE: Int
        get() = 10

}