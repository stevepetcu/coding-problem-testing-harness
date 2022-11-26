package com.stefanpetcu.api.testingharness.hammingsproblem.pacts

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.PactSpecVersion
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.github.kittinunf.fuel.Fuel
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "RegularNumbersProvider", port = "8081", pactVersion = PactSpecVersion.V3)
class HammingsProblemConsumerPactTest {
    @BeforeEach
    fun setUp(mockServer: MockServer) {
        assertThat(mockServer, `is`(notNullValue()))
    }

    @Pact(provider = "RegularNumbersProvider", consumer = "TestingHarness")
    fun noQueryParamsRegularNumbersResponse(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .given("test  state")
            .uponReceiving("A Regular Numbers Request without any parameters")
            .path("/hamming/numbers")
            .willRespondWith()
            .status(200)
            .body(
                PactDslJsonBody()
                    .array("regularNumbers")
                    .integerType(1)
                    .integerType(2)
                    .integerType(3)
                    .integerType(4)
                    .integerType(5)
                    .integerType(6)
                    .integerType(8)
                    .integerType(9)
                    .integerType(10)
                    .integerType(12)
                    .closeArray()!!
            )
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "noQueryParamsRegularNumbersResponse")
    fun testNoQueryParametersHammingsApiResponse(mockServer: MockServer) {
        val (_, response, result) = Fuel.get(
            "${mockServer.getUrl()}/hamming/numbers"
        ).responseString()

        assertThat(response.statusCode, `is`(equalTo(200)))
        assertThat(result.get(), `is`(equalTo("{\"regularNumbers\":[1,2,3,4,5,6,8,9,10,12]}")))
    }
}
