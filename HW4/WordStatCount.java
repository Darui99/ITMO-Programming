import javafx.util.Pair;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class WordStatCount {

    public static class MyTrip implements Comparable<MyTrip> {
        public Integer key;
        public String value;
        public Integer num;

        MyTrip(Integer nk, String nv, Integer nn) {
            this.key = nk;
            this.value = nv;
            this.num = nn;
        }

        @Override
        public int compareTo(MyTrip other) {
            if (this.key < other.key)
                return -1;
            if (this.key > other.key)
                return 1;
            if (this.key == other.key && this.num < other.num)
                return -1;
            if (this.key == other.key && this.num > other.num)
                return 1;
            return 0;
        }
    }

    public static String norm(StringBuffer s) {
        String res = s.toString();
        res = res.toLowerCase();
        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out);

        /*if (args.length != 2) {
            System.out.println("You have to write only input's and output's names.");
            return;
        }*/

        int curNum = 0;
        TreeMap<String, Integer> count = new TreeMap<>();
        TreeMap<String, Integer> numeric = new TreeMap<>();
        TreeSet <MyTrip> have = new TreeSet<>();

        try /*(Scanner in = new Scanner(new File(args[0]), "utf8"))*/ {

            StringBuffer now = new StringBuffer("");
            String inp = new String();

            while (in.hasNextLine()) {
                now.delete(0, now.length());
                inp = in.nextLine();

                for (int i = 0; i < inp.length(); i++) {
                    if (!(Character.isLetter(inp.charAt(i)) || Character.getType(inp.charAt(i)) == Character.DASH_PUNCTUATION || inp.charAt(i) == '\'')) {
                        if (now.length() == 0) {
                            continue;
                        }
                        String cur = norm(now);
                        if (count.containsKey(cur)) {
                            have.remove(new MyTrip(count.get(cur), cur, numeric.get(cur)));
                            count.put(cur, count.get(cur) + 1);
                            have.add(new MyTrip(count.get(cur), cur, numeric.get(cur)));
                        } else {
                            count.put(cur, 1);
                            numeric.put(cur, curNum);
                            have.add(new MyTrip(1, cur, curNum));
                            curNum++;
                        }
                        now.delete(0, now.length());
                    } else {
                        now.append(inp.charAt(i));
                    }
                }
                if (now.length() > 0) {
                    String cur = norm(now);
                    if (count.containsKey(cur)) {
                        have.remove(new MyTrip(count.get(cur), cur, numeric.get(cur)));
                        count.put(cur, count.get(cur) + 1);
                        have.add(new MyTrip(count.get(cur), cur, numeric.get(cur)));
                    } else {
                        count.put(cur, 1);
                        numeric.put(cur, curNum);
                        have.add(new MyTrip(1, cur, curNum));
                        curNum++;
                    }
                }
            }
        } /*catch (FileNotFoundException e) {
            System.out.println("Input file is not found.");
            return;
        }*/ catch (Exception e) {
            System.out.println("Something's wrong...");
            return;
        }

        try /*(PrintWriter out = new PrintWriter(new File(args[1]), "utf8"))*/ {

            for (MyTrip element : have) {
                System.out.println(element.value + " " + element.key);
            }

        } /*catch (FileNotFoundException e) {
            System.out.println("Output file is not found.");
            return;
        }*/ catch (Exception e) {
            System.out.println("Something's wrong lol...");
            return;
        }
    }
}
