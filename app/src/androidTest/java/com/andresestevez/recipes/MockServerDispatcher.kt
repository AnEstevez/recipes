package com.andresestevez.recipes

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader

class MockServerDispatcher {
    /**
     * Return ok response from mock server
     */
    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when {
                request.path?.contains("/search.php") == true -> MockResponse().setResponseCode(200).setBody(getJsonContent("response_pork_recipe_search.json"))
                request.path?.contains("/lookup.php?i=53032") == true -> MockResponse().setResponseCode(200).setBody(getJsonContent("response_recipe_by_id_53032.json"))
                request.path?.contains("/lookup.php?i=53037") == true -> MockResponse().setResponseCode(200).setBody(getJsonContent("response_lookup_53037.json"))
                request.path?.contains("/filter.php") == true -> MockResponse().setResponseCode(200).setBody(getJsonContent("response_pork_recipe_search.json"))
                else -> MockResponse().setResponseCode(400)
            }
        }
    }

    /**
     * Return error response from mock server
     */
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
        }
    }

    private fun getJsonContent(fileName: String): String {
        return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
    }
}
