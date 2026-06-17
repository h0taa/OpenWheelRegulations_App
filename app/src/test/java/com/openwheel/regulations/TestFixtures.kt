package com.openwheel.regulations

import com.openwheel.regulations.data.model.RegulationYear
import com.openwheel.regulations.data.model.VisualChange

/** Small builder so tests don't repeat the long constructor. */
fun sampleYear(
    year: Int,
    eraName: String = "Era $year",
    headline: String = "Headline $year",
    keyChanges: List<String> = listOf("Change A", "Change B"),
    isMajorChange: Boolean = false,
    visualComponents: List<VisualChange> = emptyList()
) = RegulationYear(
    year = year,
    eraName = eraName,
    headline = headline,
    keyChanges = keyChanges,
    carDimensions = "dims",
    powerUnit = "pu",
    aerodynamicRules = "aero",
    tyreRegulations = "tyres",
    notableImpact = "impact",
    funFacts = listOf("Fact 1", "Fact 2"),
    isMajorChange = isMajorChange,
    visualComponents = visualComponents
)
