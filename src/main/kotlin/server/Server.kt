package server

import kotlinx.serialization.Serializable
import server.data.*

interface Server {

    /**
     * Save the current state of the server, so it can be loaded the next time we
     * launch the application
     * @see load
     */
    fun save()

    /**
     * Load the previous saved state of the server
     * @see save
     */
    fun load()

    /**
     * Create a new account with the requested parameters.
     *
     * @return your personal [AccountId]
     * @throws [IllegalArgumentException] if some field is invalid
     */
    fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
        birthday: Birthday
    ): AccountId

    /**
     * Login with the requested email and password
     *
     * @return your personal [AccountId]
     * @throws [IllegalArgumentException] if some field is invalid or the given
     *  [password] does not match the given [email] address
     */
    fun login(
        email: String,
        password: String
    ): AccountId

    /**
     * Add a Friend to your friends list
     * @param other the [AccountId] of the friend you with to add
     * @throws IllegalArgumentException if there is no User associated with the given [AccountId]
     * @throws IllegalStateException if no user is logged in
     */
    fun addFriend(other: AccountId)

    /**
     * Remove a Friend to your friends list
     * @param other the [AccountId] of the friend you with to remove
     * @throws IllegalArgumentException if there is no User associated with the given [AccountId]
     * @throws IllegalStateException if no user is logged in
     */
    fun removeFriend(other: AccountId)

    /**
     * Get [User] information for the given [AccountId], which it can be
     * your personal [AccountId] or another person
     *
     * @return `null` if no [User] if found for the given [AccountId]
     */
    fun getUser(accountId: AccountId): User?

    /**
     * @return all the Users registered
     */
    fun allUsers(): Set<User>

    /**
     * Load the List of friends for the user with the given [AccountId]
     * @throws IllegalArgumentException if there is no User associated with the given [AccountId]
     * @throws IllegalArgumentException if there is no User associated with the given [AccountId]
     */
    fun getFriendList(): List<User>
}

fun Server(): Server = ServerImpl()
