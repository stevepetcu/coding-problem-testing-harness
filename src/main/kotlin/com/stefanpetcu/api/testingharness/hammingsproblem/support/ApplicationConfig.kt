package com.stefanpetcu.api.testingharness.hammingsproblem.support

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ReflectionUtils
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class ApplicationConfig {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.stefanpetcu.api.testingharness.hammingsproblem.presentation"))
            .paths(PathSelectors.any())
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Testing Harness Application")
            .description("This is a testing harness for the Full Binary Tree, Employee Bonuses and Hamming's Problem APIs.")
            .version("0.0.1-SNAPSHOT")
            .license("GNU General Public License 3")
            .licenseUrl("https://www.gnu.org/licenses/gpl-3.0.en.html")
            .contact(Contact("Stefan-Daniel Petcu", "https://stefanpetcu.com", "hello@stefanpetcu.com"))
            .build()
    }

    // Fixme: Workaround for Springfox's Swagger to work with Spring Boot 2.6:
    //  (see https://github.com/springfox/springfox/issues/3462)
    @Bean
    fun springfoxHandlerProviderBeanPostProcessor(): BeanPostProcessor {
        return object : BeanPostProcessor {
            @Throws(BeansException::class)
            override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
                if (bean is WebMvcRequestHandlerProvider || bean is WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean))
                }
                return bean
            }

            private fun <T : RequestMappingInfoHandlerMapping?> customizeSpringfoxHandlerMappings(mappings: MutableList<T>) {
                val copy = mappings.filter { mapping -> mapping?.patternParser == null }
                mappings.clear()
                mappings.addAll(copy)
            }

            @Suppress("UNCHECKED_CAST")
            private fun getHandlerMappings(bean: Any): MutableList<RequestMappingInfoHandlerMapping> {
                return try {
                    val field = ReflectionUtils.findField(bean.javaClass, "handlerMappings")
                    field?.setAccessible(true)
                    field?.get(bean) as MutableList<RequestMappingInfoHandlerMapping>
                } catch (e: Exception) {
                    throw IllegalStateException(e)
                }
            }
        }
    }
}
