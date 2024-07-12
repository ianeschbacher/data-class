val testString : String = "test"
testString.toUpperCase

def transformString(x: String, f: String => String): String = {
  f(x)
}

transformString("test", x=>x.toUpperCase)