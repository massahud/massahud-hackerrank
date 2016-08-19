package hackerrank.absoluteelementsums;

import java.io.*;
import java.util.*;


public class Solution {

    
    public static void main(String[] args) throws IOException {
                System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        Scanner scan = new Scanner(new BufferedReader(new InputStreamReader(Solution.class.getResourceAsStream("input06.txt")), 1024*64));
        final int N = scan.nextInt();
        long zeroPos = 2000;
        int length = (int)zeroPos*2+1;
        final int[] frequency = new int[length];
        long nPositive = 0;
        long nNegative = 0;
        long negativeSum = 0;
        long positiveSum = 0;
        long[] sums = new long[length];
        for (int i = 0; i < N; i++) {            
            int n = scan.nextInt();            
            if (n > 0) {
                nPositive++;
                positiveSum+=n;
            } else if (n < 0) {
                nNegative++;
                negativeSum+=n;
            }
            int p = (int)zeroPos+n;
            frequency[p]++;            
        }
        sums[(int)zeroPos] = Math.abs(negativeSum) + positiveSum;        
        long nNeg = nNegative;
        long nPos = nPositive;
        long pSum = positiveSum;
        long nSum = negativeSum;
        for (int i = (int)zeroPos+1; i < frequency.length; i++) {                        
           
            nSum -= nNeg + frequency[i-1];
            pSum -= nPos;            
            nNeg += frequency[i-1];
            nPos -= frequency[i];
            
            sums[i] = Math.abs(nSum) + pSum;
        }
        nNeg = nNegative;
        nPos = nPositive;
        pSum = positiveSum;
        nSum = negativeSum;
        for (int i = (int)zeroPos-1; i >= 0; i--) {
            nSum += nNeg;
            pSum += nPos + frequency[i+1];            
            nNeg -= frequency[i];
            nPos += frequency[i+1];
            
            sums[i] = Math.abs(nSum) + pSum;
        }        
     
        
            
        final int Q = scan.nextInt();
        
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out,1024*32));
        for (int i = 0; i < Q; i++) {
            zeroPos -= scan.nextInt();
            if (zeroPos >= 0 && zeroPos < length) {
                out.println(sums[(int)zeroPos]);                
            } else if (zeroPos < 0) {
                out.println(sums[0] - N*zeroPos);
            } else if (zeroPos >= length) {
                out.println(sums[length-1] + N*(zeroPos-length+1));
            }
        }    
        out.flush();
    }
}