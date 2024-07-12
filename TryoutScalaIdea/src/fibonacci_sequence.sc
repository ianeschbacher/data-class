
var current = 1
var previous =0
var total = 0
for (x <- 1 to 10) {
  total+=previous
  previous=current
  current=total
  println(current)
}