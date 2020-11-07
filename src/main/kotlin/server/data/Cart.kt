package server.data

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val items: MutableMap<Item, Int>
) {
    fun totalPrice(): Int {
        return items.values.sum()
    }
}
