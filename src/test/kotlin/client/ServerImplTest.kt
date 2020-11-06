package client

import kotlin.test.Test
import kotlin.test.*
import assert4k.*
import server.*

class ServerImplTest {

    private val server = ServerImpl()

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
        val marchId = registerMarch()

        server.addFriend(davideId, marchId)
        assert that server.getFriendList(davideId) equals listOf(server.getUser(marchId))
    }

    @Test
    fun `verify friend is added for both the users`() {
        val davideId = registerDavide()
        val marchId = registerMarch()

        server.addFriend(davideId, marchId)
        assert that server.getFriendList(davideId) equals listOf(server.getUser(marchId))
        assert that server.getFriendList(marchId) equals listOf(server.getUser(davideId))
    }

    @Test
    fun `verify friend can be removed correctly`() {
        val davideId = registerDavide()
        val marchId = registerMarch()

        server.addFriend(davideId, marchId)
        server.removeFriend(davideId, marchId)
        assert that server.getFriendList(davideId) equals emptyList<User>()
    }

    @Test
    fun `verify friend is removed for both the users`() {
        val davideId = registerDavide()
        val marchId = registerMarch()

        server.addFriend(davideId, marchId)
        server.removeFriend(davideId, marchId)
        assert that server.getFriendList(marchId) equals emptyList<User>()
    }

    @Test
    fun `state can be saved and loaded`() {
        val davideId = registerDavide()
        val marchId = registerMarch()
        server.addFriend(davideId, marchId)

        server.save()
        val newServer = ServerImpl()
        newServer.load()

        assert that newServer.getFriendList(davideId) equals listOf(newServer.getUser(marchId))
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
        "abc123",
        Birthday(22, 3, 1997)
    )

}
