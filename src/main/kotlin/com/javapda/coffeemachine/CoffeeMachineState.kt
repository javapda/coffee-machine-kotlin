package com.javapda.coffeemachine

enum class CoffeeMachineState(val prompt: String) {
        //    enum class CoffeeMachineState(val prompt:String) {
        CHOOSING_AN_ACTION("Write action (buy, fill, take, remaining, exit): "),
        CHOOSING_TYPE_OF_COFFEE("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino: "),
        FILL(""),
        FILL_WATER("Write how many ml of water you want to add: "),
        FILL_MILK("Write how many ml of milk you want to add: "),
        FILL_COFFEE_BEANS("Write how many grams of coffee beans you want to add: "),
        FILL_DISPOSABLE_CUPS("Write how many disposable cups you want to add: "),
        TAKE_MONEY("")

}
