import javafx.util.Pair;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.PrintWriter;

public class WordStatLineIndex {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("You have to write only input's and output's names.");
            return;
        }

        ByteFastScanner in = new ByteFastScanner(args[0]);

        TreeMap<String, ArrayList<Pair<Integer, Integer>>> have = new TreeMap<>();

        int numString = 1;
        while(in.hasNext()) {
            ArrayList<String> cur = in.nextLineAsWords();
            for (int i = 0; i < cur.size(); i++) {
                if (!have.containsKey(cur.get(i))) {
                    have.put(cur.get(i), new ArrayList<>());
                }
                have.get(cur.get(i)).add(new Pair<>(numString, i + 1));
            }
            numString++;
        }

        try (PrintWriter out = new PrintWriter(new File(args[1]), "utf8")) {

            for (Map.Entry<String, ArrayList<Pair<Integer, Integer>>> elem : have.entrySet()) {
                out.print(elem.getKey() + " ");
                out.print(elem.getValue().size() + " ");
                for (int i = 0; i < elem.getValue().size() - 1; i++) {
                    out.print(elem.getValue().get(i).getKey() + ":" + elem.getValue().get(i).getValue() + " ");
                }
                out.println(elem.getValue().get(elem.getValue().size() - 1).getKey() + ":" + elem.getValue().get(elem.getValue().size() - 1).getValue());
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

        in.close();
    }
}
