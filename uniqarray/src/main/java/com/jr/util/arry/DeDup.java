package com.jr.util.arry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * To create a unique set of data in an array.
 * Each method utilize a different logic to create a unique array.
 *
 */
public class DeDup {

	private int counter = 0;
	private int[] original, dupOriginal;
	private int[] segmentsPercentage = new int[]{20,40,60,80};
	int sortedSegmentIdx = 0;
		
	public DeDup(int[] randomIntegers) {
		this.original = randomIntegers;
	}
	
	/**
	 * Return the original
	 */
	public int[] getRawUnModifiableArray() {
		return original;
	}
	
	/**
	 * Implementation 1: 
	 * 		The methods will eliminate the duplicate from the array and create a unique array using Set to create a unique data.
	 * Pros: 
	 * 		No logic required for the removing duplicate. Development is immediate and zero-defect 
	 * Cons: 
	 * 		1) Completely rely on the java.util.Set
	 * 		2) The primitive type need to be transformed to Object type and vice versa.
	 * 		3) When the input data increased, a significant memory footprint will be observed. 
	 * @return
	 */
	public int[] getUniqueIntArrayWithSet() {
		Integer[] intArr = new Integer[original.length];
		for(int i=0;i<original.length;i++) {
			intArr[i] = Integer.valueOf(original[i]);
		}
		Object[] obj = new HashSet<Integer>(new ArrayList<Integer>(Arrays.asList(intArr))).toArray();		
		dupOriginal = new int[obj.length];
		for(int i=0;i<obj.length;i++) {
			try {
				dupOriginal[i] = Integer.parseInt(obj[i].toString());
			} catch(NumberFormatException e) {
				System.out.println("Not a valid number: "+obj[i] + ", Issue: "+e.getLocalizedMessage());
			}
		}
		return dupOriginal;
	}
	
	/**
	 * Implementation 2:
	 * 		The method will create a unique array set by eliminating the duplicates by comparing each index value through the entire set in the array.
	 * Pros:
	 * 		The method utilizes a two step in finding the duplicate by using inner looping to compare the index through the entire array
	 * Cons: 
	 * 		A complete iteration is done in the inner loop and thus creating a performance impact when the load increased.
	 * @return
	 */
	public int[] getUniqueIntArrayWithArrCopy() {
		dupOriginal = new int[0];
		for (int a = 0; a < original.length; a++) {			
			boolean isSame = false;
			for (int b = 0; b < a; b++) {
				if(original[a] == original[b]) {
					isSame = true;
					break;
				}
			}
			if(!isSame) {
//				Increase the size by one to add the new unique value
				dupOriginal = Arrays.copyOf(dupOriginal, dupOriginal.length+1);
//				Replace the default value 0 with the new value
				dupOriginal[dupOriginal.length-1] = original[a];
			}
		}
		return dupOriginal;
	}
	
	/**
	 * Implementation 3:
	 * 		The method uses sorting as the first mechanism to bring the duplicates in a series. Then iterate through the array
	 * 		and eliminate the adjacent duplicates using System.arraycopy
	 * Pros:
	 * 		The looping through the array is minimized to find duplicates, Arrays.sort() is used to sort and then compare both the adjacent values are not same.
	 * 		This approach quickly removes the duplicates compared to previous Implementation 2. 
	 * Cons:
	 * 		When the input data is large, the sorting of array will have a significant performance impact
	 * 		Iterating each index value and do comparison is done for the entire array will have a time consuming is large when data is very large 
	 * @return
	 */
	public int[] getUniqueIntSortedArray() {
		dupOriginal = new int[1];
		int[] uniqArray = new int[original.length];
		System.arraycopy(original,0,uniqArray,0, original.length);
		Arrays.sort(uniqArray);
		getUniqueSet(uniqArray);
		return dupOriginal;
	}
	
	/**
	 * Implementation 4: 
	 * 		The method will remove duplicates by applying a sorting method.
	 * 		The entire array is divided into equal segments increasing from 10% to 80%
	 * 		Each segment is first sorted and then iterated to remove the duplicates
	 * 		This is done recursively by increasing the segment size.
	 * Pros: 
	 * 		This approach will handle the largest array.
	 * 		java.util.Collection is eliminated in this approach. 
	 * Cons:
	 * 		Implementation of logic is time consuming
	 * 
	 * @return
	 */
	public int[] getUniqueSortedArray() {
		dupOriginal = new int[original.length];
		System.arraycopy(original,0,dupOriginal,0,original.length);		
		sortDuplicate(0,dupOriginal.length);
		return dupOriginal;
	}
	
