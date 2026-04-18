# interoperability-sdk-kotlin

# Run SDK

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
    
    class JVMSDKit {
    
        external fun fetchInteroperability(url: String, paramsJson: String): String
    
        companion object {
            init {
                System.loadLibrary("interoperability_wrapper_robusta")
            }
        }
    
        fun runDemo() {
            val url = ""
            val params = """{"page": "1"}"""
    
            println("Kotlin SDK")
    
            val response = fetchInteroperability(url, params)
    
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
