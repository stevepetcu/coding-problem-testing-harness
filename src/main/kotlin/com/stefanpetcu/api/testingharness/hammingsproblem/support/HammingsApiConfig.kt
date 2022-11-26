package com.stefanpetcu.api.testingharness.hammingsproblem.support

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("hammings-problem-api")
@ConstructorBinding
data class HammingsApiConfig(val baseUrl: String, val port: Int)
