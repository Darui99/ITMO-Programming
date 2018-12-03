import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class WordStatInput {

    public static String norm(StringBuffer s) {
        String res = s.toString();
        res = res.toLowerCase();
        return res;
    }

    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        //PrintWriter out = new PrintWriter(System.out);

        ArrayList<Integer> count = new ArrayList<>();
        ArrayList<String> have = new ArrayList<>();

        if (args.length != 2) {
            System.out.println("You have to write only input's and output's names.");
            return;
        }

        try (Scanner in = new Scanner(new File(args[0]), "utf8")) {

            StringBuffer now = new StringBuffer("");
            String inp = new String();


            TreeMap<String, Integer> num = new TreeMap<>();

            while (in.hasNextLine()) {
                now.delete(0, now.length());
                inp = in.nextLine();

                for (int i = 0; i < inp.length(); i++) {
                    if (!(Character.isLetter(inp.charAt(i)) || Character.getType(inp.charAt(i)) == 20 || inp.charAt(i) == '\'')) {
                        if (now.length() == 0) {
                            continue;
                        }
                        String cur = norm(now);
                        if (num.containsKey(cur)) {
                            count.set(num.get(cur), count.get(num.get(cur)) + 1);
                        } else {
                            num.put(cur, count.size());
                            count.add(1);
                            have.add(cur);
                        }
                        now.delete(0, now.length());
                    } else {
                        now.append(inp.charAt(i));
                    }
                }
                if (now.length() > 0) {
                    String cur = norm(now);
                    if (num.containsKey(cur)) {
                        count.set(num.get(cur), count.get(num.get(cur)) + 1);
                    } else {
                        num.put(cur, count.size());
                        count.add(1);
                        have.add(cur);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file is not found.");
            return;
        } catch (Exception e) {
            System.out.println("Something's wrong...");
            return;
        }

        try (PrintWriter out = new PrintWriter(new File(args[1]), "utf8")) {

            for (int i = 0; i < have.size(); i++) {
                out.println(have.get(i) + " " + count.get(i));
            }

        } catch (FileNotFoundException e) {
            System.out.println("Output file is not found.");
            return;
        } catch (Exception e) {
            System.out.println("Something's wrong...");
            return;
        }
    }
}