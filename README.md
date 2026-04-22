Welcome to **BHILANI**, an **Agentic Interop SDK Suite** by **Kantini, Chanchali**

Run SDK

    Command 1
    
        kotlinc -Xplugin=libs/kotlin-serialization-compiler-plugin-2.3.20.jar ^
        -cp "libs/kotlinx-coroutines-core-jvm-1.9.0.jar;libs/kotlinx-serialization-core-jvm-1.9.0.jar;libs/kotlinx-serialization-json-jvm-1.9.0.jar" ^
        src/bhilani/interoperability/jvm/JVMSDKit.kt ^
        -include-runtime -d JVMSDKit.jar

    Command 2

        java --enable-native-access=ALL-UNNAMED -Djava.library.path=. ^
        -cp "JVMSDKit.jar;libs/*" ^
        bhilani.interoperability.jvm.JVMSDKitKt

Basic Usage

    package bhilani.interoperability.jvm
    
    import kotlinx.serialization.*
    import kotlinx.serialization.json.*
    
    @Serializable
    data class FilterParams(
        val language: String? = null,
        val integration: String? = null,
        val crates: List<String>? = null,
        val developmentkit: List<String>? = null, 
        val page: String? = null,
        val ids: List<Int>? = null
    )
    
    class JVMSDKit {
    
        external fun fetchInteroperability(url: String, paramsJson: String): String
    
        companion object {
            init {
                System.loadLibrary("interoperability_wrapper_robusta")
            }
        }
    
        fun runDemo() {
    		val url = ""
    		val pageNumber = 1
    
    		val filter = FilterParams(page = pageNumber.toString())
    
    		val response = runCatching {
    			val paramsJson = Json.encodeToString(filter)
    			fetchInteroperability(url, paramsJson)
    		}.fold(
    			onSuccess = { result ->
    				println("Kotlin SDK")
    				result
    			},
    			onFailure = { ex ->
    				"Native Interop Failed: ${ex.message}"
    			}
    		)
    
    		println(response)
    	}
    
    }
    
    fun main() {
        JVMSDKit().runDemo()
    }

Dynamic Usage

    package bhilani.interoperability.jvm
    
    import kotlinx.serialization.*
    import kotlinx.serialization.json.*
    
    @Serializable
    data class Pagination(
        @SerialName("total_pages") val totalPages: Int
    )
    
    @Serializable
    data class SDKItem(
        val title: String
    )
    
    @Serializable
    data class FetchResponse(
        val data: List<SDKItem>,
        val pagination: Pagination
    )
    
    class JVMSDKit {
    
        external fun fetchInteroperability(url: String, paramsJson: String): String
    
        companion object {
            init {
                System.loadLibrary("interoperability_wrapper_robusta")
            }
            val jsonParser = Json { ignoreUnknownKeys = true }
        }
    
        fun fetchPage(url: String, page: Int): String {
            val params = """{"page": "$page"}"""
            return fetchInteroperability(url, params)
        }
    }
    
    fun main() {
        val sdk = JVMSDKit()
        val url = ""
        
        println("--- Bhilani Interop SDK ---")
    
        // Loop through the requested range
        for (pageNum in 1..5) {
            try {
                val response = sdk.fetchPage(url, pageNum)
                val parsed = JVMSDKit.jsonParser.decodeFromString<FetchResponse>(response)
                
                val totalPages = parsed.pagination.totalPages
    
                if (parsed.data.isEmpty() || pageNum > totalPages) {
                    println("Page $pageNum: Success (No Data - Server has $totalPages pages)")
                } else {
                    println("Page $pageNum: Success")
                    parsed.data.forEach { item ->
                        println("  - Title: ${item.title}")
                    }
                }
            } catch (e: Exception) {
                println("Page $pageNum: Failed (Error: ${e.message})")
            }
        }
    }

Concurrent Usage

    package bhilani.interoperability.jvm
    
    import kotlinx.coroutines.*
    import kotlinx.serialization.*
    import kotlinx.serialization.json.*
    import kotlin.random.Random
    
    @Serializable
    data class Pagination(
        @SerialName("total_pages") val totalPages: Int
    )
    
    @Serializable
    data class SDKItem(
        val title: String
    )
    
    @Serializable
    data class FetchResponse(
        val data: List<SDKItem>,
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
    
        // Concurrent fetch logic
        suspend fun fetchPages(url: String, pageRange: IntRange): List<Result<String>> = coroutineScope {
            pageRange.map { page ->
                async(Dispatchers.IO) {
                    delay(Random.nextLong(50, 251))
                    
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
        
        println("--- Bhilani Interop SDK (Kotlin Concurrency) ---")
    
        // Concurrent request for pages 1 through 5
        val results = sdk.fetchPages(url, 1..5)
    
        results.forEachIndexed { index, result ->
            val pageNum = index + 1
    
            result.onSuccess { res ->
                try {
                    val parsed = JVMSDKit.jsonParser.decodeFromString<FetchResponse>(res)
                    val totalPages = parsed.pagination.totalPages
    
                    if (parsed.data.isEmpty() || pageNum > totalPages) {
                        println("Page $pageNum: Success (No Data - Server has $totalPages pages)")
                    } else {
                        println("Page $pageNum: Success")
                        parsed.data.forEach { item ->
                            println("  - Title: ${item.title}")
                        }
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

First time
<img width="885" height="444" alt="kotlin1" src="https://github.com/user-attachments/assets/1b13c4cc-7a60-4f7b-9e44-359bec8b08dd" />
Second time
<img width="929" height="439" alt="kotlin2" src="https://github.com/user-attachments/assets/d3cc2811-154c-4f45-b9cc-576fa24f44ca" />
Third time
<img width="923" height="441" alt="kotlin3" src="https://github.com/user-attachments/assets/5ff12a68-2e25-44f6-98a4-5955ad612522" />

**🙏 Mata Shabri 🙏**
