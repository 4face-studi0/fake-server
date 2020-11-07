import server.data.*

operator fun Collection<User>.get(accountId: AccountId) =
    find { it.accountId == accountId }

operator fun Collection<User>.get(email: Email) =
    find { it.email == email }

operator fun Collection<User>.get(name: Name) =
    find { it.name == name }

operator fun Collection<User>.get(surname: Surname) =
    find { it.surname == surname }

infix fun Collection<User>.findBy(email: Email) =
    find { it.email containsNoCase email }

infix fun Collection<User>.findBy(name: Name) =
    find { it.name containsNoCase name }

infix fun Collection<User>.findBy(surname: Surname) =
    find { it.surname containsNoCase surname }

infix fun String.containsNoCase(other: String) =
    contains(other, ignoreCase = true)

infix fun StringHolder.containsNoCase(other: StringHolder) =
    string containsNoCase other.string
