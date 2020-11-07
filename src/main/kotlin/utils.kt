import server.data.*

// Users Collection
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

// Carts Collection
operator fun MutableCollection<Pair<AccountId, Cart>>.get(accountId: AccountId): Cart {
    val foundCart = find { it.first == accountId }?.second
    val cart = foundCart ?: Cart(items = mutableSetOf())
    if (foundCart == null) this += accountId to cart
    return cart
}

operator fun MutableCollection<Pair<Item, Int>>.get(item: Item): Int? =
    find { it.first == item }?.second

operator fun MutableCollection<Pair<Item, Int>>.set(item: Item, count: Int) {
    minusAssign(item)
    add(item to count)
}

operator fun MutableCollection<Pair<Item, Int>>.minusAssign(item: Item) {
    val found = find { it.first == item }
    remove(found)
}

infix fun String.containsNoCase(other: String) =
    contains(other, ignoreCase = true)

infix fun StringHolder.containsNoCase(other: StringHolder) =
    string containsNoCase other.string
