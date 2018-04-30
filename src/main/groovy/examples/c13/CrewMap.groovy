package examples.c13
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*


class CrewMap extends  HashMap<Object, Object> {
	
  def theCrew = new Crew()
  
  def Object put ( Object itsKey, Object itsValue ) {
    theCrew.startWrite()
    super.put ( itsKey, itsValue )
    theCrew.endWrite()
  }
  
  def Object get ( Object itsKey ) {
    theCrew.startRead()
    def result = super.get ( itsKey )
    theCrew.endRead()
    return result
  }

}

  