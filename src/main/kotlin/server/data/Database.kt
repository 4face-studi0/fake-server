package server.data

import kotlinx.serialization.Serializable

@Serializable
data class Database(
    var loggedIn: AccountId? = null,
    val users: MutableSet<User> = mutableSetOf(),
    val credentials: MutableSet<Pair<Email, String>> = mutableSetOf(),
    val friends: MutableList<Pair<AccountId, AccountId>> = mutableListOf()
)
