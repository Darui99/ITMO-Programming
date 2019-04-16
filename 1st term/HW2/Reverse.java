import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class Reverse {

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

		ArrayList < ArrayList <Integer> > arr = new ArrayList < ArrayList <Integer> >();

		while (in.hasNextLine()) {
			now.delete(0, now.length());
			inp = in.nextLine();
			arr.add(new ArrayList <Integer>());
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

		 for (int i = 0; i < arr.size(); i++) {
		 	int n = arr.get(i).size();
		  	for (int j = 0; j < arr.get(i).size(); j++) {
		  		if (n - j - 1 <= j) {
		  			break;
		  		}
		  		int cur_value = arr.get(i).get(j);
		  		arr.get(i).set(j, arr.get(i).get(n - j - 1));
		  		arr.get(i).set(n - j - 1, cur_value);	
		  	}
		 }

		 int n = arr.size();
		 for (int i = 0; i < arr.size(); i++) {
		 	if (n - i - 1 <= i) {
		 		break;
		 	}
		 	ArrayList <Integer> cur_value = new ArrayList <Integer>();
		 	for (int j = 0; j < arr.get(i).size(); j++) {
		 		cur_value.add(arr.get(i).get(j));
		 	}
		 	arr.set(i, arr.get(n - i - 1));
		 	arr.set(n - i - 1, cur_value);
		 }

		 for (int i = 0; i < arr.size(); i++) {
		 	for (int j = 0; j < arr.get(i).size(); j++) {
		 	 	System.out.print(arr.get(i).get(j) + " ");
		 	}
		 	System.out.print('\n');
		 }
		  
	}
} 	


