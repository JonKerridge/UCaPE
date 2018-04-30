package examples.c24.SingleMachine.methods

class defsWithComments {

	/**
	 * processLine takes a line of text 
	 * which is split into words using tokenize(' ')
	 * 
	 * @param line a line that has been read from a file
	 * @return list of words containing each word in the line
	 */
	def static processLine (line){
		def words = []
		words = line.tokenize(' ')
		return words
	}
	
	/**
	 * endPunctuationList the list of punctuation symbols 
	 * that can be removed from the ends of words
	 */
	def static endPunctuationList =[',','.',';',':','?','!', '\'', '"', '_', '}']
	
	/**
	 * startPunctuationList the list of punctuation symbols 
	 * that can be removed from the start of words
	 */
	def static startPunctuationList = ['\'' ,'"', '_', '\t', '{']
	/**
	 * 
	 * removePunctuation removes any punctuation characters 
	 * from the start and end of a word
	 * @param w String containing the word to be processed
	 * @return String rw containing the input word less any punctuation symbols
	 * 
	 */
	def static removePunctuation(w) {
		def ew = w
		def rw
		def len = w.size()
		if ( len == 1 ) 
			rw = w
		else {
			def lastCh = w.substring(len-1, len)
			//println "lastCh  is $lastCh"
			/*
			if ( endPunctuationList.contains(lastCh) ) {
				ew = w.substring(0, len-1)
				//println "special: $rw and with $lastCh"
			}
			else {
				ew = w
				//println "normal: $rw"
			}
			*/
			while (endPunctuationList.contains(lastCh)){
				ew = w.substring(0, len-1)
				len = len - 1
//				if (len == 1) break
				lastCh = ew.substring(len-1, len)
			}
			def firstCh = ew.substring(0, 1)
			if ( startPunctuationList.contains(firstCh) ) {
				rw = w.substring(1, len)
				//println "special: $rw and with $lastCh"
			}
			else {
				rw = ew
				//println "normal: $rw"
			}
		}
		return rw
	}
	
	
	/**
	 * charSum transforms a word into a single integer based upon the
	 * sum of the ASCII characters that make up the word 
	 * 
	 * @param w String containing word to be transformed into an integer
	 * @return int containing the integer equivalent of word
	 */
	def static charSum(w) {
		def sum = 0
		def wbuff = new StringBuffer(w)
		def len = wbuff.length()
		for ( i in 0..< len) {
			//def v = (int) wbuff[i]
			sum = sum + (int) wbuff[i]
			//print " ${wbuff[i]} is $v "
		}
		//println "$w is $sum"
		return sum
	}
	
	 
	/**
	 * sequencer takes a list of numeric word values and returns
	 * a list containing the sum of sequences of N numbers
	 * *removed in forming the sequence sum each word is multiplied by a prime number
	 * *removed in an attempt to ensure that each sequence is unique
	 * 
     * @param n the sequence length being generated
	 * @param baseList list of numbers to be sequenced into groups of N
	 * @param outList list of numbers to be generated
	 */
	def static sequencer (n, baseList, outList){
		def seqLength = baseList.size()
		for (gl in 0..seqLength-n) {
			def int partSum = 0
			for ( i in 0..< n) partSum = partSum + baseList[gl + i]
			outList << partSum
		}		
	}
	
