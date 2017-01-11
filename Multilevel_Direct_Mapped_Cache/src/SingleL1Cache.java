import java.math.BigInteger;
import java.util.*;

public class SingleL1Cache {

	int offsetLength, indexLength, tagLength, miss =0, hit=0, index, cycles;
	String tag, array[];
	
	/** Constructor that will initialize the length of offset, index and tag*/
	public SingleL1Cache (int numberOfBlocks, int bytesPerBlock){ 
		
		this.indexLength = (Integer.toBinaryString(numberOfBlocks-1)).length();
		this.offsetLength = (Integer.toBinaryString(bytesPerBlock-1)).length();
		this.tagLength = 16 - this.indexLength - this.offsetLength;
		
		array = new String[numberOfBlocks];		// this array will act as the cache

		for(int i=0;i<numberOfBlocks;i++){	// this loop creates a list that will act as our cache
			array[i] = "0";   // 0 Stand for invalid and one stands for valid, initial everything is invalid
		}
	}
	
	/** This method will convert a hex value to binary and return it in 16 bits*/
	public String hexToBin(String s) { 
		
		 // This line does the actual convention but removes 0's in front
		  String binaryValue = new BigInteger(s, 16).toString(2);  
		  
		  // if the length is less than 16, we add zero's at the beginning
		  if(binaryValue.length()<16){  			
			  while(binaryValue.length()<16){
				  binaryValue = '0' + binaryValue;
			  }
		  }
		  return binaryValue;
	}
	
	/** Return a value for the index in decimal*/
	public int getIndex(String address){	
		
		String binaryIndex = address.substring(tagLength, tagLength + indexLength);
		return(Integer.parseInt(binaryIndex, 2));
				
	}
	
	/** Return the value of a tag*/
	public String getTag(String address){
		
		return(address.substring(0, tagLength));
	}
	
	/** Return number of misses*/
	public int getMisses(){
		return miss;
	}
	
	/** Return number of hits*/
	public int getHits(){
		return hit;
	}
	
	/** Return number of cycles*/
	public int getCycles(){
		cycles = hit*10 + miss*1000;
		return cycles;
	}
	
	public void contentChecker1(ArrayList<String> addresses){
		
		for(String address: addresses){

			index = getIndex(address);
			tag = getTag(address);
			
			if(array[index].substring(0, 1).equals("0")){   // if it is invalid
				miss +=1;
				array[index] = "1 " + tag;
			}
				
			else{

				if(array[index].substring(2).equals(tag)){
					hit +=1;
				}
				else{
					miss +=1;
					array[index] = "1 " + tag;
				}
			}
			
		}
	}
}
