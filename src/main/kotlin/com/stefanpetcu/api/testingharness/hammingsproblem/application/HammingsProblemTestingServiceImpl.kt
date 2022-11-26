package com.stefanpetcu.api.testingharness.hammingsproblem.application

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.requests.CancellableRequest
import com.stefanpetcu.api.testingharness.hammingsproblem.domain.PerformanceCharacteristicDto
import com.stefanpetcu.api.testingharness.hammingsproblem.domain.PerformanceRequestParameters
import com.stefanpetcu.api.testingharness.hammingsproblem.domain.RegularNumbersDto
import com.stefanpetcu.api.testingharness.hammingsproblem.support.DateUtils
import com.stefanpetcu.api.testingharness.hammingsproblem.support.HammingsApiConfig
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.system.measureTimeMillis

@Service
class HammingsProblemTestingServiceImpl(private val config: HammingsApiConfig) : HammingsProblemTestingService {
    override fun getPlainResponsesForRequestSeriesBlocking(
        firstUntil: Int,
        vararg optUntils: Int
    ): List<RegularNumbersDto> {
        val responseList = mutableListOf<RegularNumbersDto>()

        /**
         * This is for when you just want to get a string response back:
         * val (_, _, result) = Fuel.get(
         * "http://localhost:8081/hamming/numbers",
         * listOf("until" to firstUntil)
         * ).responseString()
         * val (payload, _) = result

         * responseList.add(payload.orEmpty())

         * optUntils.forEach {
         * val (_, _, optResult) = Fuel.get(
         * "http://localhost:8081/hamming/numbers",
         * listOf("until" to it)
         * ).responseString()
         * val (optPayload, _) = optResult

         * responseList.add(optPayload.orEmpty())
         * }
         */

        responseList.add(plainRequestBlocking(firstUntil))

        optUntils.forEach {
            responseList.add(plainRequestBlocking(it))
        }

        return responseList
    }

    private fun plainRequestBlocking(until: Int): RegularNumbersDto {
        val requestId = UUID.randomUUID()
        val (_, _, result) = Fuel.get(
            "${config.baseUrl}:${config.port}/hamming/numbers",
            listOf("until" to until)
        ).responseProgress { readBytes, totalBytes ->
            val progress = readBytes.toFloat() / totalBytes.toFloat() * 100
            println("Request id: $requestId; Bytes downloaded $readBytes / $totalBytes ($progress %)")
        }.responseObject(RegularNumbersDto.Deserializer())
        val (payload, _) = result

        return payload ?: RegularNumbersDto(emptyList())
    }

    override fun getPlainResponsesForRequestSeriesAsync(
        firstUntil: Int,
        vararg optUntils: Int
    ): List<RegularNumbersDto> {
        val requestList = mutableListOf<CancellableRequest>()
        val responseList = mutableListOf<RegularNumbersDto>()

        requestList.add(plainRequestAsync(firstUntil, responseList))

        optUntils.forEach {
            requestList.add(plainRequestAsync(it, responseList))
        }

        requestList.forEach {
            it.join()
            println("Finishing a request at ${DateUtils.currentDateTime()}")
        }

        return responseList
    }

    private fun plainRequestAsync(until: Int, responseList: MutableList<RegularNumbersDto>): CancellableRequest {
        val requestId = UUID.randomUUID()
        val httpAsync = Fuel.get(
            "${config.baseUrl}:${config.port}/hamming/numbers",
            listOf("until" to until)
        ).responseProgress { readBytes, _ ->
//                    To calculate progress, source API must include a Content-Length header (for totalBytes),
//                    which is not trivial, so I'll exclude the totalBytes:
//                    val progress = readBytes.toFloat() / totalBytes.toFloat() * 100
            println("Request id: $requestId; Bytes downloaded: $readBytes; at ${DateUtils.currentDateTime()}")
        }.responseObject(RegularNumbersDto.Deserializer()) { _, _, result ->
            val (regularNumbers, err) = result

            if (null != err) {
                println(err.localizedMessage)
            } else {
                responseList.add(regularNumbers ?: RegularNumbersDto(emptyList()))
            }
        }

        return httpAsync
    }

    override fun getPerformanceResponsesForRequestSeries(
        firstUntil: Int,
        vararg optUntils: Int
    ): List<PerformanceCharacteristicDto> {
        val responseList = mutableListOf<PerformanceCharacteristicDto>()

        val (timeInMillis, regularNumbers) = performanceMeasuredRequestBlocking(firstUntil)

        responseList.add(
            PerformanceCharacteristicDto(PerformanceRequestParameters(firstUntil), regularNumbers, timeInMillis)
        )

        optUntils.forEach {
            val (optTimeInMillis, optRegularNumbers) = performanceMeasuredRequestBlocking(it)

            responseList.add(
                PerformanceCharacteristicDto(PerformanceRequestParameters(it), optRegularNumbers, optTimeInMillis)
            )
        }

        return responseList
    }

    private fun performanceMeasuredRequestBlocking(until: Int): Pair<Long, RegularNumbersDto> {
        val requestId = UUID.randomUUID()
        val regularNumbers: RegularNumbersDto
        val timeInMillis = measureTimeMillis {
            val (_, _, result) = Fuel.get(
                "${config.baseUrl}:${config.port}/hamming/numbers",
                listOf("until" to until)
            ).timeoutRead(90000)
                .responseProgress { readBytes, _ ->
                    println("Request id: $requestId; Bytes downloaded: $readBytes")
                }
                .responseObject(RegularNumbersDto.Deserializer())
            val (payload, _) = result
            regularNumbers = payload ?: RegularNumbersDto(emptyList())
        }

        return Pair(timeInMillis, regularNumbers)
    }
}