	/**
	 * Recursively, iterate through the array to eliminate the duplicates
	 * Compare 3 values in the sequence in a single iteration (previous value, current value, next value)
	 * Use the System.arraycopy to dynamically grow the array
	 * To ease the comparison, the pre-requisite is to sort the array first. 
	 */
	private void getUniqueSet(int[] array) {
		boolean isPreviousDuplicate = false;
		boolean isNextDuplicate = false;
//		System.out.print("\n"+ (counter > 0 ? array[counter-1] : array[counter]));
//		System.out.print("--"+array[counter]);
//		System.out.print("--"+array[counter+1]);
		if(counter == 0) {
			dupOriginal[counter] = array[counter];
		}
		// Logic 1: If previous & current value are same
		if((counter > 0) && (array[counter-1] == array[counter])) {
			isPreviousDuplicate = true;
		}
//		If current value is not same, then add it to new array
		if((counter > 0) && !isPreviousDuplicate) {
			copyNewValue(array[counter], counter, array);
		}
		//Logic 2: If current & next value are same
		if((counter+1 < array.length-1) && (array[counter] == array[counter+1])) {
			if(isPreviousDuplicate) {
				counter += 2;
			} else {
				counter += 1;
			}
			isNextDuplicate = true;
		}
//		If next value is not same, then add it to new array
		if(!isNextDuplicate) {
			counter += (counter > array.length) ? 0 : 1;
			if(counter < array.length) {
				copyNewValue(array[counter], counter, array);
			}
			counter += (counter > array.length) ? 0 : 1;
		}
//		if(!isPreviousDuplicate && !isNextDuplicate && counter < original.length) {
//			counter++;
//		}
		if(counter < array.length) {
			getUniqueSet(array);
		}
	}
	
	/**
	 * Dynamically grow array, add the new values to the existing array
	 * @param value
	 * @param currindex
	 */
	private void copyNewValue(int value, int currindex, int[] array) {
		int[] tmp = new int[dupOriginal.length+1];
//		Copy to a temporary array from the result array with additional one index to add the new value
		System.arraycopy(dupOriginal,0,tmp,0,dupOriginal.length);
//		Add the new value to the last index of the temporary array
		tmp[tmp.length-1] = array[counter];
//		Now, copy to the result array from temporary array
		dupOriginal = new int[tmp.length];
		System.arraycopy(tmp,0,dupOriginal,0,tmp.length);		
//		System.out.println(Arrays.toString(uniqSet));
	}
	
