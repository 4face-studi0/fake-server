package server.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val accountId: AccountId,
    val email: Email,
    val name: Name,
    val surname: Surname,
    val birthday: Birthday
) {
    override fun toString(): String {
        return "$name $surname $birthday"
    }
}

interface StringHolder {
    val string: String
}

@Serializable
data class AccountId(val number: Int)

@Serializable
data class Email(override val string: String) : StringHolder {
    override fun toString() = string
}

@Serializable
data class Name(override val string: String) : StringHolder {
    override fun toString() = string
}

@Serializable
data class Surname(override val string: String) : StringHolder {
    override fun toString() = string
}

@Serializable
data class Birthday(
    val day: Int,
    val month: Int,
    val year: Int
) {
    override fun toString(): String {
        return "$day $month $year"
    }
}
