package server.data

import kotlinx.serialization.Serializable

@Serializable
data class Item (
    val type: Type,
    val brand: Brand,
    val color: Color,
    val price: Int,
)

@Serializable
enum class Type {
    Shoe,
    Dress,
    Bottom,
    Top,
    Jacket,
    Coat,
    Makeup,
}

@Serializable
enum class Color{
    Black,
    Green,
    Nude,
    Red,
    Yellow,
    White,
    Purple,
    Pink,
    Blue,
}

@Serializable
enum class Brand {
    Zara,
    HnM,
    Nike,
}