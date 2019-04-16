import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class ReverseMin {

    public static int convert(StringBuilder s) {
        try {
            return Integer.parseInt(s.toString());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void main(String[] args) {
        FastScanner in = new FastScanner();
        //Scanner in = new Scanner(System.in);

        StringBuilder now = new StringBuilder("");
        String inp = new String();

        ArrayList < ArrayList <Integer> > arr = new ArrayList <>();
        while (in.hasNext()) {
            now.delete(0, now.length());
            inp = in.nextLine();
            arr.add(new ArrayList <>());
            int n = arr.size();

            for (int i = 0; i < inp.length(); i++) {
                if (!(Character.isDigit(inp.charAt(i)) || inp.charAt(i) == '-')) {
                    if (now.length() > 0) {
                        arr.get(n - 1).add(convert(now));
                        now.delete(0, now.length());
                    }
                } else {
                    now.append(inp.charAt(i));
                }
            }
            if (now.length() > 0) {
                arr.get(n - 1).add(convert(now));
            }
        }

        int width = 0;
        int ans[][] = new int[arr.size()][];
        for (int i = 0; i < arr.size(); i++) {
            ans[i] = new int[arr.get(i).size()];
            if (arr.get(i).size() > width) {
                width = arr.get(i).size();
            }
            if (arr.get(i).size() == 0) {
                continue;
            }
            int min = arr.get(i).get(0);
            for (int j = 0; j < arr.get(i).size(); j++) {
                if (arr.get(i).get(j) < min) {
                    min = arr.get(i).get(j);
                }
            }
            for (int j = 0; j < arr.get(i).size(); j++) {
                ans[i][j] = min;
            }
        }

        int columnMin[] = new int[width];
        boolean was[] = new boolean[width];
        for (int i = 0; i < width; i++) {
            was[i] = false;
        }

        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < arr.get(i).size(); j++) {
                if (!was[j] || columnMin[j] > arr.get(i).get(j)) {
                    was[j] = true;
                    columnMin[j] = arr.get(i).get(j);
                }
            }
        }

        for (int i = 0; i < ans.length; i++) {
            for (int j = 0; j < ans[i].length; j++) {
                if (ans[i][j] > columnMin[j]) {
                    ans[i][j] = columnMin[j];
                }
                System.out.print(ans[i][j] + " ");
            }
            System.out.print('\n');
        }

        in.close();
    }
}


