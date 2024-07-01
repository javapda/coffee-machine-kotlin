package com.javapda

import kotlin.system.exitProcess

private const val WATER_ML_PER_CUP = 200

private const val MILK_ML_PER_CUP = 50

private const val COFFEE_BEAN_GRAMS_PER_CUP = 15

class CoffeeMachine {
    private var inventory = Inventory(Ingredients(400, 540, 120), 9, 550)
    private var numberOfCoffeeCups: Int = 0
        set(value) {
            field = value
        }

    init {
        test()
    }

    fun askUserHowManyCoffeeCupsNeeded(): Int = promptUserForNumber("Write how many cups of coffee you will need:")

    operator fun Ingredients.unaryMinus() = Ingredients(-waterMl, -milkMl, -coffeeBeanGrams)
    operator fun Ingredients.compareTo(other: Ingredients): Int =
        if (waterMl > other.waterMl && milkMl > other.milkMl && coffeeBeanGrams > other.coffeeBeanGrams) 1 else -1

    fun Ingredients.whatsLow(other: Ingredients): List<String> {
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

    data class Ingredients(var waterMl: Int = 0, val milkMl: Int = 0, val coffeeBeanGrams: Int = 0) {
        fun numberOfCupsMakeable(): Int =
            listOf(
                waterMl / WATER_ML_PER_CUP,
                milkMl / MILK_ML_PER_CUP,
                coffeeBeanGrams / COFFEE_BEAN_GRAMS_PER_CUP
            ).min()

        companion object
    }

    val espressoCupIngredients = Ingredients(250, 0, 16)
    val latteCupIngredients = Ingredients(350, 75, 20)
    val cappuccinoCupIngredients = Ingredients(200, 100, 12)
    val espressoCost = 4
    val latteCost = 7
    val cappuccinoCost = 6

    data class Inventory(val ingredients: Ingredients, val numberOfDisposableCups: Int, val numberOfDollars: Int) {
        fun adjust(margin: CoffeeMachine.Inventory): Inventory {
            return Inventory(
                Ingredients(
                    ingredients.waterMl + margin.ingredients.waterMl,
                    ingredients.milkMl + margin.ingredients.milkMl,
                    ingredients.coffeeBeanGrams + margin.ingredients.coffeeBeanGrams
                ),
                numberOfDisposableCups + margin.numberOfDisposableCups,
                numberOfDollars + margin.numberOfDollars
            )
        }
    }

    fun askUserForIngredientsAvailable(): Ingredients {
        val numberOfWaterMlAvailable = promptUserForNumber("Write how many ml of water the coffee machine has:")
        val numberOfMilkMlAvailable = promptUserForNumber("Write how many ml of milk the coffee machine has:")
        val numberOfCoffeeBeanGramsAvailable =
            promptUserForNumber("Write how many grams of coffee beans the coffee machine has:")
        return Ingredients(numberOfWaterMlAvailable, numberOfMilkMlAvailable, numberOfCoffeeBeanGramsAvailable)

    }

    private fun promptUserForNumber(question: String): Int {
        return promptUser(question).toInt()
    }

    private fun promptUser(question: String): String {
        println(question)
        print("> ")
        return readln()
    }

    private fun ingredientsRequiredReport(cupsRequired: Int = numberOfCoffeeCups): String {
//        one cup of coffee made on this coffee machine contains 200 ml of water, 50 ml of milk, and 15 g
        val waterMlRequired = waterMlRequiredForCups(cupsRequired)
        val milkMlRequired = milkMlRequiredForCups(cupsRequired)
        val coffeeBeanGramsRequired = coffeeBeanGramsForCups(cupsRequired)
        return """For $cupsRequired cups of coffee you will need:
$waterMlRequired ml of water
$milkMlRequired ml of milk
$coffeeBeanGramsRequired g of coffee beans"""
    }

    private fun coffeeBeanGramsForCups(cupsRequired: Int = numberOfCoffeeCups): Int =
        cupsRequired * COFFEE_BEAN_GRAMS_PER_CUP

    private fun milkMlRequiredForCups(cupsRequired: Int = numberOfCoffeeCups): Int = cupsRequired * MILK_ML_PER_CUP

    private fun waterMlRequiredForCups(cupsRequired: Int = numberOfCoffeeCups): Int = cupsRequired * WATER_ML_PER_CUP

    fun stage1() {
        println(
            """
            Starting to make a coffee
            Grinding coffee beans
            Boiling water
            Mixing boiled water with crushed coffee beans
            Pouring coffee into the cup
            Pouring some milk into the cup
            Coffee is ready!
        """.trimIndent()
        )
    }

    private fun test() {
        testIngredientsMinusOrNegation()
        testAdjustInventory()
        testInventoryReport()
        testIngredients()
        testEvaluateIngredientsForRequiredCups()
        testGramsOfCoffeeBeansForCups()
        testWaterMlRequiredForCups()
        testMilkMlRequiredForCups()
        testReportIngredientsRequired()
    }

    private fun testIngredientsMinusOrNegation() {
        val start: Ingredients = Ingredients(100, 50, 75)
        val negated = -start
        reqCM(-100, negated.waterMl)
        reqCM(-50, negated.milkMl)
        reqCM(-75, negated.coffeeBeanGrams)
    }

    private fun testAdjustInventory() {
        val baseInventory = Inventory(Ingredients(100, 100, 100), 100, 100)
        val marginInventory = Inventory(-Ingredients(30, 20, 10), -1, 7)
        val adjInv = baseInventory.adjust(marginInventory)
        reqCM(70, adjInv.ingredients.waterMl)
        reqCM(80, adjInv.ingredients.milkMl)
        reqCM(90, adjInv.ingredients.coffeeBeanGrams)
        reqCM(99, adjInv.numberOfDisposableCups)
        reqCM(107, adjInv.numberOfDollars)
    }

    private fun testInventoryReport() {
        val expected = """The coffee machine has:
400 ml of water
540 ml of milk
120 g of coffee beans
9 disposable cups
${'$'}550 of money"""
        reqCM(
            expected, inventoryReport(
                Inventory(
                    Ingredients(400, 540, 120), 9, 550
                )
            )
        )
    }

    fun inventoryReport(theInventory: CoffeeMachine.Inventory = inventory): String {
        return """The coffee machine has:
${theInventory.ingredients.waterMl} ml of water
${theInventory.ingredients.milkMl} ml of milk
${theInventory.ingredients.coffeeBeanGrams} g of coffee beans
${theInventory.numberOfDisposableCups} disposable cups
${'$'}${theInventory.numberOfDollars} of money"""
    }

    private fun testIngredients() {
        reqCM(1, Ingredients(WATER_ML_PER_CUP, MILK_ML_PER_CUP, COFFEE_BEAN_GRAMS_PER_CUP).numberOfCupsMakeable())
    }

    private fun testEvaluateIngredientsForRequiredCups() {

        reqCM("No, I can make only 0 cups of coffee", evaluateIngredientForRequiredCups(Ingredients(0, 0, 0), 1))
        reqCM("Yes, I can make that amount of coffee", evaluateIngredientForRequiredCups(Ingredients(300, 65, 100), 1))
        reqCM(
            "Yes, I can make that amount of coffee (and even 2 more than that)",
            evaluateIngredientForRequiredCups(Ingredients(1550, 299, 300), 3)
        )
    }

    fun evaluateIngredientForRequiredCups(ingredients: CoffeeMachine.Ingredients, cupsRequired: Int): String {
        val numberOfCupsMakeable = ingredients.numberOfCupsMakeable()
        return when {
            numberOfCupsMakeable < cupsRequired -> "No, I can make only $numberOfCupsMakeable cups of coffee"
            numberOfCupsMakeable == cupsRequired -> "Yes, I can make that amount of coffee"
            else -> "Yes, I can make that amount of coffee (and even ${numberOfCupsMakeable - cupsRequired} more than that)"
        }
    }

    private fun testGramsOfCoffeeBeansForCups() {
        reqCM(375, coffeeBeanGramsForCups(25))
    }

    private fun testMilkMlRequiredForCups() {
        reqCM(1250, milkMlRequiredForCups(25))
    }

    private fun testWaterMlRequiredForCups() {
        reqCM(5000, waterMlRequiredForCups(25))
    }


    private fun testReportIngredientsRequired() {
        val expected = """For 125 cups of coffee you will need:
25000 ml of water
6250 ml of milk
1875 g of coffee beans"""
        reqCM(expected, ingredientsRequiredReport(125))
    }

    private fun <T> reqCM(expected: T, actual: T) {
        require(expected == actual) { "expected '${expected}', but found '$actual'" }
    }

    fun askSpecialWorkerForCommand() {
        println("Write action (buy, fill, take, remaining, exit):")
        print("> ")
        processSpecialWorkerCommand(readln())
    }

    class UnknownSpecialWorkerCommand(specialWorkerCommand: String) :
        Exception("unknown special worker command '$specialWorkerCommand', should be one of buy, fill, or take")

    private fun help() {
        println(
            """
            TODO: Help for ${this::class.simpleName}
        """.trimIndent()
        )
    }

    private fun processSpecialWorkerCommand(specialWorkerCommand: String) {
        when (specialWorkerCommand) {
            "buy" -> buyCommand()
            "fill" -> fillCommand()
            "take" -> takeCommand()
            "inv" -> println(inventoryReport())
            "remaining" -> println(inventoryReport())
            "exit" -> exitProcess(0)
            in listOf("?", "help") -> help()
            else -> throw UnknownSpecialWorkerCommand(specialWorkerCommand)
        }
    }

    private fun buyCommand(): Unit {
        val buyOption = promptUser("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:")
        if (buyOption != "back") {
            val whatToBuy = buyOption.toInt()
            when (whatToBuy) {
//                promptUserForNumber("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:")) {
                1 -> buyCoffee(espressoCupIngredients, 1, espressoCost) // buyEspresso()
                2 -> buyCoffee(latteCupIngredients, 1, latteCost) // buyLatte()
                3 -> buyCoffee(cappuccinoCupIngredients, 1, cappuccinoCost) // buyCappuccino()
                else -> throw UnknownSpecialWorkerCommand("for buyCommand '$whatToBuy' is not an option")
            }
        }

    }

    private fun buyCoffee(neededIngredients: Ingredients, neededDisposableCups: Int = 1, cost: Int) {
        if (inventory.ingredients > neededIngredients) {
            println("I have enough resources, making you a coffee!")
            inventory = inventory.adjust(Inventory(-neededIngredients, -neededDisposableCups, cost))
        } else {
            val lowList = inventory.ingredients.whatsLow(neededIngredients)
            println("Sorry, not enough ${lowList.joinToString(", ")}!")
        }

    }

    private fun fillCommand(): Unit {
        val waterMlToAdd = promptUserForNumber("Write how many ml of water you want to add: ")
        val milkMlToAdd = promptUserForNumber("Write how many ml of milk you want to add: ")
        val coffeeBeanGramsToAdd = promptUserForNumber("Write how many grams of coffee beans you want to add: ")
        val disposableCupsToAdd = promptUserForNumber("Write how many disposable cups you want to add: ")
        inventory = inventory.adjust(
            Inventory(
                Ingredients(waterMlToAdd, milkMlToAdd, coffeeBeanGramsToAdd),
                disposableCupsToAdd,
                0
            )
        )

    }

    private fun takeCommand(): Unit {
        val dollars = inventory.numberOfDollars
        inventory = inventory.adjust(
            Inventory(
                Ingredients(0, 0, 0),
                0,
                -inventory.numberOfDollars
            )
        )
        println("I gave you \$$dollars")

    }

}

fun main() {
    val coffeeMachine = CoffeeMachine()

    // stage 5 below
    while (true) {
        println()
        coffeeMachine.askSpecialWorkerForCommand()
    }


    // stage 4 below
//    coffeeMachine.inventoryReport().let(::println)
//    println()
//    coffeeMachine.askSpecialWorkerForCommand()

    // stage 3 below
//    coffeeMachine.evaluateIngredientForRequiredCups(
//        coffeeMachine.askUserForIngredientsAvailable(),
//        coffeeMachine.askUserHowManyCoffeeCupsNeeded()
//    ).let(::println)

    // stage 2 below
//    coffeeMachine.ingredientsRequiredReport(coffeeMachine.askUserHowManyCoffeeCupsNeeded()).let(::println)

    // stage 1 below
//    coffeeMachine.stage1()
}