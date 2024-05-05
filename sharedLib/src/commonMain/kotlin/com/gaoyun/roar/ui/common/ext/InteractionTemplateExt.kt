package com.gaoyun.roar.ui.common.ext

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.app_name

/**
 * Retrieves string resource of Template name. If no template name - returns template type string.
 *
 * Currently new template names must be added to the `listOfTemplateNames` to avoid `IllegalStateException`
 * that `stringResource` throws if there is no such resource. Since try-catch operator is prohibited around
 * composable function it is like it is now until better solution will come up.
 */
@Throws(IllegalArgumentException::class)
@Composable
fun InteractionTemplate.getName(): String {
    return stringResource(Res.string.app_name)
//    return if (listOfTemplateNames.contains(name)) {
//        stringResource(getStringByName(name))
//    } else {
//        stringResource(getStringByName(type.toString()))
//    }
}

val listOfTemplateNames = listOf(
    "deworming_3m",
    "health_check_yearly",
    "flees_monthly",
    "grooming_6m",
    "grooming_3m",
    "nails_3m",
    "nails_yearly",
    "vaccine_rabies",
    "vaccine_planned",
    "pills_prescribed",
    "bathing_4w"
)