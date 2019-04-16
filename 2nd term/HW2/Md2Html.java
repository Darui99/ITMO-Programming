package md2html;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.*;

public class Md2Html {
    private int ptr, paragraphNum;
    private String text;
    private HashSet<Character> markupSymbols;
    private HashMap<String, Pair> markupMatching;
    private HashMap<Character, String> specialSymbolsMatching;

    public void setText(String inp) {
        text = inp;
        ptr = 0;
        paragraphNum = 0;
    }

    public Md2Html() {
        markupSymbols = new HashSet<>();
        markupMatching = new HashMap<>();
        specialSymbolsMatching = new HashMap<>();

        markupSymbols.add('*');
        markupSymbols.add('_');
        markupSymbols.add('-');
        markupSymbols.add('`');

        markupMatching.put("*", new Pair("<em>", "</em>"));
        markupMatching.put("**", new Pair("<strong>", "</strong>"));
        markupMatching.put("_", new Pair("<em>", "</em>"));
        markupMatching.put("__", new Pair("<strong>", "</strong>"));
        markupMatching.put("--", new Pair("<s>", "</s>"));
        markupMatching.put("`", new Pair("<code>", "</code>"));

        specialSymbolsMatching.put('<', "&lt;");
        specialSymbolsMatching.put('>', "&gt;");
        specialSymbolsMatching.put('&', "&amp;");
    }

    private void skipTrash() {
        while(ptr < text.length() && Character.getType(text.charAt(ptr)) == 16) {
            ptr++;
        }
    }

    private void skipCharacter(char c) {
        while(ptr < text.length() && text.charAt(ptr) == c) {
            ptr++;
        }
    }

    private int skipCharacter(String s, int begin, char c) {
        int res = 0;
        for (int i = begin; i < s.length() && s.charAt(i) == c; i++)
            res++;
        return res;
    }

    public String parse() throws ParsingException {
        skipCharacter('\n');

        StringBuilder res = new StringBuilder();

        String curPar;
        while (ptr < text.length()) {
            curPar = getNextParagraph();
            if (curPar.length() == 0) {
                continue;
            }
            res.append(parseParagraph(curPar));
            res.append("\n");
        }

        while(res.length() > 0 && res.charAt(res.length() - 1) == '\n') {
            res.delete(res.length() - 1, res.length());
        }
        return res.toString();
    }

    private String processImage(String s, int begin) throws ParsingException {
        StringBuilder res = new StringBuilder();
        StringBuilder ans = new StringBuilder();
        for (int i = begin; i < s.length(); i++) {
            if (s.charAt(i) == '[') {
                for (i++; i < s.length() && s.charAt(i) != ']'; i++)
                    res.append(s.charAt(i));
                ans.append("<img alt='").append(res.toString()).append("' src='");
                res.delete(0, res.length());
            }
            if (i == s.length())
                throw new ParsingException("Incorrect image using.");
            if (s.charAt(i) == '(') {
                for (i++; i < s.length() && s.charAt(i) != ')'; i++)
                    res.append(s.charAt(i));
                ans.append(res.toString()).append("'>");
                break;
            }
        }
        return ans.toString();
    }

