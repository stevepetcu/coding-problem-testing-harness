package com.stefanpetcu.api.testingharness.hammingsproblem.presentation

import com.stefanpetcu.api.testingharness.hammingsproblem.domain.PerformanceCharacteristicDto

data class PerformanceCharacteristicsResponse(
    val responses: List<PerformanceCharacteristicDto>
)