	/**
	 * The recursive method creates 2 segments and sort the segments
	 * After each iteration, the 2 segments are merged and sent out for another search.
	 * This will be done until all of the data is sorted.
	 * Each segment will compare the values of first & the second index and swaps it in ascending order
	 * 
	 * To add performance, in each iteration, the first segment will increment to sort 
	 * and the second segment will decrement the sorting.
	 *  
	 * @param fIdx
	 * @param lIdx
	 */
//	int recursivecalls = 0;
//	int loopexecutions = 0;
//	int innerloopexecutions = 0;
	private void sortDuplicate(int fIdx, int lIdx) {
//		System.out.println(Arrays.toString(dupOriginal));
//		recursivecalls+=1;
//		1. Iterate the array from start and from the ending simultaneously
		int fSegmentVal = 0;
		int sSegmentVal = 0;
		int fSegmentIdx = 0;
		int sSegmentIdx = 0;
		sortSegments();
		int arrLength = dupOriginal.length;
		for (int i = fIdx, j = lIdx-1; i <= lIdx && j > i; i++, j--) {
//			System.out.println(Arrays.toString(dupOriginal));
//			loopexecutions+=1;
			if(i+1 == dupOriginal.length) {
				fIdx = i;
				break;
			}
			if(j >= arrLength) {
				j = arrLength-1;
			}
//			System.out.println("i = "+i+" : value : ******"+dupOriginal[i] +", fSegmentVal: "+fSegmentVal);
//			System.out.println("j = "+j+" : value : ******"+dupOriginal[j] +", sSegmentVal: "+sSegmentVal);
//			Store the first index and compare this values through each iteration
			if(i == fIdx && j == lIdx-1) {
				fSegmentIdx = i;
				sSegmentIdx = j;
				fSegmentVal = dupOriginal[i];
				sSegmentVal = dupOriginal[j];
//				System.out.println("i = "+i+" : value : ******"+dupOriginal[i] +", fSegmentVal: "+fSegmentVal);
			}
//			Both first and end are same
			if((fSegmentIdx == i) && (sSegmentIdx == j) && (dupOriginal[i] == dupOriginal[j])) {
//				System.out.println("first & last index are same");
//				1,2,4,5,1
				if(i == 0) {
					removeFirstIdx(i, j-1);					
				}
//				2,3,1,4,6,1
				if(i > 0 && j == arrLength-1) {
					removeLastIdx(j);
				}				
				arrLength = dupOriginal.length;
				--j;
				sSegmentIdx = j;
				sSegmentVal = dupOriginal[j-1];
			}
			if((fSegmentIdx < i) && (sSegmentIdx < j) && (dupOriginal[i] == dupOriginal[j])) {
//				System.out.println("first & last index are same");
				removeMiddleIdx(i, j+1, arrLength-1);
				arrLength = dupOriginal.length;
				--j;
				sSegmentIdx = j;
				sSegmentVal = dupOriginal[j-1];
			}			
//			jump to the 2nd index, to start comparing
			if(i == fSegmentIdx && ((i+1) < dupOriginal.length)){
				++i;
			}
//			jump to the last but one index, to start comparing
			if(j == sSegmentIdx) {
				--j;
			}
			if(sSegmentVal == dupOriginal[i]) {
//				if the 2nd index is compared
				if(j == arrLength-1) {
//					System.out.println("first index compared with next index value :"+i +". dupOriginal[i] :"+dupOriginal[i]+ ", fSegmentVal: "+fSegmentVal);
					removeFirstIdx(i, arrLength-1);
				}
				--j;
				arrLength = dupOriginal.length;
				sSegmentIdx = arrLength-1;
				sSegmentVal = dupOriginal[arrLength-1];
			}			
			if(fSegmentVal == dupOriginal[i]) {
//				if the 2nd index is compared
				if(i == 1) {
//					System.out.println("first index compared with next index value :"+i +". dupOriginal[i] :"+dupOriginal[i]+ ", fSegmentVal: "+fSegmentVal);
					removeFirstIdx(i, arrLength-1);
				}
				if(i > 1 && ((i+1) < arrLength-1)) {
//					System.out.println("first index compared are same on the index :"+i +". dupOriginal[i] :"+dupOriginal[i]+ ", fSegmentVal: "+fSegmentVal);
					removeMiddleIdx(i, i+1, (arrLength)-(i+1));
				}
				--i;
				--j;
				arrLength = dupOriginal.length;
			}			
			if((i <= arrLength-1) && (i+1 <= arrLength-1) && (dupOriginal[i] == dupOriginal[i+1])) {
//				System.out.println("index value compared with adjacent value :"+i);
				removeMiddleIdx(i, i+1, (arrLength)-(i+1));
				arrLength = dupOriginal.length;
				--i;
				--j;
				
			}
			if((j < i+5) && j > i+2 && j < arrLength) {
				j = arrLength;
			}
		}
		boolean isDup = false;
		int firstNo = 0;
		for (int k = 0; k < dupOriginal.length; k++) {
			for(int m=0; m < dupOriginal.length; m++) {
				if((k != m) && dupOriginal[k] == dupOriginal[m]) {
					isDup = true;
					firstNo = k;
					break;
				}				
			}
			if(isDup) {
				isDup = true;
				break;
			}
		}
		if(isDup) {
			sortDuplicate(firstNo, dupOriginal.length);
		}
	}
	
	/**
	 * The entire array is divided into segments and 
	 * each segment array is sorted. This approach will be able to remove duplicates quickly. 
	 */
	private void sortSegments() {
//		sortFirstPartialArray();
//		sortLastPartialArray();
		if(sortedSegmentIdx == segmentsPercentage.length) {
			return;
		}
		float x = segmentsPercentage[sortedSegmentIdx];
		float y = 100;
		float percentage = (x/y);
		int segment = calculateSegmentIdx(0, dupOriginal.length, percentage);
		int totalLength = dupOriginal.length;
		int iterations = (totalLength/segment);
		int remainingSegment = totalLength - (iterations*segment);
		for(int i=0; i<iterations; i++) {
			int stIdx = i==0 ? 0 : segment*i;
			int miIdx = i+1 < iterations ? segment*(i+1) : totalLength;
			if(i == 0) {
				sortFirstOrLastSegment(stIdx, miIdx, totalLength, true);
			}
			if((i > 0) && (i < iterations-1)) {
				sortMiddleSegment(0, stIdx, miIdx, totalLength);
			}
			if(i == iterations-1) {
				if(miIdx < totalLength) {
					miIdx+=remainingSegment;
				}
				sortFirstOrLastSegment(0, stIdx, miIdx, false);
			}
		}		
		sortedSegmentIdx+=1;		
	}
	
