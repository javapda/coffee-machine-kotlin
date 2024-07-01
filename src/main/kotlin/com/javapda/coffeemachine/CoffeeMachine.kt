package com.javapda.coffeemachine

import com.javapda.coffeemachine.CoffeeMachineState.*
import com.javapda.coffeemachine.CoffeeMakerConstants.Companion.coffeeCostMap
import com.javapda.coffeemachine.CoffeeMakerConstants.Companion.ingredientsMap
import kotlin.system.exitProcess

class CoffeeMachine(
    private val initialState: CoffeeMachineState = CHOOSING_AN_ACTION,
    private var inventory: Inventory = Inventory(Ingredients(400, 540, 120), 9, 550),
) {
    class UnhandledStateException(state: CoffeeMachineState) : Exception("unhandled coffee machine state '${state}'")

    var userInputCount: Int = 0
    fun userInput(userInput: String): Boolean {
        userInputCount++
        if (userInput.isBlank()) {
            prompt()
            return true
        }
        if (userInput == "exit") return false
        return processStateInput(userInput)
    }

    private fun adjustInventory(deltaInventory: Inventory) {
        inventory = inventory.adjust(deltaInventory)
    }

    private fun processStateInput(userInput: String): Boolean {
        if (userInput == "inv") {
            println(inventory.inventoryReport())
            return true
        } else if (userInput == "back") {
            state = CHOOSING_AN_ACTION
            return true
        }
        return when (state) {
            CHOOSING_AN_ACTION -> chooseAction(userInput)
            CHOOSING_TYPE_OF_COFFEE -> chooseCoffeeToBrew(userInput)
            FILL_WATER -> fill(userInput.toInt(), FILL_MILK)
            FILL_MILK -> fill(userInput.toInt(), FILL_COFFEE_BEANS)
            FILL_COFFEE_BEANS -> fill(userInput.toInt(), FILL_DISPOSABLE_CUPS)
            FILL_DISPOSABLE_CUPS -> fill(userInput.toInt(), CHOOSING_AN_ACTION)
            TAKE_MONEY -> takeMoney()
            else -> throw Exception("no support for state: $state, userInput=$userInput")
        }
    }

    private fun takeMoney(): Boolean {
        val dollars = inventory.numberOfDollars
        inventory = inventory.adjust(
            Inventory(
                Ingredients(0, 0, 0),
                0,
                -inventory.numberOfDollars
            )
        )
        println()
        println("I gave you \$$dollars")
        println()
        state = CHOOSING_AN_ACTION
        return true
    }

    private fun fill(amount: Int, nextState: CoffeeMachineState): Boolean {

        when (state) {
            FILL_WATER -> adjustInventory(Inventory(Ingredients(waterMl = amount), 0, 0))
            FILL_MILK -> adjustInventory(Inventory(Ingredients(milkMl = amount), 0, 0))
            FILL_COFFEE_BEANS -> adjustInventory(Inventory(Ingredients(coffeeBeanGrams = amount), 0, 0))
            FILL_DISPOSABLE_CUPS -> adjustInventory(Inventory(Ingredients(), amount, 0))
            else -> throw UnhandledStateException(state)
        }
        state = nextState
        return true
    }


    /**
     * Choose coffee to brew
     * checks to see if there is adequate inventory
     * if not enough ingredients, then report the shortage
     * otherwise, write you have enough...
     *
     * @param coffeeToBrew : 1=espresso, 2=latte, 3=cappuccino
     * @return
     */
    private fun chooseCoffeeToBrew(coffeeToBrew: String, neededDisposableCups: Int = 1): Boolean {
        val neededIngredients: Ingredients = ingredientsMap[coffeeToBrew.toInt()] ?: Ingredients()
        val cost = coffeeCostMap[coffeeToBrew.toInt()] ?: 0
        if (inventory.ingredients > neededIngredients) {
            println("I have enough resources, making you a coffee!")
            println()
            inventory = inventory.adjust(Inventory(-neededIngredients, -neededDisposableCups, cost))
        } else {
            val lowList = inventory.ingredients.whatsLow(neededIngredients)
            println("Sorry, not enough ${lowList.joinToString(", ")}!")
            println()
        }
        state = CHOOSING_AN_ACTION

        return true
    }

    private fun Ingredients.whatsLow(other: Ingredients): List<String> {
        return if (this > other) {
            listOf()
        } else {
            val whatsLow = mutableListOf<String>()
            if (waterMl < other.waterMl) whatsLow.add("water")
            if (milkMl < other.milkMl) whatsLow.add("milk")
            if (coffeeBeanGrams < other.coffeeBeanGrams) whatsLow.add("coffee beans")
            whatsLow
        }
    }

    private fun chooseAction(option: String): Boolean {
        when (option) {
            "buy" -> state = CHOOSING_TYPE_OF_COFFEE
            "fill" -> state = FILL_WATER
            "take" -> {
                state = TAKE_MONEY
                takeMoney()
            }

            "state" -> println("current state is '$state'")
            "remaining" -> {
                println()
                println(inventory.inventoryReport())
                state = CHOOSING_AN_ACTION
                prompt()
            }

            "exit" -> exitProcess(0)
        }
        return true

    }

    private fun prompt(message: String = state.prompt) {
        if (message.isNotBlank()) println("\n$message ")
//        if (message.isNotBlank()) print("$message\n> ")
    }

    private var state: CoffeeMachineState = initialState
        set(value) {
            val changed = field != value
            field = value
            // handle state transitions?
            if (changed) prompt(state.prompt)
        }

    fun info(): Any =
        """
            Inventory :   $inventory
            state:        $state, prompt: ${state.prompt}
        """.trimIndent()
}

fun main() {
    val coffeeMachine = CoffeeMachine().also { it.userInput("") }
    while (coffeeMachine.userInput(readln()));

}