    public String parseParagraph(String parText) throws ParsingException {
        paragraphNum++;
        String curMarkupString;
        if (parText.length() == 0)
            return "";

        int pos = 0, headerNumber = skipCharacter(parText, 0, '#'), curLen, paragraphsBegin = 0;
        if (headerNumber == parText.length())
            throw new ParsingException(paragraphNum + " paragraph contains only #.");

        int spacesAfterSharps = skipCharacter(parText, headerNumber, ' ');
        if (headerNumber > 0) {
            if (spacesAfterSharps == 0) {
                headerNumber = 0;
            } else {
                paragraphsBegin = headerNumber + spacesAfterSharps;
            }
        }

        HashMap<String, Integer> markupStringsInText = new HashMap<>();
        HashMap<Integer, Boolean> isMarkup = new HashMap<>();

        pos = paragraphsBegin;
        while (pos < parText.length()) {
            if (markupSymbols.contains(parText.charAt(pos))) {
                if (pos > 0 && parText.charAt(pos - 1) == '\\') {
                    pos++;
                    continue;
                }
                curLen = skipCharacter(parText, pos, parText.charAt(pos));
                curMarkupString = parText.substring(pos, pos + curLen);
                if (!markupMatching.containsKey(curMarkupString)) {
                    isMarkup.put(pos, false);
                } else {
                    if (markupStringsInText.containsKey(curMarkupString)) {
                        isMarkup.put(pos, true);
                        isMarkup.put(markupStringsInText.get(curMarkupString), true);
                        markupStringsInText.remove(curMarkupString);
                    } else {
                        markupStringsInText.put(curMarkupString, pos);
                    }
                }
                pos += curLen;
            } else {
                pos++;
            }
        }

        for (HashMap.Entry<String, Integer> elem : markupStringsInText.entrySet()) {
            isMarkup.put(elem.getValue(), false);
        }

        StringBuilder res = new StringBuilder();

        if (headerNumber == 0) {
            res.append("<p>");
        } else {
            res.append("<h" + headerNumber + ">");
        }

        HashMap<String, Boolean> open = new HashMap<>();
        pos = paragraphsBegin;
        while (pos < parText.length()) {
            if (markupSymbols.contains(parText.charAt(pos))) {
                if (pos > 0 && parText.charAt(pos - 1) == '\\') {
                    res.delete(res.length() - 1, res.length());
                    res.append(parText.charAt(pos));
                    pos++;
                    continue;
                }
                curLen = skipCharacter(parText, pos, parText.charAt(pos));
                curMarkupString = parText.substring(pos, pos + curLen);
                if (isMarkup.get(pos)) {
                    if (!open.containsKey(curMarkupString))
                        open.put(curMarkupString, false);
                    if (!open.get(curMarkupString)) {
                        res.append(markupMatching.get(curMarkupString).getFirst());
                        open.put(curMarkupString, true);
                    }
                    else {
                        res.append(markupMatching.get(curMarkupString).getSecond());
                        open.put(curMarkupString, false);
                    }
                } else {
                    res.append(curMarkupString);
                }
                pos += curLen;
            } else {
                if (specialSymbolsMatching.containsKey(parText.charAt(pos))) {
                    res.append(specialSymbolsMatching.get(parText.charAt(pos)));
                    pos++;
                    continue;
                }
                if (parText.charAt(pos) == '!') {
                    if (pos == parText.length() - 1 || parText.charAt(pos + 1) != '[') {
                        res.append(parText.charAt(pos));
                        pos++;
                        continue;
                    }
                    String curImage = processImage(parText, pos);
                    if (curImage.length() == 0) {
                        res.append(parText.charAt(pos));
                        pos++;
                        continue;
                    }
                    res.append(curImage);
                    while(pos < parText.length() && parText.charAt(pos) != ')')
                        pos++;
                    pos++;
                    continue;
                }
                res.append(parText.charAt(pos));
                pos++;
            }
        }

        if (headerNumber == 0) {
            res.append("</p>");
        } else {
            res.append("</h" + headerNumber + ">");
        }

        return res.toString();
    }

    public String getNextParagraph() {
        StringBuilder res = new StringBuilder();
        boolean prevWasNewline = false;
        while(ptr < text.length()) {
            skipTrash();
            skipCharacter('\r');
            if (text.charAt(ptr) == '\n') {
                if (!prevWasNewline) {
                    prevWasNewline = true;
                    res.append(text.charAt(ptr++));
                } else {
                    skipCharacter('\n');
                    while (res.length() > 0 && Character.getType(res.charAt(res.length() - 1)) == Character.CONTROL) {
                        res.deleteCharAt(res.length() - 1);
                    }
                    break;
                }
            } else {
                res.append(text.charAt(ptr++));
                prevWasNewline = false;
            }
        }

        while(res.length() > 0 && res.charAt(res.length() - 1) == '\n')
            res.delete(res.length() - 1, res.length());
        return res.toString();
    }

    public String getText() {
        return text;
    }

    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    public static void main(String args[]) {
        Md2Html q = new Md2Html();
        StringBuilder s = new StringBuilder();
        try (Scanner in = new Scanner(new File(args[0]), StandardCharsets.UTF_8)) {
            while(in.hasNextLine()) {
                s.append(in.nextLine()).append('\n');
            }
            q.setText(s.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(new File(args[1]), StandardCharsets.UTF_8)) {
            String jojo = q.parse();
            for (int i = 0; i < jojo.length(); i++) {
                if (jojo.charAt(i) != '\n')
                    out.print(jojo.charAt(i));
                else
                    out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
