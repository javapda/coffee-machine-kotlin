package com.javapda.coffeemachine

import com.javapda.coffeemachine.CoffeeMakerConstants.Companion.COFFEE_BEAN_GRAMS_PER_CUP
import com.javapda.coffeemachine.CoffeeMakerConstants.Companion.MILK_ML_PER_CUP
import com.javapda.coffeemachine.CoffeeMakerConstants.Companion.WATER_ML_PER_CUP
import com.javapda.coffeemachine.Ingredients
import com.javapda.coffeemachine.unaryMinus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IngredientsTest {
    @BeforeEach
    fun beforeEach() = println("After Each")

    @AfterEach
    fun afterEach() = println("After Each")

    @Test
    fun firstTest() {
        assertEquals(10, 10)
    }

    @Test
    fun minusOrNegatedIngredients() {
        val start: Ingredients = Ingredients(100, 50, 75)
        val negated = -start
        assertEquals(-100, negated.waterMl)
        assertEquals(-50, negated.milkMl)
        assertEquals(-75, negated.coffeeBeanGrams)

    }

    @Test
    fun testIngredients() {
        assertEquals(
            1,
            Ingredients(WATER_ML_PER_CUP, MILK_ML_PER_CUP, COFFEE_BEAN_GRAMS_PER_CUP).numberOfCupsMakeable()
        )

    }


}