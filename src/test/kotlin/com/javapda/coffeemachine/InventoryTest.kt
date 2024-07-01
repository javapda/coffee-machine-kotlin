package com.javapda.coffeemachine

import com.javapda.coffeemachine.Ingredients
import com.javapda.coffeemachine.Inventory
import com.javapda.coffeemachine.unaryMinus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InventoryTest {

    @Test
    fun testAdjustInventory() {
        val baseInventory = Inventory(Ingredients(100, 100, 100), 100, 100)
        val marginInventory = Inventory(-Ingredients(30, 20, 10), -1, 7)
        val adjInv = baseInventory.adjust(marginInventory)
        assertEquals(70, adjInv.ingredients.waterMl)
        assertEquals(80, adjInv.ingredients.milkMl)
        assertEquals(90, adjInv.ingredients.coffeeBeanGrams)
        assertEquals(99, adjInv.numberOfDisposableCups)
        assertEquals(107, adjInv.numberOfDollars)
    }

    @Test
    fun testInventoryReport() {
        assertEquals(
            """The coffee machine has:
17 ml of water
985 ml of milk
81 g of coffee beans
27 disposable cups
${'$'}532 of money""",
            Inventory(
                ingredients = Ingredients(waterMl = 17, milkMl = 985, coffeeBeanGrams = 81),
                numberOfDisposableCups = 27,
                numberOfDollars = 532
            ).inventoryReport()
        )
    }

}