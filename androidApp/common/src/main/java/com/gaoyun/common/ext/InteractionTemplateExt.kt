package com.gaoyun.common.ext

import android.content.Context
import com.gaoyun.common.ui.getStringByName
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate

fun InteractionTemplate.getName(context: Context): String? {
    return (context.getStringByName(name) ?: context.getStringByName(type.toString()))?.let { context.getString(it) }
}