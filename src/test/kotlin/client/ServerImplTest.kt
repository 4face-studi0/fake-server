package client

import kotlin.test.Test
import kotlin.test.*
import assert4k.*
import server.*

class ServerImplTest {

    private val server: Server = ServerImpl()

    @Test
    fun `register works successfully if all the parameters are valid`() {
        registerDavide()
    }

    @Test
    fun `User can register correctly`() {
        val accountId = server.register(
            "Davide",
            "Farella",
            "fardavide@gmail.com",
            "abc123",
            Birthday(20, 8, 1991)
        )

        assert that accountId equals server.login("fardavide@gmail.com", "abc123")
    }

    @Test
    fun `new User is created with a new Id`() {
        val accountId1 = server.register(
            "Davide",
            "Farella",
            "fardavide@gmail.com",
            "abc123",
            Birthday(20, 8, 1991)
        )
        val accountId2 = registerMarch()

        assert that accountId1 `not equals` accountId2
    }

    @Test
    fun `verify that we cant register more than one user with the same email`() {
        registerDavide()

        assert that fails {
            registerDavide()
        } with "There is already an user registered with this email"
    }

    @Test
    fun `verify we cant register with an empty name`() {
        assert that fails {
            server.register(
                "",
                "Farella",
                "fardavide@gmail.com",
                "abc123",
                Birthday(20, 8, 1991)
            )
        } with "Name cannot be blank"
    }

    @Test
    fun `verify we cant register with an empty surname`() {
        assert that fails {
            server.register(
                "Davide",
                "",
                "fardavide@gmail.com",
                "abc123",
                Birthday(20, 8, 1991)
            )
        } with "Surname cannot be blank"
    }

    @Test
    fun `verify we cant register with an invalid email`() {
        assert that fails {
            server.register(
                "Davide",
                "Farella",
                "fardavide@com",
                "abc123",
                Birthday(20, 8, 1991)
            )
        } with "Invalid email format"
    }

    @Test
    fun `verify that the password requires at least one letter`() {
        assert that fails {
            server.register(
                "Davide",
                "Farella",
                "fardavide@gmail.com",
                "1234",
                Birthday(20, 8, 1991)
            )
        } with "Password must contain at least one number and one letter"
    }

    @Test
    fun `verify that the password requires at least one number`() {
        assert that fails {
            server.register(
                "Davide",
                "Farella",
                "fardavide@gmail.com",
                "hello",
                Birthday(20, 8, 1991)
            )
        } with "Password must contain at least one number and one letter"
    }

    @Test
    fun `verify friend can be added correctly`() {
        val davideId = registerDavide()
        registerMarch()

        server.addFriend(davideId)
        assert that server.getFriendList() equals listOf(server.getUser(davideId))
    }

    @Test
    fun `verify friend is added for both the users`() {
        val davideId = registerDavide()
        val marchId = registerMarch()

        server.addFriend(davideId)
        assert that server.getFriendList() equals listOf(server.getUser(davideId))
        loginDavide()
        assert that server.getFriendList() equals listOf(server.getUser(marchId))
    }

    @Test
    fun `verify friend can be removed correctly`() {
        val davideId = registerDavide()
        registerMarch()

        server.addFriend(davideId)
        server.removeFriend(davideId)
        assert that server.getFriendList() equals emptyList()
    }

    @Test
    fun `verify friend is removed for both the users`() {
        val davideId = registerDavide()
        registerMarch()

        server.addFriend(davideId)
        server.removeFriend(davideId)
        assert that server.getFriendList() equals emptyList()
        loginMarch()
        assert that server.getFriendList() equals emptyList()
    }

    @Test
    fun `state can be saved and loaded`() {
        val davideId = registerDavide()
        val marchId = registerMarch()
        server.addFriend(davideId)

        server.save()
        val newServer: Server = ServerImpl()
        newServer.load()

        assert that newServer.getFriendList() equals listOf(newServer.getUser(davideId))
        loginDavide()
        assert that server.getFriendList() equals listOf(server.getUser(marchId))
    }

    private fun registerDavide() = server.register(
        "Davide",
        "Farella",
        "fardavide@gmail.com",
        "abc123",
        Birthday(20, 8, 1991)
    )

    private fun registerMarch() = server.register(
        "March",
        "Queen",
        "marchq2292@gmail.com",
        "def456",
        Birthday(22, 3, 1997)
    )

    private fun loginDavide() = server.login(
        "fardavide@gmail.com",
        "abc123",
    )

    private fun loginMarch() = server.login(
        "marchq2292@gmail.com",
        "def456",
    )

}
