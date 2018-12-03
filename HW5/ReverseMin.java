import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;

public class ReverseMin {

    public static void main(String[] args) {

        FastScanner in = new FastScanner();
        //Scanner in = new Scanner(System.in);

        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
        while (in.hasNext()) {
            arr.add(in.nextLineAsIntegers());
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
    } // C:\obligatoriness\idea\Scanner\src
}


