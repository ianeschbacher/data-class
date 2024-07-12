val captainStuff = ("Picard", "Enterprise-D", "NCC-1781-D")
println(captainStuff)

// One based index
println(captainStuff._1)
println(captainStuff._2)
println(captainStuff._3)
val picardsShip = "Picard" -> "Enterprise-D"
println(picardsShip._2)

val aBunchOfStuff = ("Kirk", 1964, true)

// Lists must be same type
val shipList = List("Enterprise", "Defiant", "Voyager", "Deep Space Nine")

println(shipList(1))

println(shipList.head)
println(shipList.tail)

// map applies to each item in a list
val backwardShips = shipList.map((ship:String) => {ship.reverse})

//val tokenShips = List()
//shipList.map((ship:String) => {ship.split(" ") :: tokenShips})


// reduce combines together... think running total
val numberList = List(1, 2, 3, 4, 5)
val sum = numberList.reduce((x: Int, y: Int) => x+y)

// filterRemoves stuff
val iHateFives = numberList.filter((x: Int) => x != 5)

val iHateThrees = numberList.filter(_ != 3)

val moreNumbers = List(6,7,8)
val lotsOfNumbers = numberList ++ moreNumbers

val reversed = numberList.reverse
val sorted = reversed.sorted
val lotsOfDuplicates = numberList ++ numberList
val distinctValues = lotsOfDuplicates.distinct
val maxValue = numberList.max
val total = numberList.sum
val hasThree = iHateThrees.contains(3)

// MAPS
val shipMap = Map{"Kirk" -> "Enterprise"}