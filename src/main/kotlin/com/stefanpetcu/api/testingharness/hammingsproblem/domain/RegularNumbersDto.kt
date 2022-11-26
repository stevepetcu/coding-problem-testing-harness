package com.stefanpetcu.api.testingharness.hammingsproblem.domain

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.math.BigInteger

data class RegularNumbersDto(val regularNumbers: List<BigInteger>) {
    class Deserializer : ResponseDeserializable<RegularNumbersDto> {
        override fun deserialize(content: String): RegularNumbersDto =
            Gson().fromJson(content, RegularNumbersDto::class.java)
    }
}
