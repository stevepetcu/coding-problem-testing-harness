package com.stefanpetcu.api.testingharness.hammingsproblem.domain

data class PerformanceRequestParameters(
// if this were to be a "value" instead of a data class, the "until" variable name would not show up in the JSON output!
//    private val until: Int if private, it won't appear in the JSON response output!
    val until: Int
)

data class PerformanceCharacteristicDto(
    val requestParams: PerformanceRequestParameters,
    val response: RegularNumbersDto,
    val msTaken: Long
)
