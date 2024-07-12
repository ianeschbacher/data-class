//Flow control

// If / else

if (1 > 3) println("Impossible!") else println("The world makes sense")

val number = 3
number match {
  case 1 => println("One")
  case 2 => println("Two")
  case 3 => println("Three")
  case _ => println("Something else")
}

for (x <- 1 to 4) {
  val squared = x * x
  println(squared)
}

var x = 10
while (x>=0) {
  println(x)
  x-=1
}