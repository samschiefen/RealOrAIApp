package com.example.realorai.data

import android.content.Context

fun loadImagesFromAssets(context: Context): List<Pair<String, Boolean>> {
    val assetManager = context.assets

    // Load real and fake image lists from assets
    val realImages = assetManager.list("real")?.map { "real/$it" } ?: emptyList()
    val fakeImages = assetManager.list("fake")?.map { "fake/$it" } ?: emptyList()

    // Select 25 random images from each category
    val selectedReal = realImages.shuffled().take(25).map { it to true }  // Real = true
    val selectedFake = fakeImages.shuffled().take(25).map { it to false } // Fake = false

    // Combine and shuffle all images
    return (selectedReal + selectedFake).shuffled()
}