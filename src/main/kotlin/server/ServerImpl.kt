package server

class ServerImpl : Server {

    private val ids = mutableSetOf<AccountId>()
    private val emails = mutableMapOf<String, User>()
    private val passwords = mutableMapOf<String, User>()
    private val friends = mutableSetOf<Pair<AccountId, AccountId>>()

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
        check(emails[email] == null) { "There is already an user registered with this email" }

        // Create a new user
        val lastId = ids.takeIf { it.isNotEmpty() }?.maxOf { it.number } ?: -1
        val accountId = AccountId(lastId + 1)
        val user = User(
            accountId = accountId,
            name = name,
            surname = surname,
            birthday = birthday
        )

        // User info
        ids += accountId
        emails[email] = user
        passwords[password] = user

        return accountId
    }

    override fun login(email: String, password: String): AccountId {
        checkPasswordMatchesEmail(email, password)
        val user = checkNotNull(emails[email]) { "No user found for given email address" }
        return user.accountId
    }

    override fun addFriend(me: AccountId, other: AccountId) {
        checkUserExists(me)
        checkUserExists(other)
        if (other to me !in friends)
            friends += me to other
    }

    override fun removeFriend(me: AccountId, other: AccountId) {
        checkUserExists(me)
        checkUserExists(other)
        friends -= me to other
        friends -= other to me
    }

    override fun getUser(accountId: AccountId): User? =
        emails.values.find { it.accountId == accountId }

    override fun getFriendList(accountId: AccountId): List<User> {
        checkUserExists(accountId)
        val allIdPairs = friends.filter { it.first == accountId || it.second == accountId }
        val allIdsList = allIdPairs.flatMap { listOf(it.first, it.second) }.toSet()
        return (allIdsList - accountId).mapNotNull(::getUser)
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
        require(emails[email] == passwords[password]) { "Invalid credentials" }
    }

    private fun checkUserExists(accountId: AccountId) {
        requireNotNull(getUser(accountId)) { "No user found with the given account Id" }
    }
}
