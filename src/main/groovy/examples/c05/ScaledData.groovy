package examples.c05;
   
    
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com




class ScaledData implements Serializable {
	
  int original
  int scaled
   
  def String toString () {
	  def s = " " + original + "\t\t" + scaled
	  return s 
  }	
}
