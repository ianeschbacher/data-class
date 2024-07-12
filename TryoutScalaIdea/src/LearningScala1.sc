//Values are immutable constructs
var hello: String = "Hola!"

// Variables are mutable
var helloThere: String = hello
helloThere = hello + " There!"
println(helloThere)

val immutableHelloThere = hello + " There"
println(immutableHelloThere)

val numberOne: Int = 1
val truth: Boolean = true
val letterA: Char = 'a'
val pi : Double = 3.14159265
val piSinglePrecision: Float = 3.14159265f
val bigNumber: Long = 123456789
val smallNumber: Byte = 127

println("Here is a mess: "+ numberOne+truth+letterA+bigNumber)

println(f"Pi is about $piSinglePrecision%.3f")

val picard: String = "Picard"
val bestCaptain: String = "Picard"

val isBest: Boolean = picard == bestCaptain
println(isBest)
//val isBestRef: Boolean = picard = bestCaptain
//println(isBestRef)