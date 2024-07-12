def squareIt(x: Int) : Int = {
  x*x
}

def cubeIt(x: Int) : Int = x*x*x

println(x = squareIt(2))
println {
  cubeIt(3)
}

def transformInt(x: Int, f: Int => Int): Int = {
  f(x)
}

val result = transformInt(2, cubeIt)

transformInt (10, x=> x / 2)

transformInt(2, x=> {val y = x*2; y*y})