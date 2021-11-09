import java.math.BigInteger;
import java.util.*;

public class MultiLevelCache {
	
	int addressSize;

	/** L1 cache variables*/
	int offsetLength1, indexLength1, tagLength1, miss1 =0, hit1=0, index1, cycles;
	String tag1, array1[];
	
	/** L2 cache variables*/
	int offsetLength2, indexLength2, tagLength2, miss2 =0, hit2=0, index2;
	String tag2, array2[];

	//L3캐시 관련 변수 추가
	int offsetLength3, indexLength3, tagLength3, miss3 =0, hit3=0, index3;
	String tag3, array3[];	
	
	/** Constructor that will initialize the length of offset, index and tag*/
	public MultiLevelCache(int numberOfBlocks1, int bytesPerBlock1, int numberOfBlocks2, int bytesPerBlock2, int numberOfBlocks3, int bytesPerBlock3){ 
		
		/** This is for the l1 cache*/
		this.indexLength1 = (Integer.toBinaryString(numberOfBlocks1-1)).length();
		this.offsetLength1 = (Integer.toBinaryString(bytesPerBlock1-1)).length();
		this.tagLength1 = 16 - this.indexLength1 - this.offsetLength1;
		
		array1 = new String[numberOfBlocks1];		// this array will act as the cache
		for(int i=0;i<numberOfBlocks1;i++){	// this loop creates a list that will act as our cache
			array1[i] = "0";   // 0 Stand for invalid and one stands for valid, initial everything is invalid
		}
		
		/** This is for the l2 cache*/
		this.indexLength2 = (Integer.toBinaryString(numberOfBlocks2-1)).length();
		this.offsetLength2 = (Integer.toBinaryString(bytesPerBlock2-1)).length();
		this.tagLength2 = 16 - this.indexLength2 - this.offsetLength2;
		
		array2 = new String[numberOfBlocks2];		// this array will act as the cache
		for(int i=0;i<numberOfBlocks2;i++){	// this loop creates a list that will act as our cache
			array2[i] = "0";   // 0 Stand for invalid and one stands for valid, initial everything is invalid
		}	

		//L3 캐시 생성자 추가
		this.indexLength3 = (Integer.toBinaryString(numberOfBlocks3-1)).length();
		this.offsetLength3 = (Integer.toBinaryString(bytesPerBlock3-1)).length();
		this.tagLength3 = 16 - this.indexLength3 - this.offsetLength3;
		
		array3 = new String[numberOfBlocks3];		
		for(int i=0;i<numberOfBlocks3;i++){	
			array3[i] = "0";   
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
	
	/** Return a value for the index in decimal for L1*/
	public int getIndex1(String address){	
		
		String binaryIndex1 = address.substring(tagLength1, tagLength1 + indexLength1);
		return(Integer.parseInt(binaryIndex1, 2));			
	}
	
	/** Return a value for the index in decimal for L2*/
	public int getIndex2(String address){	
		
		String binaryIndex2 = address.substring(tagLength2, tagLength2 + indexLength2);
		return(Integer.parseInt(binaryIndex2, 2));			
	}

	//L3 인덱스 반환 추가
	public int getIndex3(String address){	
		
		String binaryIndex3 = address.substring(tagLength3, tagLength3 + indexLength3);
		return(Integer.parseInt(binaryIndex3, 2));			
	}
	
	/** Return the value of a tag for L1*/
	public String getTag1(String address){
		
		return(address.substring(0, tagLength1));
	}
	
	/** Return the value of a tag for L2*/
	public String getTag2(String address){
	
		return(address.substring(0, tagLength2));
	}

	//L3 태그 반환 추가
	public String getTag3(String address){
	
		return(address.substring(0, tagLength3));
	}
	
	/** Return number of misses*/
	public int getMisses1(){
		return miss1;
	}
	
	public int getMisses2(){
		return miss2;
	}
	
	/** Return number of hits*/
	public int getHits1(){
		return hit1;
	}
	
	public int getHits2(){
		return hit2;
	}

	public int getHits3(){	//hit3 반환 추가
		return hit3;
	}

	public int getMisses3(){ //miss3 반환 추가
		return miss3;
	}
	public double getHitRatio1(){	//L1캐시 hitRatio
		return (double)hit1/addressSize;
	}

	public double getMissRatio1(){	//L1캐시 missRatio
		return (double)miss1/addressSize;
	}

	public double getHitRatio2(){	//L2캐시 hitRatio
		return (double)hit2/miss1;
	}

	public double getMissRatio2(){	//L1캐시 missRatio
		return (double)miss2/miss1;
	}

	public double getHitRatio3(){	//L3캐시 hitRatio 추가
		return (double)hit3/miss2;
	}

	public double getMissRatio3(){	//L3캐시 missRatio 추가
		return (double)miss3/miss2;
	}

	public double getAvgAccessTime(){  //Average Access Time 갱신(L3 캐시의 access time은 500으로 가정)
		return getHitRatio1()*10+getMissRatio1()*(getHitRatio2()*100+getMissRatio2()*(getHitRatio3()*500+getMissRatio3()*1000));
	}

	/** Return number of cycles*/
	//L3까지 고려하여 갱신
	public int getCycles(){
		cycles = hit1*10 + hit2*100 + hit3*500 + miss3*1000;
		return cycles;
	}
	
	/** This method checks on the L1 cache, and do relevant calculations. If there is no content it goes to L2 cache*/
	public void contentChecker1(ArrayList<String> addresses){
		
		this.addressSize=addresses.size();

		/**Iterate through the array-list with addresses and check if they are on L1*/
		for(String address: addresses){

			index1 = getIndex1(address);
			tag1 = getTag1(address);
			
			if(array1[index1].substring(0, 1).equals("0")){   // if it is invalid
				miss1 +=1;
				this.contentChecker2(address);  // if there is a miss it goes to checks L2
				array1[index1] = "1 " + tag1;
			}
				
			else{

				if(array1[index1].substring(2).equals(tag1)){
					hit1 +=1;
				}
				else{
					miss1 +=1;
					this.contentChecker2(address);	// if there is a miss it goes to checks L2
					array1[index1] = "1 " + tag1;
				}
			}
		}
	}
	
	/** This method checks on the L2 cache is there is content or not then do the relevant calculations */
	//miss시 L3캐시를 확인하는 contentChecker3() 실행하는 코드 추가
	public void contentChecker2(String address){
		
		index2 = getIndex2(address);
		tag2 = getTag2(address);
			
		if(array2[index2].substring(0, 1).equals("0")){   // if it is invalid
			miss2 +=1;
			array2[index2] = "1 " + tag2;
		}
				
		else{

			if(array2[index2].substring(2).equals(tag2)){
				hit2 +=1;
			}
			else{
				miss2 +=1;
				this.contentChecker3(address);	//여기 추가됨
				array2[index2] = "1 " + tag2;
			}
		}
	}

	//L3캐시를 확인하는 메서드 추가
	public void contentChecker3(String address){
		
		index3 = getIndex3(address);
		tag3 = getTag3(address);
			
		if(array3[index3].substring(0, 1).equals("0")){   
			miss3 +=1;
			array3[index3] = "1 " + tag3;
		}
				
		else{

			if(array3[index3].substring(2).equals(tag3)){
				hit3 +=1;
			}
			else{
				miss3 +=1;
				array3[index3] = "1 " + tag3;
			}
		}
	}

}