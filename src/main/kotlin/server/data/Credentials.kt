package server.data

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    val user: User,
    val password: String
)
