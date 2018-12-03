import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.PrintWriter;

public class FastScanner {

    final private int BUFFER_SIZE = 4096;
    private InputStreamReader in;
    private char[] buffer;
    private int bufferPointer, bytesRead;

    public FastScanner() {
        in = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        buffer = new char[BUFFER_SIZE];
        bufferPointer = 0;
        bytesRead = 0;
    }

    public FastScanner(String name) {
        try {
            in = new InputStreamReader(new FileInputStream(name), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        buffer = new char[BUFFER_SIZE];
        bufferPointer = 0;
        bytesRead = 0;
    }

    private void fillBuffer() {
        bufferPointer = 0;
        try {
            while ((bytesRead = in.read(buffer)) == 0) {}
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bytesRead == -1)
            buffer[0] = 0;
    }

    private char next() {
        if (bufferPointer >= bytesRead) {
            fillBuffer();
        }
        return buffer[bufferPointer++];
    }

    public boolean hasNext() {
        if (bufferPointer >= bytesRead) {
            fillBuffer();
            return (bytesRead != -1);
        }
        return true;
    }

    public String nextLine() {
        try {
            StringBuilder now = new StringBuilder();
            char c;
            while (true) {
                c = next();
                if (c == 0 || c == '\n') {
                    break;
                }
                now.append(c);
            }
            return now.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private int convert(StringBuilder s) {
        try {
            return Integer.parseInt(s.toString());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ArrayList<Integer> nextLineAsIntegers() {
        try {
            String inp = nextLine();
            StringBuilder now = new StringBuilder();
            ArrayList<Integer> res = new ArrayList<>();

            for (int i = 0; i < inp.length(); i++) {
                if (!(Character.isDigit(inp.charAt(i)) || inp.charAt(i) == '-')) {
                    if (now.length() > 0) {
                        res.add(convert(now));
                        now.delete(0, now.length());
                    }
                } else {
                    now.append(inp.charAt(i));
                }
            }
            if (now.length() > 0) {
                res.add(convert(now));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<String> nextLineAsWords() {
        ArrayList<String> res = new ArrayList<>();

        try {
            StringBuffer now = new StringBuffer("");
            String inp = nextLine();

            for (int i = 0; i < inp.length(); i++) {
                if (!(Character.isLetter(inp.charAt(i)) || Character.getType(inp.charAt(i)) == Character.DASH_PUNCTUATION || inp.charAt(i) == '\'')) {
                    if (now.length() == 0) {
                        continue;
                    }
                    String cur = now.toString().toLowerCase();
                    res.add(cur);
                    now.delete(0, now.length());
                } else {
                    now.append(inp.charAt(i));
                }
            }
            if (now.length() > 0) {
                String cur = now.toString().toLowerCase();
                res.add(cur);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String nextString() {
        while(true) {
            char c = next();
            if (!Character.isWhitespace(c)) {
                bufferPointer--;
                break;
            }
        }
        try {
            StringBuilder now = new StringBuilder();
            char c;
            while (true) {
                c = next();
                if (c == 0 || Character.isWhitespace(c)) {
                    break;
                }
                now.append(c);
            }
            return now.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new String("");
        }
    }

    public int nextInt() {
        try {
            int sign = 1, res = 0;
            char c;
            while (true) {
                c = next();
                if (!Character.isWhitespace(c)) {
                    break;
                }
            }
            if (c == '-') {
                sign = -1;
                c = next();
            }
            while (Character.isDigit(c)) {
                res = res * 10 + (int) (c - '0');
                c = next();
            }
            bufferPointer--;
            if (sign == 1) {
                return res;
            } else {
                return -res;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void close() {
        try {
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
