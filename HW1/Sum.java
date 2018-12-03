import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class Sum
{
   public static int convert(String s)
   {
   		int res = 0;
   		for (int i = (int)s.length() - 1, r = 1; i >= 0; i--, r *= 10)
   		{
   			if (s.charAt(i) == '-')
   			{
   				res = -res;
   			}
   			else	
   			{
   				res += (int)(s.charAt(i) - '0') * r;
   			}
   		}
   		return res;	
   }
   
   public static void main(String[] args)
   {
      Scanner in = new Scanner(System.in);
      PrintWriter out = new PrintWriter(System.out);

      int ans = 0;
      String now = new String();
      for (int i = 0; i < (int)args.length; i++)
      {
      		now = "";
      		// System.out.println("! " + args[i]);
      		for (int j = 0; j < (int)args[i].length(); j++)
      		{
      			if (!('0' <= args[i].charAt(j) && args[i].charAt(j) <= '9' || args[i].charAt(j) == '-'))
      			{
      				ans += convert(now);
      				now = "";
      			}
      			else
      			{
      				now += args[i].charAt(j);
      			}
      		}
      		if ((int)now.length() > 0)
      		{
      			ans += convert(now);
      		}
       }
       System.out.println(ans);		
   }
}
