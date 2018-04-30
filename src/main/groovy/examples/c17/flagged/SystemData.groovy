package examples.c17.flagged

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com





class SystemData {
	
  def a
  def b
  def c  
  
  def String toString() {
    String s
    s = "System Data: [" + a + ", " + b + ", " + c + "]"
    return s
  }
}
