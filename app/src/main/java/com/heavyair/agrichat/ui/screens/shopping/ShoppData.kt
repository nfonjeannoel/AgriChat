package com.heavyair.agrichat.ui.screens.shopping

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.heavyair.agrichat.R

data class Product(
    val productName: String,
    val productDescription: String,
    @DrawableRes val productImage: Int,

    )


object ShoppData {
//        Product(
//            productName = "Premium Fertilizer",
//            productDescription = "Boost your crop yield with our premium fertilizer.",
//            productImage = R.drawable.agrichat,
//        )

    val products = listOf<Product>(
        Product(
            productName = "Agrodyke",
            productDescription = "Organic multipurpose fertilizer for all crops.",
            productImage = R.drawable.prod1,
        ),
        Product(
            productName = "NPK Fertilizer",
            productDescription = "The nitrogen in NPK fertilizer is usefully for helping plants to growth leaves",
            productImage = R.drawable.prod2,
        ),
        Product(
            productName = "Premium Fertilizer",
            productDescription = "Explore our range of sustainable food components for a greener future.",
            productImage = R.drawable.agrichat,
        ),
    )


}