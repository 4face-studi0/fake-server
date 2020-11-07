package server

import server.data.AccountId
import server.data.Birthday

class AuthImpl : Auth {

    override fun login(email: String, password: String): AccountId {
        TODO("Not yet implemented")
    }

    override fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
        birthday: Birthday
    ): AccountId {
        TODO("Not yet implemented")
    }
}
