package com.javapda.coffeemachine
import com.javapda.coffeemachine.CoffeeMakerConstants.Companion.MILK_ML_PER_CUP
import com.javapda.coffeemachine.CoffeeMakerConstants.Companion.WATER_ML_PER_CUP
import com.javapda.coffeemachine.CoffeeMakerConstants.Companion.COFFEE_BEAN_GRAMS_PER_CUP

data class Ingredients(var waterMl: Int = 0, val milkMl: Int = 0, val coffeeBeanGrams: Int = 0) {
    fun numberOfCupsMakeable(): Int =
        listOf(
            waterMl / WATER_ML_PER_CUP,
            milkMl / MILK_ML_PER_CUP,
            coffeeBeanGrams / COFFEE_BEAN_GRAMS_PER_CUP
        ).min()

}
operator fun Ingredients.unaryMinus() = Ingredients(-waterMl, -milkMl, -coffeeBeanGrams)
operator fun Ingredients.compareTo(other: Ingredients): Int =
    if (waterMl > other.waterMl && milkMl > other.milkMl && coffeeBeanGrams > other.coffeeBeanGrams) 1 else -1

