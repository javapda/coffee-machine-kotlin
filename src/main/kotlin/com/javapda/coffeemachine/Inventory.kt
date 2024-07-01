package com.javapda.coffeemachine

data class Inventory(
    val ingredients: Ingredients = Ingredients(),
    val numberOfDisposableCups: Int = 0,
    val numberOfDollars: Int = 0
) {
    fun adjust(margin: Inventory): Inventory {
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

    fun inventoryReport(theInventory: Inventory = this): String {
        return """The coffee machine has:
${theInventory.ingredients.waterMl} ml of water
${theInventory.ingredients.milkMl} ml of milk
${theInventory.ingredients.coffeeBeanGrams} g of coffee beans
${theInventory.numberOfDisposableCups} disposable cups
${'$'}${theInventory.numberOfDollars} of money"""
    }

}
