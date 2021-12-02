package com.andresestevez.recipes.di

import com.andresestevez.domain.Recipe

val mockedRecipe = Recipe(
    "recipe01",
    "Carne o caldeiro",
    "http://www.cocinayrecetasfaciles.com/files/styles/receta_facebook/public/receta/carne-o-caldeiro.png-1.png",
    "Cortar la carne de ternera en trozos medianos. Ponerla en una cazuela.Añadir el unto y sal. Cubrirla con agua fría y llevar a ebullición.En el momento que comience a hervir disminuir la fuente de calor y cocer a fuego lento hasta que la carne esté tierna, 60/75 minutos aproximadamente.Retirar la carne cocida a otro recipiente con caldo de la cocción para mantenerla caliente.Cocer las patatas, peladas y enteras, en el resto del caldo 15/20′.Colocar la carne, las patatas y un poco de caldo en una fuente.Espolvorear con el pimentón elegido y rociar con aceite de oliva.Servir caliente.",
    "spanish",
    listOf("falda de ternera", "unto", "patatas", "Pimentón", "Aceite de oliva", "sal"),
    listOf("1,5kg", "75g", "12", "al gusto", "al gusto", "al gusto"),
    false,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
)

val defaultFakeRecipes = mutableListOf(
    mockedRecipe.copy("1", "Chocolate Chip Banana Bread", favorite = true),
    mockedRecipe.copy("2", "Chicken Chow Mein"),
    mockedRecipe.copy("3", "Chicken Parmesan"),
    mockedRecipe.copy("4", "French Onion Soup"),
    mockedRecipe.copy("53032", "Tonkatsu pork")
)
