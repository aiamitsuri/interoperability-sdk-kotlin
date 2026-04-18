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
