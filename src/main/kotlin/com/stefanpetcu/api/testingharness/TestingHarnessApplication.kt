package com.stefanpetcu.api.testingharness

import com.stefanpetcu.api.testingharness.hammingsproblem.support.HammingsApiConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(HammingsApiConfig::class)
class TestingHarnessApplication

fun main(args: Array<String>) {
    runApplication<TestingHarnessApplication>(*args)
}
