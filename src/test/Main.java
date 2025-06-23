package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;



public class Main {
	static List<List<Integer>> graph;
	static Boolean[] visited;
	static List<List<Integer>> reverseGraph;
	public static void main(String[] args) throws IOException {
		boolean[][] A = {{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true},{true,true,true,true}}; 
        int result = 0 ;
        int squareleng = 0; 
        int N = A.length;
        int M = A[0].length;
        int[][] dp = new int[N][M];
        Map<Integer,Integer> squareCheck = new HashMap<Integer,Integer>();
        //첫행
        for (int i = 0; i < N; i++) {
            dp[i][0] = A[i][0] ? 1: 0;
        }
        for (int i = 0; i < M; i++) {
            dp[0][i] = A[0][i] ? 1: 0;
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if(A[i][j]){
                    dp[i][j] = Math.min(Math.min(dp[i-1][j],dp[i][j-1]),dp[i-1][j-1])+1 ;
                        squareleng = dp[i][j];
                        int s = squareCheck.getOrDefault(squareleng,0)+1;
                        System.out.println("11 " + squareleng);
                        System.out.println("22 " + +s);
                        if(squareCheck.containsKey(squareleng)){
                            squareCheck.put(squareleng,s);
                        } else {
                            squareCheck.put(squareleng,1);
                        }
                } else {
                    dp[i][j] = 0;
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : squareCheck.entrySet()) {
			Integer key = entry.getKey();
			Integer val = entry.getValue();
			System.out.println("key == " +key);
			System.out.println("val == " +val);
			if(val >= 2 ) {
				if(result < key) {
					result = key;
				}
			}
		}
        if(result == 1) {
            //한칸일떄 
            result = 0;
        } else {
            //넓이 
            result = result*result;
        }  
        System.out.println(result);;
    }
}
	
//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//StringTokenizer st = new StringTokenizer(br.readLine());