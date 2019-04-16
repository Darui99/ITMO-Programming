import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class ReverseTranspose {

	public static int convert(StringBuffer s) {
		int res = 0;
		for (int i = s.length() - 1, r = 1; i >= 0; i--, r *= 10) {
			if (s.charAt(i) == '-') {
				res = -res;
			} else {
				res += (int)(s.charAt(i) - '0') * r;
			}
		}
		return res;
	}	

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		PrintWriter out = new PrintWriter(System.out);

		StringBuffer now = new StringBuffer("");
		String inp = new String();

		ArrayList<ArrayList<Integer>> arr = new ArrayList<>();

		while (in.hasNextLine()) {
			now.delete(0, now.length());
			inp = in.nextLine();
			arr.add(new ArrayList<Integer>());
			int n = arr.size();

			for (int i = 0; i < inp.length(); i++) {
			 	if (!('0' <= inp.charAt(i) && inp.charAt(i) <= '9' || inp.charAt(i) == '-')) {
			 		arr.get(n - 1).add(convert(now));
			 		now.delete(0, now.length());
			 	} else {
			 		now.append(inp.charAt(i));
			 	}
			}
			if (now.length() > 0) {
				arr.get(n - 1).add(convert(now));
			}			
		 }

		 ArrayList<ArrayList<Integer>> trArr = new ArrayList<ArrayList<Integer>>();

		 int maxu = -1;
		 for (int i = 0; i < arr.size(); i++) {
		 	if (maxu < arr.get(i).size()) {
		 		maxu = arr.get(i).size();
		 	}
		 }

		 for (int i = 0; i < maxu; i++) {
		 	trArr.add(new ArrayList<Integer>());
		 	for (int j = 0; j < arr.size(); j++) {
		 		if (i < arr.get(j).size()) {
		 			trArr.get(trArr.size() - 1).add(arr.get(j).get(i));	
		 		}	
		 	}
		 }

		 for (int i = 0; i < trArr.size(); i++) {
		 	for (int j = 0; j < trArr.get(i).size(); j++) {
		 	 	System.out.print(trArr.get(i).get(j) + " ");
		 	}
		 	System.out.print('\n');
		 }	  
	}
} 	


