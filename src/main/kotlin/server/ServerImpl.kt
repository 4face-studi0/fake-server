package server

import get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import server.data.*
import java.io.File

class ServerImpl : Server {

    private var database = Database()

    private var loggedIn
        get() = database.loggedIn
        set(value) {
            database.loggedIn = value
        }
    private val users get() = database.users
    private val credentials get() = database.credentials
    private val friends get() = database.friends

    override fun save() {
        ServerFile.writeText(Json.encodeToString(database))
    }

    override fun load() {
        if (ServerFile.exists().not()) return
        database = Json.decodeFromString(ServerFile.readText())
    }

    override fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
        birthday: Birthday,
    ): AccountId {

        // Check parameters
        checkName(name)
        checkSurname(surname)
        checkEmail(email)
        checkPassword(password)
        checkBirthday(birthday)
        check(users[Email(email)] == null) { "There is already an user registered with this email" }

        // Create a new user
        val lastId = users.takeIf { it.isNotEmpty() }?.maxOf { it.accountId.number } ?: -1
        val accountId = AccountId(lastId + 1)
        val user = User(
            accountId = accountId,
            email = Email(email),
            name = Name(name),
            surname = Surname(surname),
            birthday = birthday
        )

        // User info
        users += user
        credentials += Email(email) to password

        loggedIn = accountId
        return accountId
    }

    override fun login(email: String, password: String): AccountId {
        checkPasswordMatchesEmail(email, password)
        val user = checkNotNull(users[Email(email)]) { "No user found for given email address" }
        loggedIn = user.accountId
        return user.accountId
    }

    override fun addFriend(other: AccountId) {
        val me = requireNotNull(loggedIn) { "You're not logged in" }
        checkUserExists(other)
        if (other to me !in friends)
            friends += me to other
    }

    override fun removeFriend(other: AccountId) {
        val me = requireNotNull(loggedIn) { "You're not logged in" }
        checkUserExists(other)
        friends -= me to other
        friends -= other to me    }

    override fun getUser(accountId: AccountId): User? =
        users[accountId]

    override fun allUsers(): Set<User> =
        users

    override fun getFriendList(): List<User> {
        val me = requireNotNull(loggedIn) { "You're not logged in" }
        val allIdPairs = friends.filter { it.first == me || it.second == me }
        val allIdsList = allIdPairs.flatMap { listOf(it.first, it.second) }.toSet()
        return (allIdsList - me).mapNotNull(::getUser)
    }

    private fun checkName(name: String) {
        require(name.isNotBlank()) { "Name cannot be blank" }
    }

    private fun checkSurname(surname: String) {
        require(surname.isNotBlank()) { "Surname cannot be blank" }
    }

    private fun checkEmail(email: String) {
        require(""".+@.+\..*""".toRegex().matches(email)) { "Invalid email format" }
    }

    private fun checkPassword(password: String) {
        require(password.length >= 4) {
            "Password too short, it must be at least 4, but is ${password.length}"
        }
        require(password.any { it.isDigit() } && password.any { it.isLetter() }) {
            "Password must contain at least one number and one letter"
        }
    }

    private fun checkBirthday(birthday: Birthday) {
        require(birthday.day in 1..31) { "Day must be between 1 and 31, but is ${birthday.day}" }
        require(birthday.month in 1..12) { "Month must be between 1 and 12, but is ${birthday.month}" }
        require(birthday.year in 1900..2020) { "Year must be between 1900 and 2020, but is ${birthday.year}" }
    }

    private fun checkPasswordMatchesEmail(email: String, password: String) {
        require(credentials.find { it.first.string == email }?.second == password) { "Invalid credentials" }
    }

    private fun checkUserExists(accountId: AccountId) {
        requireNotNull(getUser(accountId)) { "No user found with the given account Id" }
    }

    private companion object {
        val ServerFile = File("server")
    }
}
