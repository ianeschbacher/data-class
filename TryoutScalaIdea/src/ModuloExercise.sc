var testData = List(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)

val divisibleBy3 = testData.filter(_ %3 == 0)

println {
  divisibleBy3
}


//val divisibleBy3 = testData.map((x:Int) => {if (x % 0 ==3) print(x)})
