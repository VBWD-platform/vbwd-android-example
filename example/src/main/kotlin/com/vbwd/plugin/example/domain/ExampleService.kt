package com.vbwd.plugin.example.domain

import com.vbwd.core.networking.ApiClient
import com.vbwd.core.networking.EmptyResponse
import com.vbwd.core.networking.get
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

/**
 * Abstraction for the Example plugin's backend interactions (DIP). Depends on
 * the [ApiClient] interface, not a concrete HTTP client. Interface segregation:
 * only the methods this plugin needs are declared. Port of the iOS
 * `ExampleServiceProtocol`.
 */
interface ExampleService {
    suspend fun fetchPlans(): Boolean
}

/** Default impl using the SDK's shared API client. */
class DefaultExampleService(private val api: ApiClient) : ExampleService {
    /**
     * Reads tariff plans — demonstrates a plugin making an API call through the
     * SDK's shared client. Returns whether the backend responded. Best-effort:
     * any failure is reported as "no response" (cancellation still propagates).
     */
    override suspend fun fetchPlans(): Boolean =
        runCatching {
            api.get<EmptyResponse>("/tarif-plans/")
            true
        }.getOrElse {
            currentCoroutineContext().ensureActive()
            false
        }
}
