package examples.c05;
   
    
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com




class ScaledData implements Serializable {
	
  def int original
  def int scaled 
   
  def String toString () {
	  def s = " " + original + "\t\t" + scaled
	  return s 
  }	
}
