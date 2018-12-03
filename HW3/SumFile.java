import javafx.util.Pair;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class SumFile {

    public static String norm(StringBuffer s) {
        String res = s.toString();
        res = res.toLowerCase();
        return res;
    }

    public static void main(String[] args) {
        // Scanner in = new Scanner(System.in);
        // PrintWriter out = new PrintWriter(System.out);

        if (args.length != 2) {
            System.out.println("You have to write only input's and output's names.");
            return;
        }

        TreeMap<String, Integer> count = new TreeMap<>();

        try (Scanner in = new Scanner(new File(args[0]), "utf8")) {

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
                            count.put(cur, count.get(cur) + 1);
                        } else {
                            count.put(cur, 1);
                        }
                        now.delete(0, now.length());
                    } else {
                        now.append(inp.charAt(i));
                    }
                }
                if (now.length() > 0) {
                    String cur = norm(now);
                    if (count.containsKey(cur)) {
                        count.put(cur, count.get(cur) + 1);
                    } else {
                        count.put(cur, 1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file is not found.");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index is out of bounds.");
            return;
        } catch (Exception e) {
            System.out.println("Something's wrong...");
            return;
        }

        try (PrintWriter out = new PrintWriter(new File(args[1]), "utf8")) {

            for (Map.Entry<String, Integer> elem : count.entrySet()) {
                out.println(elem.getKey() + " "  + elem.getValue());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Output file is not found.");
            return;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Use utf8 encoding.");
            return;
        } catch (Exception e) {
            System.out.println("Something's wrong...");
            return;
        }
    }
}
