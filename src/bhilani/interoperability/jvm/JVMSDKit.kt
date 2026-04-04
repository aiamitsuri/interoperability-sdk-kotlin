//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji

package bhilani.interoperability.jvm

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.random.Random

/**
 * Data models for type-safe JSON parsing
 */
@Serializable
data class Pagination(
    @SerialName("total_pages") val totalPages: Int
)

@Serializable
data class FetchResponse(
    val data: List<JsonElement>,
    val pagination: Pagination
)

class JVMSDKit {
    private external fun fetchInteroperability(url: String, paramsJson: String): String

    companion object {
        init {
            System.loadLibrary("interoperability_wrapper_robusta")
        }

        val jsonParser = Json { ignoreUnknownKeys = true }
    }

    /**
     * Fetches multiple pages in parallel with staggering and timeouts
     */
    suspend fun fetchPages(url: String, pageRange: IntRange): List<Result<String>> = coroutineScope {
        pageRange.map { page ->

            delay(Random.nextLong(50, 251))
            
            async(Dispatchers.IO) {
                runCatching {
                    withTimeout(5000L) {
                        fetchInteroperability(url, """{"page": "$page"}""")
                    }
                }
            }
        }.awaitAll()
    }
}

suspend fun main() {
    val sdk = JVMSDKit()
    val url = ""
    
    println("--- Bhilani Interop SDK ---")

    val results = sdk.fetchPages(url, 1..5)

    results.forEachIndexed { index, result ->
        val pageNum = index + 1
        result.onSuccess { res ->
            try {

                // Parse JSON string into FetchResponse object
                val parsed = JVMSDKit.jsonParser.decodeFromString<FetchResponse>(res)
                
                val isEmpty = parsed.data.isEmpty()
                val totalPages = parsed.pagination.totalPages

                if (isEmpty) {
                    println("Page $pageNum: Success (No Data - Server has $totalPages pages)")
                } else {
                    println("Page $pageNum: Success (Found ${parsed.data.size} items)")
                }
            } catch (e: Exception) {
                println("Page $pageNum: Success (JSON Parsing Failed: ${e.message})")
            }
        }
        result.onFailure { error ->
            println("Page $pageNum: Failed (${error.message})")
        }
    }
}

//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji
//ramramjiramramjuramramji
//ramramjiramramjuramramji
//ramramjiramramjiramramji