	/**
	 * A sorting functionality with a inner loop to find the duplicate number
	 * When a duplicate is identified the inner loop is terminated and 
	 * this improves the efficiency of the search
	 * @param arry
	 */
	private void sort(int[] arry) {
		int[] nArry = new int[1];
		int jVal = 0;
		for(int i=0; i < arry.length; i++) {
//			if((i+1 < arry.length) && (arry[i] == arry[i+1])) {
//				continue;
//			}
			if(i==0) {
				nArry[i] = arry[i];
			}
			int hiIdx = 0;
			int hiVal = 0;
			boolean needChng = false;
//			locate high value index through the array for inserting
			for(int j=jVal; j<arry.length;j++) {
				if((j>i) && (arry[i] > hiVal) && (arry[i] == arry[j])) {
					hiIdx = j;
					hiVal = arry[j];
//					System.out.println(Arrays.toString(arry));
//					System.out.println("[i="+i+"] [value ="+arry[i]+"]***[j="+j+"] [value ="+arry[j]+"] ****");
					needChng = true;
					break;
				}
			}
			if(needChng && (i+1 < arry.length) && (i+1 == hiIdx)) {
				continue;
			}
			if(!needChng && i+2 >= arry.length) {
				jVal = 0;
				break;
			}
			if(needChng) {
				int newIdx = i+1;
				nArry = new int[hiIdx+1];
//				Get the first index
				System.arraycopy(arry, 0, nArry, 0, newIdx);
//				System.out.println(Arrays.toString(nArry));
//				Assign the shifted index next to master copy
				System.arraycopy(arry, hiIdx, nArry, newIdx, 1);
//				System.out.println(Arrays.toString(nArry));
//				Fill the remaining in the segment excluding the shifted index
				System.arraycopy(arry, i, nArry, newIdx, ((hiIdx+1)-(newIdx)));
//				System.out.println(Arrays.toString(nArry));
//				Copy the sorted over the master data
				System.arraycopy(nArry, 0, arry, 0, nArry.length);
//				System.out.println(Arrays.toString(arry));
				jVal = newIdx;
			}
		}
//		Arrays.sort(dupOriginal, 0, mIdx);
	}

	/**
	 * Create a segment of 10% of the total array
	 * @param fIdx
	 * @param lIdx
	 * @return
	 */
	private int calculateSegmentIdx(int fIdx, int lIdx, float percentage) {
		float segment = (lIdx-fIdx)*percentage;
		return Math.round(segment);
	}
	
	private void sortFirstOrLastSegment(int stIdx, int mIdx, int lIdx, boolean isFirst) {
		if(isFirst) {
			int[] tmpi = new int[mIdx];
			System.arraycopy(dupOriginal, 0, tmpi, 0, mIdx);
//			System.out.println(Arrays.toString(tmpi));
			sort(tmpi);
//			System.out.println(Arrays.toString(tmpi));
			System.arraycopy(tmpi, 0, dupOriginal, 0, tmpi.length);
		}
		if(!isFirst) {
			int[] tmpj = new int[lIdx-(mIdx)];
			System.arraycopy(dupOriginal, mIdx, tmpj, 0, lIdx-(mIdx));
//			System.out.println(Arrays.toString(tmpj));
			sort(tmpj);
//			System.out.println(Arrays.toString(tmpj));
			System.arraycopy(tmpj, 0, dupOriginal, mIdx, lIdx-(mIdx));
		}
//		System.out.println(Arrays.toString(dupOriginal));
	}
	
	private void sortMiddleSegment(int fIdx, int stIdx, int miIdx, int lIdx) {		
		int[] tmpj = new int[stIdx];
		System.arraycopy(dupOriginal, stIdx, tmpj, 0, (miIdx-stIdx));
//		System.out.println(Arrays.toString(tmpj));
		sort(tmpj);
//		System.out.println(Arrays.toString(tmpj));
		System.arraycopy(tmpj, 0, dupOriginal, stIdx, (miIdx-stIdx));
//		System.out.println(Arrays.toString(dupOriginal));
	}
		
