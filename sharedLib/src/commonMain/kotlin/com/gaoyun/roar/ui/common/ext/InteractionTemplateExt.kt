package com.gaoyun.roar.ui.common.ext

import androidx.compose.runtime.Composable
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.bathing_4w
import roar.sharedlib.generated.resources.deworming_3m
import roar.sharedlib.generated.resources.flees_monthly
import roar.sharedlib.generated.resources.grooming_3m
import roar.sharedlib.generated.resources.grooming_6m
import roar.sharedlib.generated.resources.health_check_yearly
import roar.sharedlib.generated.resources.nails_3m
import roar.sharedlib.generated.resources.nails_yearly
import roar.sharedlib.generated.resources.pills_prescribed
import roar.sharedlib.generated.resources.reminder
import roar.sharedlib.generated.resources.vaccine_planned
import roar.sharedlib.generated.resources.vaccine_rabies

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
    val resource = templateNamesMap.getOrElse(name) { Res.string.reminder }
    return stringResource(resource)
}

private val templateNamesMap = mapOf(
    "deworming_3m" to Res.string.deworming_3m,
    "health_check_yearly" to Res.string.health_check_yearly,
    "flees_monthly" to Res.string.flees_monthly,
    "grooming_6m" to Res.string.grooming_6m,
    "grooming_3m" to Res.string.grooming_3m,
    "nails_3m" to Res.string.nails_3m,
    "nails_yearly" to Res.string.nails_yearly,
    "vaccine_rabies" to Res.string.vaccine_rabies,
    "vaccine_planned" to Res.string.vaccine_planned,
    "pills_prescribed" to Res.string.pills_prescribed,
    "bathing_4w" to Res.string.bathing_4w
)