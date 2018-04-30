package examples.c17.counted

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com





class CountedData {
	
  def counter
  def value  
  
  def String toString () {
    def s = "Counted Data: [ " + counter + ", " + value + " ]"
    return s
  }
}
