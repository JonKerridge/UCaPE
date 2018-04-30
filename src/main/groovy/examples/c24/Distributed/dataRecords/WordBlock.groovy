package examples.c24.Distributed.dataRecords

import java.io.Serializable;

class WordBlock implements Serializable {
  int startSubscript = 0
  def last = false
  def words = []
}