	/**
	 * extractEqualValues searches the list of sequenceValues for equal valued entries 
	 * and constructs a map comprising the sequence Value and a list of the indexes
	 * where the value was found.  Every sequenceValue will have an entry which 
	 * contains at least one index where the value was found.
	 * 
	 * NOTE:
	 * 
	 * the sequenceValue may correspond to different word sequences 
	 * because different sequences of words may give rise to the same value
	 * 
	 * see extractUniqueSequences which determines the different word sequences
	 * corresponding to each sequenceValues and then associates these with the 
	 * indexes where they occur.
	 * 
	 * @param maxLength the length of the sequence to be searched
	 * @param startIndex the initial offset of this sequence relative to the start of the text
	 * @param sequenceValues the list of values to be searched
	 * @param equalityMap a map comprising a sequenceValue as key with a list of indexes where that value is found as the entry
	 */
	def static extractEqualValues (maxLength, startIndex, sequenceValues, equalityMap){
		//println "eeV: $sequenceValues"
//		def equalityMap = new TreeMap()		// should be a tree map
		def index = 0
		def indexList = []
		for ( v in sequenceValues){
			/*
			if (equalityMap.containsKey(v)) {
				indexList = equalityMap.get (v)
				indexList << (index + startIndex)
				equalityMap.put (v, indexList)
			}
			else {
				equalityMap.put (v, [(index + startIndex)] )
			}
			*/
			indexList = equalityMap.get (v, [])
			indexList << (index + startIndex)
			equalityMap.put (v, indexList)
			index = index + 1
		}
		/*
		if ( sequenceValues != []){
			for ( x in 0..maxLength-1){			
				if ( ! equalityMap.containsKey(sequenceValues[x])){
					def subscriptList = []
					subscriptList << (x + startIndex)
					for ( y in x+1..maxLength){
						if (sequenceValues[x] == sequenceValues[y]) subscriptList << (y + startIndex)
					}
					equalityMap.put (sequenceValues[x], subscriptList)
				}
			}
		}
		*/
		//println " eev: $equalityMap"
//		return equalityMap
	}
	
	/**
	 * extractUniqueSequences takes an equalityMap produced by extractEqualValues
	 * and determines the word sequences that correspond to each sequenceValue and the
	 * indexes where these exist in the block.
	 * 
	 * The method returns a map comprising a key of sequenceValue, with an entry that
	 * is itself a map comprising the corresponding words and the indexes where these were found
	 * 
	 * @param equalMap the map of sequenceValues and the list of indexes where those value occur in block
	 * @param N the word sequence length
	 * @param startIndex the start index of this block
	 * @param words the words in the block
	 * @param equalWordMap map of words and the indexes where they were found
	 */
	def static extractUniqueSequences (equalMap, N, startIndex, words, equalWordMap) {
		def sequenceValues = equalMap.keySet()
		println "N:$N, SV: $sequenceValues"
		def wordKeyList = []
		def wordMap = [:]
		def indexList = []
		def wordMapEntry = []
		for ( sv in sequenceValues){
			indexList = equalMap.get(sv)	// indexList values are offset by startIndex
			println "Index List for $sv is $indexList"
			wordMap = [:]
			for ( il in indexList){
				wordKeyList = []
				for ( n in 0..(N-1)) wordKeyList << words[il-startIndex + n]
				wordMapEntry = wordMap.get (wordKeyList, [])
				wordMapEntry << il
				wordMap.put (wordKeyList, wordMapEntry)
			}
			equalWordMap.put(sv, wordMap)
			println "WordMap for $sv is $wordMap"
		}
	}
	
	def static extractConcordance (equalMap, N, startIndex, words, minSeqLen, printWriter) 	{
		def sequenceValues = equalMap.keySet()
		//println "N: $N, sequenceValues: $sequenceValues"
		def wordKeyList = []
		def wordMap = [:]
		def indexList = []
		def wordMapEntry = []
		def concordanceEntry = " "		
		for ( sv in sequenceValues){
			indexList = equalMap.get(sv)	// indexList values are offset by startIndex
			//println "Index List for $sv is $indexList"
			wordMap = [:]
			for ( il in indexList){
				wordKeyList = []
				for ( n in 0..(N-1)) wordKeyList << words[il-startIndex + n]
				wordMapEntry = wordMap.get (wordKeyList, [])
				wordMapEntry << il
				wordMap.put (wordKeyList, wordMapEntry)
			}
			// at this point wordMap contains for N = 2
			// [[w,w]:[i,i,i,i], [w,w]:[i,i,i,i], ...] corresponding to sv
			wordMap.each { //println "key: ${it.key}, value ${it.value}"
							concordanceEntry = " "
							if (it.value.size() >= minSeqLen) {
								concordanceEntry = concordanceEntry + it.key + ", "
								concordanceEntry = concordanceEntry + it.value.size() + ", "
								concordanceEntry = concordanceEntry + it.value
								printWriter.println "$concordanceEntry"
							}
			}
		}	
	}
  
  def static int sizeof(Object obj) throws IOException {
    
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
    
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
    
        return byteOutputStream.toByteArray().length;
    }
 
}