	private void removeMiddleIdx(int sIdx, int mIdx, int eIdx) {
		int[] tmpi = new int[sIdx];
		System.arraycopy(dupOriginal, 0, tmpi, 0, sIdx);
//		System.out.println(Arrays.toString(tmpi));
		int[] tmpj = new int[eIdx];
		System.arraycopy(dupOriginal, mIdx, tmpj, 0, eIdx);
//		System.out.println(Arrays.toString(tmpj));
		dupOriginal = new int[tmpi.length + tmpj.length];
		System.arraycopy(tmpi, 0, dupOriginal, 0, tmpi.length);
		System.arraycopy(tmpj, 0, dupOriginal, tmpi.length, tmpj.length);
//		System.out.println(Arrays.toString(dupOriginal));
	}
	
	private void removeFirstIdx(int sIdx, int eIdx) {
		int[] tmpi = new int[eIdx];
		System.arraycopy(dupOriginal, sIdx, tmpi, 0, eIdx);
		dupOriginal = new int[tmpi.length];
		System.arraycopy(tmpi, 0, dupOriginal, 0, tmpi.length);
//		System.out.println(Arrays.toString(dupOriginal));
	}

	private void removeLastIdx(int eIdx) {
		int[] tmpi = new int[eIdx];
		System.arraycopy(dupOriginal, 0, tmpi, 0, eIdx);
		dupOriginal = new int[tmpi.length];
		System.arraycopy(tmpi, 0, dupOriginal, 0, tmpi.length);
//		System.out.println(Arrays.toString(dupOriginal));
	}
	/**
	 * Main execution
	 * build the application using maven
	 * Execute the application: mvn exec:java -Dexec.mainClass=com.jr.util.arry.DeDup
	 * @param args
	 */
	public static void main(String[] args) {
/*				int[] randomIntegers = {1,2,34,34,25,1,45,3,26,85,4,34,86,25,43,2,1,10000,11,16,19,1,18,4,9,3,
                20,17,8,15,6,2,5,10,14,12,13,7,8,9,1,2,15,12,18,10,14,20,17,16,3,6,19,
                13,5,11,4,7,19,16,5,9,12,3,20,1,2,34,34,25,1,45,3,26,85,4,34,86,25,43,2,1,10000,11,16,19,1,18,4,9,3,
                20,17,8,15,6,2,5,10,14,12,13,7,8,9,1,2,15,12,18,10,14,20,17,16,3,6,19,
                13,5,11,4,7,19,16,5,9,12,3,20,7,15,17,10,6,1,8,18,4,14,13,2,11,11,7,15,17,10,6,1,8,18,4,14,13,2,11,11,
                1,2,34,34,25,1,45,3,26,85,4,34,86,25,43,2,1,10000,11,16,1,2,34,34,25,1,45,3,26,85,4,34,86,25,43,2,1,10000,11,16,19,1,18,4,9,3,
                20,17,8,15,6,2,5,10,14,12,13,7,8,9,1,2,15,12,18,10,14,20,17,16,3,6,19,
                13,5,11,4,7,19,16,5,9,12,3,20,7,15,17,10,6,1,8,18,4,14,13,2,11,11,19,1,18,4,9,3,
                20,17,8,15,6,2,5,10,14,12,13,7,8,9,1,2,15,12,18,10,14,20,17,16,3,6,19,
                13,5,11,4,7,19,16,5,9,12,3,20,7,15,17,10,6,1,8,18,4,14,13,2,11,11};
*/
		
	    int[] randomIntegers = {1,2,34,34,25,1,45,3,26,85,4,34,86,25,43,2,1,10000,11,16,19,1,18,4,9,3,
                20,17,8,15,6,2,5,10,14,12,13,7,8,9,1,2,15,12,18,10,14,20,17,16,3,6,19,
                13,5,11,4,7,19,16,5,9,12,3,20,7,15,17,10,6,1,8,18,4,14,13,2,11};
			
/*		
		int[] randomIntegers = new int[4000];
		Random x = new Random();
		for(int i=0; i<randomIntegers.length; i++) {
			randomIntegers[i] = x.nextInt(randomIntegers.length/2);
		}
*/		
		DeDup dup = new DeDup(randomIntegers);
		System.out.println(Arrays.toString(dup.getRawUnModifiableArray()));
		System.out.println(Arrays.toString(dup.getUniqueIntArrayWithArrCopy()));
		System.out.println(Arrays.toString(dup.getUniqueIntArrayWithSet()));
		System.out.println(Arrays.toString(dup.getUniqueSortedArray()));
//		System.out.println("loopexecutions: "+dup.loopexecutions+", recursivecalls: "+dup.recursivecalls);
		System.out.println(Arrays.toString(dup.getUniqueIntSortedArray()));
	}

}