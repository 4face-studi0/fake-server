package server.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Database(
    var loggedIn: AccountId? = null,
    val users: MutableSet<User> = mutableSetOf(),
    val credentials: MutableSet<Pair<Email, String>> = mutableSetOf(),
    val carts: MutableSet<Pair<AccountId, Cart>> = mutableSetOf(),
    val friends: MutableList<Pair<AccountId, AccountId>> = mutableListOf()
) {

    fun save() {
        ServerFile.writeText(Json.encodeToString(this))
    }

    fun assertLoggedIn(): AccountId =
        checkNotNull(loggedIn) { "You're not logged in" }

    companion object {
        private val ServerFile = File("database")

        fun load(): Database =
            if (ServerFile.exists().not()) Database()
            else Json.decodeFromString(ServerFile.readText())

    }
}
