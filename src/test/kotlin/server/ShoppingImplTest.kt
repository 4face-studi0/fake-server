package server

import assert4k.assert
import assert4k.equals
import assert4k.that
import client.loginMarch
import client.registerMarch
import org.junit.Before
import server.data.*
import kotlin.test.Test

internal class ShoppingImplTest {

    private val database = Database()
    private val server = Server(database, Auth(), Shopping(database))
    private val shopping = server.shopping

    @Before
    fun setup() {
        server.registerMarch()
    }

    @Test
    fun `addToCart increases the count when the same item is added`() {
        with(shopping) {
            addToCart(TestItem1, 1)
            addToCart(TestItem1, 1)

            assert that getCart() equals Cart(
                items = mutableMapOf(
                    TestItem1 to 2
                )
            )
        }
    }

    @Test
    fun `removeItemFromCart removes the item correctly`() {
        with(shopping) {
            addToCart(TestItem1, 3)
            removeItemFromCart(TestItem1, 3)

            assert that getCart() equals Cart(
                items = mutableMapOf()
            )
        }
    }

    @Test
    fun `removeItemFromCart decreases the count for the item correctly`() {
        with(shopping) {
            addToCart(TestItem1, 3)
            removeItemFromCart(TestItem1, 1)

            assert that getCart() equals Cart(
                items = mutableMapOf(
                    TestItem1 to 2
                )
            )
        }
    }

    @Test
    fun `getCart return the correct list of items`() {
        with(shopping) {
            addToCart(TestItem1, 2)
            addToCart(TestItem2, 1)

            assert that getCart() equals Cart(
                items = mutableMapOf(
                    TestItem1 to 2,
                    TestItem2 to 1
                )
            )
        }
    }

    @Test
    fun `emptyCart removes all the items from the cart`() {
        with(shopping) {
            addToCart(TestItem1, 2)
            addToCart(TestItem2, 1)
            emptyCart()

            assert that getCart() equals Cart(
                items = mutableMapOf()
            )
        }
    }
}

val TestItem1 = Item(
    Type.Dress,
    Brand.HnM,
    Color.Nude,
    price = 25
)

val TestItem2 = Item(
    Type.Bottom,
    Brand.Zara,
    Color.White,
    price = 30
)
