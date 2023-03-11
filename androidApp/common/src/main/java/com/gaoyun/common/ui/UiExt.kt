package com.gaoyun.common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources

@SuppressLint("DiscouragedApi")
fun Context.getDrawableByName(idRes: String): Int {
    val resources: Resources = resources

    return resources.getIdentifier(
        idRes, "drawable",
        packageName
    )
}

@SuppressLint("DiscouragedApi")
fun Context.getStringByName(idRes: String): Int? {
    val resources: Resources = resources
    val result = resources.getIdentifier(
        idRes, "string",
        packageName
    )
    return if (result != 0) result else null
}