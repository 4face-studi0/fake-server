package server

import get
import server.data.Cart
import server.data.Database
import server.data.Item
import set
import minusAssign

class ShoppingImpl(
    private val database: Database
) : Shopping {

    @get:JvmName("cart")
    private val cart get() = database.carts[database.assertLoggedIn()]

    override fun addToCart(item: Item, count: Int) {
        val prevCount = cart.items[item]
        val nextCount = (prevCount ?: 0) + count
        cart.items[item] = nextCount
    }

    override fun removeItemFromCart(item: Item, count: Int) {
        val prevCount = cart.items[item]
        val nextCount = (prevCount ?: 0) - count
        if (nextCount > 0)
            cart.items[item] = nextCount
        else cart.items -= item
    }

    override fun getCart(): Cart =
        cart

    override fun emptyCart() {
        cart.items.clear()
    }

}
