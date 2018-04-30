package examples.c02

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import jcsp.lang.*

class ConsumeHello implements CSProcess {
  
  def ChannelInput inChannel
  
  void run() {
    def first = inChannel.read()
    def second = inChannel.read()
    println "\n${first} ${second}!\n"
  }
}

