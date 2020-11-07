package server

import server.data.Cart
import server.data.Database
import server.data.Item

interface Shopping {

    /**
     * Add the given [Item] to the personal Cart
     */
    fun addToCart(item: Item, count: Int = 1)

    /**
     * Remove the given [Item] to the personal Cart
     */
    fun removeItemFromCart(item: Item, count: Int = 1)

    /**
     * Get all the items in the personal [Cart]
     */
    fun getCart(): Cart

    /**
     * Remove all the items from the personal Cart
     */
    fun emptyCart()
}

fun Shopping(database: Database): Shopping = ShoppingImpl(database)
