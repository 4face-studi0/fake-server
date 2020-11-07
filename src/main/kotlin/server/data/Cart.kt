package server.data

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val items: MutableSet<Pair<Item, Int>>
) {
    fun totalPrice(): Int {
        return items.sumBy { it.second }
    }
}
