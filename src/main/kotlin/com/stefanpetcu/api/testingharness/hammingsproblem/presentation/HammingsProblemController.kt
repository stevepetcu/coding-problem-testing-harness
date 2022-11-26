package com.stefanpetcu.api.testingharness.hammingsproblem.presentation

import com.stefanpetcu.api.testingharness.hammingsproblem.application.HammingsProblemTestingServiceImpl
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("hamming-test")
@Api(
    produces = "application/json"
)
class HammingsProblemController(private val hammingsProblemTestingService: HammingsProblemTestingServiceImpl) {
    @GetMapping("/numbers/plain")
    fun plainTestRegularNumbers(
        @RequestParam(
            "until",
            required = false,
            defaultValue = "1,2,3,5,10"
        ) untilList: List<Int>
    ): PlainResponse {
        return PlainResponse(
            hammingsProblemTestingService.getPlainResponsesForRequestSeriesBlocking(
                untilList.first(),
                *untilList.subList(1, untilList.size).toIntArray()
            )
        )
    }

    @GetMapping("/numbers/plain-async")
    fun plainTestRegularNumbersAsync(
        @RequestParam(
            "until",
            required = false,
            defaultValue = "1,2,3,5,10"
        ) untilList: List<Int>
    ): PlainResponse {
        return PlainResponse(
            hammingsProblemTestingService.getPlainResponsesForRequestSeriesAsync(
                untilList.first(),
                *untilList.subList(1, untilList.size).toIntArray()
            )
        )
    }

    @GetMapping("/numbers/performance")
    fun performanceTestRegularNumbers(
        @RequestParam(
            "until",
            required = false,
            defaultValue = "1,2,3,5,10"
        ) untilList: List<Int>
    ): PerformanceCharacteristicsResponse {
        return PerformanceCharacteristicsResponse(
            hammingsProblemTestingService.getPerformanceResponsesForRequestSeries(
                untilList.first(),
                *untilList.subList(1, untilList.size).toIntArray()
            )
        )
    }
}
