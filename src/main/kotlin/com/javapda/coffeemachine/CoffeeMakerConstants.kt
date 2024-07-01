package com.javapda.coffeemachine

class CoffeeMakerConstants {
    companion object {
        const val WATER_ML_PER_CUP = 200
        const val MILK_ML_PER_CUP = 50
        const val COFFEE_BEAN_GRAMS_PER_CUP = 15
        const val ESPRESSO_COST = 4
        const val LATTE_COST = 7
        const val CAPPUCCINO_COST = 6

        val espressoCupIngredients = Ingredients(250, 0, 16)
        val latteCupIngredients = Ingredients(350, 75, 20)
        val cappuccinoCupIngredients = Ingredients(200, 100, 12)
        val ingredientsMap: Map<Int, Ingredients> = mapOf(
            1 to espressoCupIngredients,
            2 to latteCupIngredients,
            3 to cappuccinoCupIngredients
        )
        val coffeeCostMap: Map<Int, Int> = mapOf(
            1 to ESPRESSO_COST,
            2 to LATTE_COST,
            3 to CAPPUCCINO_COST
        )

    }
}
