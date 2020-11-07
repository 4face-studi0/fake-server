package server

import server.data.AccountId
import server.data.Birthday

interface Auth  {

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
}

fun Auth(): Auth = AuthImpl()
