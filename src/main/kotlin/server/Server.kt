package server

interface Server {

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
     * @param me your personal [AccountId]
     * @param other the [AccountId] of the friend you with to add
     * @throws IllegalArgumentException if there is no User associated with the given [AccountId]
     */
    fun addFriend(me: AccountId, other: AccountId)

    /**
     * Remove a Friend to your friends list
     * @param me your personal [AccountId]
     * @param other the [AccountId] of the friend you with to remove
     * @throws IllegalArgumentException if there is no User associated with the given [AccountId]
     */
    fun removeFriend(me: AccountId, other: AccountId)

    /**
     * Get [User] information for the given [AccountId], which it can be
     * your personal [AccountId] or another person
     *
     * @return `null` if no [User] if found for the given [AccountId]
     */
    fun getUser(accountId: AccountId): User?

    /**
     * Load the List of friends for the user with the given [AccountId]
     * @throws IllegalArgumentException if there is no User associated with the given [AccountId]
     */
    fun getFriendList(accountId: AccountId): List<User>
}

data class AccountId(val number: Int)

data class User(
    val accountId: AccountId,
    val name: String,
    val surname: String,
    val birthday: Birthday
)

data class Birthday(
    val day: Int,
    val month: Int,
    val year: Int
)
