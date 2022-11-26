package com.stefanpetcu.api.testingharness.hammingsproblem.presentation

import com.stefanpetcu.api.testingharness.hammingsproblem.domain.RegularNumbersDto

data class PlainResponse(
    val responses: List<RegularNumbersDto>
)
