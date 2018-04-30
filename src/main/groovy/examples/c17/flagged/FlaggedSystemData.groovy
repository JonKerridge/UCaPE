package examples.c17.flagged

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com





class FlaggedSystemData extends SystemData {  
	
  def boolean testFlag = false
  
  def String toString() {
    String s
    s = "Flagged System Data: [" + a + ", " + b + ", " + c + ", " + testFlag + "]"
    return s
  }
}
