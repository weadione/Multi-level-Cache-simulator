import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		
		String fileName = args[0];			    	// comment out this line from the command line
		//String fileName = "heapsort.addr";     // uncomment this line to run on an IDE
		String config = "singlelevel";
		int l1NumberOfBlocks = 16, l1BytesPerBlock = 32;		// sets the L1 cache size
		int l2NumberOfBlocks = 64, l2BytesPerBlock = 64;		// sets the L2 cache size
		
		ArrayList<String> addresses = new ArrayList<String>();  // this will store all addresses read from the file

		SingleL1Cache tempCache = new SingleL1Cache(16,16); // This is just a dummy cache so i can be able to use the method in the single cache class
		BufferedReader file = null;
		
		try {	// Here we read a file and file store all addresses in an arrayList
			
			String line;
			file = new BufferedReader(new FileReader("Files/"+fileName));
			
			/** Adding each line on the array, converting it to decimal before*/
			while ((line = file.readLine()) != null) {
					addresses.add(tempCache.hexToBin(line));
			}
		} 
		
		catch (IOException e) {
				System.out.println("File cannot be found");
		}
		
		
		System.out.println(fileName + " conatins " + addresses.size() + " instructions."); 
		
		if(config.equalsIgnoreCase("Multilevel")){   // if multilevel cache selected
			
			MultiLevelCache cache = new  MultiLevelCache(l1NumberOfBlocks, l1BytesPerBlock,l2NumberOfBlocks, l2BytesPerBlock);	// Creating a new single cache object with giving blocks and block sizes
			cache.contentChecker1(addresses);	// Calling the method that checks for content in the cache
			System.out.println("L1 hits:       " + cache.getHits1());
			System.out.println("L1 misses:     " + cache.getMisses1());
			System.out.println("L2 hits:       " + cache.getHits2());
			System.out.println("L2 misses:     " + cache.getMisses2());
			System.out.println("Cycles:        " + cache.getCycles());
		}
		
		else if(config.equalsIgnoreCase("singlelevel")){	// if single cache was selected
			
			SingleL1Cache cache = new SingleL1Cache(l1NumberOfBlocks, l1BytesPerBlock);	// Creating a new single cache object with giving blocks and block size
			cache.contentChecker1(addresses);				// Calling the method that checks for content in the cache
			System.out.println("L1 hits:       " +	cache.getHits());
			System.out.println("L1 misses:     " +  cache.getMisses());
			System.out.println("L1 cycles:     " +  cache.getCycles());
		}
		
		else{
			System.out.println("The configuration you selected does not exist");
		}
	}
}
