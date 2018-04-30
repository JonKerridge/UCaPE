package examples.c12.fork

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

def PHILOSOPHERS = 5

def lefts = Channel.one2oneArray(PHILOSOPHERS)
def rights = Channel.one2oneArray(PHILOSOPHERS)
def enters = Channel.one2oneArray(PHILOSOPHERS)
def exits = Channel.one2oneArray(PHILOSOPHERS)

def entersList = new ChannelInputList(enters)
def exitsList = new ChannelInputList(exits)

def butler = new LazyButler ( enters: entersList, exits: exitsList )

def philosophers = ( 0 ..< PHILOSOPHERS).collect { i ->  
         return new Philosopher ( leftFork: lefts[i].out(), 
                                   rightFork: rights[i].out(), 
                                   enter: enters[i].out(), 
                                   exit: exits[i].out(), id:i ) }

def forks = ( 0 ..< PHILOSOPHERS).collect { i ->  
               return new Fork ( left: lefts[i].in(), 
                                  right: rights[(i+1)%PHILOSOPHERS].in() ) }

def processList = philosophers + forks + butler

new PAR ( processList ).run()                               
                               
