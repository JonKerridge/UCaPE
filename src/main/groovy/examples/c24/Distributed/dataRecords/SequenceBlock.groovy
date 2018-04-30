package examples.c24.Distributed.dataRecords

class SequenceBlock implements Serializable{
  def startSubscript        // the startSubscript for block
  def words                 // the unpunctuated words
  // each block of words has
  def NSequenceLists = []   // its own 1..N sequence lists
  def equalKeyMapList = []  // and equalKeyMapLists 1..N
  def equalWordMapList = [] // and equalWordMap list 1..N
}
