package examples.c24.SingleMachine.methods

class defs {

	def static processLine (line){
		def words = []
		words = line.tokenize(' ')
		return words
	}
	
	def static endPunctuationList =[',','.',';',':','?','!', '\'', '"', '_', '}']
	
	def static startPunctuationList = ['\'' ,'"', '_', '\t', '{']

	def static removePunctuation(w) {
		def ew = w
		def rw
		def len = w.size()
		if ( len == 1 ) 
			rw = w
		else {
			def lastCh = w.substring(len-1, len)
			while (endPunctuationList.contains(lastCh)){
				ew = w.substring(0, len-1)
				len = len - 1
				lastCh = ew.substring(len-1, len)
			}
			def firstCh = ew.substring(0, 1)
			if ( startPunctuationList.contains(firstCh) ) {
				rw = w.substring(1, len)
			}
			else {
				rw = ew
			}
		}
		return rw
	}
	
		def static charSum(w) {
		def sum = 0
		def wbuff = new StringBuffer(w)
		def len = wbuff.length()
		for ( i in 0..< len) {
			sum = sum + (int) wbuff[i]
		}
		return sum
	}
	
    def static sequencer (N, baseList, outList){
        def seqLength = baseList.size()
        for (gl in 0..seqLength-N) {
            def int partSum = 0
            for ( i in 0..< N) partSum = partSum + baseList[gl + i]
            outList << partSum
        }       
    }
    
    def static multiSequencer (N, n, baseList, outList, lastBlock){
        def seqLength = baseList.size()
        def limit = seqLength - N
        if (lastBlock) limit = limit + (N - n)
        for (gl in 0..limit) {
            def int partSum = 0
            if ( n == 1) partSum = baseList[gl]
            else for ( i in 0..< n) partSum = partSum + baseList[gl + i]
            outList << partSum
        }       
    }
    
	def static extractEqualValues (maxLength, startIndex, sequenceValues, equalityMap){
		def index = 0
		def indexList = []
		for ( v in sequenceValues){
			indexList = equalityMap.get (v, [])
			indexList << (index + startIndex)
			equalityMap.put (v, indexList)
			index = index + 1
		}
	}
	
	def static extractConcordance (equalMap, N, startIndex, words, minSeqLen, printWriter) 	{
		def sequenceValues = equalMap.keySet()
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
			wordMap.each { 	concordanceEntry = " "
							if (it.value.size() >= minSeqLen) {
								concordanceEntry = concordanceEntry + it.key + ", "
								concordanceEntry = concordanceEntry + it.value.size() + ", "
								concordanceEntry = concordanceEntry + it.value
								printWriter.println "$concordanceEntry"
							}
			}
		}	
	}	

	def static extractUniqueSequences (equalMap, N, startIndex, words, equalWordMap) {
		def sequenceValues = equalMap.keySet()
		//println "N:$N, SV: $sequenceValues"
		def wordKeyList = []
		def wordMap = [:]
		def indexList = []
		def wordMapEntry = []
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
			equalWordMap.put(sv, wordMap)
			//println "WordMap for $sv is $wordMap"
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


