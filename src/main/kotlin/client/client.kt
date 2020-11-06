package client

import server.AccountId
import server.Birthday
import server.Server
import server.User

fun main() {
    val server = Server()
    server.load()

    menu(server, null)
}

fun menu(server: Server, myId: AccountId?) {
    server.save()

    println("""
        
        Type 'register' for register a new User.
        Type 'login' for login with an existing User.
        Type 'add' for add a new Friend
        Type 'remove' for remove a Friend
        Type 'friends' for see the list of your Friends
        Type 'users' for see all the registered Users
    """.trimIndent())

    val selection = readLine()
    when (selection) {
        "register" -> startRegistration(server)
        "login" -> starLogin(server)
        "add" -> startAddingFriend(server, myId)
        "remove" -> startRemoveFriend(server, myId)
        "friends" -> seeFriendList(server, myId)
        "users" -> seeAllUsers(server, myId)
        else -> println("Unknown command")
    }
}

fun startRegistration(server: Server) {
    println("Insert your Name")
    val name = readLine()!!
    println("Insert your Surname")
    val surname = readLine()!!
    println("Insert Email Address")
    val email = readLine()!!
    println("Insert password")
    val password = readLine()!!
    println("Insert Your Birthday as dd/mm/yyyy")
    val birthdayString = readLine()!!
    val (day, month, year) = birthdayString // "22 / 03 / 1997"
        .split('/') // "22 " - " 03 " - " 1997"
        .map { it.trim().toInt() } // trim: remove white spaces -> "22" - "03" - "1997"
    val birthday = Birthday(day, month, year)

    val accountId = server.register(name, surname, email, password, birthday)

    menu(server, accountId)
}

fun starLogin(server: Server) {
    println("Email address")
    val email = readLine()!!
    println("Enter Your Password")
    val password = readLine()!!

    val accountId = server.login(email, password)
    println("logged in as ${server.getUser(accountId)!!.name}")

    menu(server, accountId)
}

fun startAddingFriend(server: Server, myId: AccountId?) {
    checkNotNull(myId) { "You're not logged in" }

    println("Enter Your Friend Name")
    val name = readLine()!!
    val accountId = findUserByName(server, name)
    requireNotNull(accountId)

    server.addFriend(me = myId, other = accountId)

    menu(server, myId)
}

fun startRemoveFriend(server: Server, myId: AccountId?) {
    checkNotNull(myId) { "You're not logged in" }

    println("Enter the Name for remove")
    val name = readLine()!!
    val accountId = findUserByName(server, name)
    requireNotNull(accountId)

    server.removeFriend(me = myId, other = accountId)

    menu(server, myId)
}

fun seeFriendList(server: Server, myId: AccountId?) {
    checkNotNull(myId) { "You're not logged in" }

    val friendList = server.getFriendList(myId)
    if (friendList.isEmpty()) {
        println("your Friends List is Empty")
    } else {
        println(friendList)
    }

    menu(server, myId)
}

fun seeAllUsers(server: Server, myId: AccountId?) {
    println(server.allUsers())

    menu(server, myId)
}

fun findUserByName(server: Server, query: String): AccountId? {
    val findUsers = server.allUsers().filter { query in it.name || query in it.surname }
    return if (findUsers.isEmpty()) {
        println("No Users Found")
        null
    } else {
        println("Select the User you want")
        println(findUsers.mapIndexed { index, user -> "${index + 1} - $user" })
        val selection = readLine()!!.toInt()
        findUsers[selection - 1].accountId
    }
}

