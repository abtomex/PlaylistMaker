package dom.dima.practicum.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp(shareAppLink: String)
    fun openTerms(termsLink: String)
    fun openSupport(email: Array<String>, emailSubject: String, emailText: String)
}