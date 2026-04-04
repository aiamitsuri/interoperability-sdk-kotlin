# interoperability-sdk-kotlin

Run
1. kotlinc -Xplugin=libs/kotlin-serialization-compiler-plugin-2.3.20.jar ^
-cp "libs/kotlinx-coroutines-core-jvm-1.9.0.jar;libs/kotlinx-serialization-core-jvm-1.9.0.jar;libs/kotlinx-serialization-json-jvm-1.9.0.jar" ^
src/bhilani/interoperability/jvm/JVMSDKit.kt ^
-include-runtime -d JVMSDKit.jar

2. java --enable-native-access=ALL-UNNAMED -Djava.library.path=. ^
-cp "JVMSDKit.jar;libs/*" ^
bhilani.interoperability.jvm.JVMSDKitKt
