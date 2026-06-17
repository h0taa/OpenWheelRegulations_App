package com.openwheel.regulations.data.model

import kotlinx.serialization.Serializable

/**
 * Describes a single before/after change for the visual comparison canvas
 * shown on major-regulation-change years.
 *
 * @param component which part of the car the annotation arrow points to.
 *        One of: "frontWing", "rearWing", "floor", "engine", "tyre", "body".
 * @param category  the chip grouping shown below the comparison
 *        (e.g. "Power Unit", "Aerodynamics", "Chassis", "Tyres", "Sporting").
 * @param label     short text shown on the annotation arrow.
 * @param previousValue description drawn against the grey "previous era" car.
 * @param newValue      description drawn against the red "new era" car.
 */
@Serializable
data class VisualChange(
    val component: String,
    val category: String,
    val label: String,
    val previousValue: String,
    val newValue: String
)
