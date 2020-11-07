package client

import server.Auth
import server.data.AccountId
import server.data.Birthday
import server.Server
import server.Shopping
import server.data.Database

val database = Database.load()

fun main() {
    val server = Server(database, Auth(), Shopping(database))

    menu(server)
}

fun menu(server: Server) {
    database.save()

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
        "login" -> startLogin(server)
        "add" -> startAddingFriend(server)
        "remove" -> startRemoveFriend(server)
        "friends" -> seeFriendList(server)
        "users" -> seeAllUsers(server)
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

    server.register(name, surname, email, password, birthday)

    menu(server)
}

fun startLogin(server: Server) {
    println("Email address")
    val email = readLine()!!
    println("Enter Your Password")
    val password = readLine()!!

    val accountId = server.login(email, password)
    println("logged in as ${server.getUser(accountId)!!.name}")

    menu(server)
}

fun startAddingFriend(server: Server) {
    println("Enter Your Friend Name")
    val name = readLine()!!
    val accountId = findUserByName(server, name)
    requireNotNull(accountId)

    server.addFriend(other = accountId)

    menu(server)
}

fun startRemoveFriend(server: Server) {
    println("Enter the Name for remove")
    val name = readLine()!!
    val accountId = findUserByName(server, name)
    requireNotNull(accountId)

    server.removeFriend(other = accountId)

    menu(server)
}

fun seeFriendList(server: Server) {
    val friendList = server.getFriendList()
    if (friendList.isEmpty()) {
        println("your Friends List is Empty")
    } else {
        println(friendList)
    }

    menu(server)
}

fun seeAllUsers(server: Server) {
    println(server.allUsers())

    menu(server)
}

fun findUserByName(server: Server, query: String): AccountId? {
    val findUsers = server.allUsers().filter { query in it.name.string || query in it.surname.string }
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



