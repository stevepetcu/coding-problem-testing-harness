package com.stefanpetcu.api.testingharness.hammingsproblem.application

import com.stefanpetcu.api.testingharness.hammingsproblem.domain.PerformanceCharacteristicDto
import com.stefanpetcu.api.testingharness.hammingsproblem.domain.RegularNumbersDto

sealed interface HammingsProblemTestingService {
    fun getPlainResponsesForRequestSeriesBlocking(firstUntil: Int, vararg optUntils: Int): List<RegularNumbersDto>
    fun getPlainResponsesForRequestSeriesAsync(firstUntil: Int, vararg optUntils: Int): List<RegularNumbersDto>
    fun getPerformanceResponsesForRequestSeries(
        firstUntil: Int,
        vararg optUntils: Int
    ): List<PerformanceCharacteristicDto>
}
