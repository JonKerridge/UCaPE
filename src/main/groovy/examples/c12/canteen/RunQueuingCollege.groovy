package examples.c12.canteen
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import jcsp.userIO.*

def philosophers = Ask.Int ("Number of Philosophers (>1)? ", 2, 9)
 
def service = Channel.any2one ()
def deliver = Channel.one2any ()
def supply = Channel.one2one ()
    
def philosopherList = (0 .. (philosophers-1) ).collect{
              i -> return new Philosopher( philosopherId: i, 
                                           service: service.out(), 
                                           deliver: deliver.in())
              }
    
def processList = [ new QueuingServery ( service:service.in(), 
                                         deliver:deliver.out(), 
                                         supply:supply.in()),
                    new Kitchen (supply:supply.out())
                  ]

processList = processList +  philosopherList
   
new PAR ( processList ).run()     