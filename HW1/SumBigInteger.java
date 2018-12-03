import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class SumBigInteger {
   public static BigInteger convert(StringBuffer s) {
   		if (s.length() == 0) {
   			return new BigInteger("0");
   		} else {
   			return new BigInteger(s.toString());	
   		}
   }
   
   public static void main(String[] args) {
      Scanner in = new Scanner(System.in);
      PrintWriter out = new PrintWriter(System.out);

      BigInteger ans = new BigInteger("0");
      StringBuffer now = new StringBuffer("");
      for (int i = 0; i < args.length; i++) {
     		now.delete(0, now.length());
      		for (int j = 0; j < args[i].length(); j++) {
      			if (!('0' <= args[i].charAt(j) && args[i].charAt(j) <= '9' || args[i].charAt(j) == '-')) {
      				ans = ans.add(convert(now));
      				now.delete(0, now.length());
      			} else {
      				now.append(args[i].charAt(j));
      			}
      		}
      		if (now.length() > 0) {
      			ans = ans.add(convert(now));
      		}
       }
       System.out.println(ans);	
   }
}