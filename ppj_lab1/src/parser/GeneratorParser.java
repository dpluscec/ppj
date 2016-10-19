package parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dominik on 19.10.2016..
 */
public class GeneratorParser {
    private static final Pattern DEFINITION_NAME = Pattern.compile("\\{(\\w+)\\}");
    private static final Pattern DEFINITION =
            Pattern.compile("^" + DEFINITION_NAME + " (.+)$");
    private static final Pattern STATE = Pattern.compile("S_\\w+");
    private static final Pattern TOKEN = Pattern.compile("\\w+");
    private static final Pattern RULE_DEFINITION = Pattern.compile("^<(" + STATE + ")>(.+)$");

    public static Map<String, String> definitions = new HashMap<>();

    static {
        definitions.put("sviZnakovi",
                "\\\\\\(|\\\\\\)|\\\\\\{|\\\\\\}|\\\\\\||\\\\\\\\|\\\\\\*|\\\\\\$");
        definitions.put("bjelina", "\t|\n");
    }

    public static List<String> states = new ArrayList<>();
    public static List<String> tokens = new ArrayList<>();
    public static List<Rule> rules = new ArrayList<>();

    static int lineNum = 0;

    public static void parse(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        parse(lines);
    }

    public static void parse(List<String> lines) {
        Tuple<String, String> def = getDefinition(lines.get(lineNum));
        while (def != null) {
            definitions.put(def.left, def.right);
            lineNum++;
            def = getDefinition(lines.get(lineNum));
        }

        readStates(lines.get(lineNum));
        lineNum++;

        readTokens(lines.get(lineNum));
        lineNum++;

        int len = lines.size();
        while (lineNum < len) {
            Rule rule = getRule(lines);
            rules.add(rule);
        }
    }

    private static Rule getRule(List<String> lines) {
        Matcher m = RULE_DEFINITION.matcher(lines.get(lineNum));
        m.matches();
        String state = m.group(1);
        String regex = cleanRegex(m.group(2));
        String token;
        lineNum += 2;

        String line = lines.get(lineNum).trim();
        if (line.equals("-")) {
            token = "REJECT";
        } else {
            token = line;
        }
        lineNum++;
        line = lines.get(lineNum).trim();

        Map<Action, String> actions = new HashMap<>();
        while (!line.equals("}")) {
            String[] data = line.split(" ");
            Action action = Action.valueOf(data[0]);
            String arg = null;
            if (action.isArg()) {
                arg = data[1];
            }

            actions.put(action, arg);
            lineNum++;
            line = lines.get(lineNum).trim();
        }
        lineNum++;

        return new Rule(state, regex, token, actions);
    }

    private static void readTokens(String s) {
        s = s.replace("%L", "");

        Matcher m = TOKEN.matcher(s);
        while (m.find()) {
            tokens.add(m.group());
        }
    }

    private static void readStates(String s) {
        s = s.replace("%X", "");

        Matcher m = STATE.matcher(s);
        while (m.find()) {
            states.add(m.group());
        }
    }

    static Tuple<String, String> getDefinition(String line) {
        Matcher matcher = DEFINITION.matcher(line);

        boolean matches = matcher.matches();
        if (!matches) {
            return null;
        }

        String name = matcher.group(1);
        String regex = matcher.group(2);
        regex = cleanRegex(regex);

        return new Tuple<>(name, regex);
    }

    static String cleanRegex(String regex) {
        Matcher matcher = DEFINITION_NAME.matcher(regex);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String name = matcher.group(1);
            matcher.appendReplacement(sb, "(" + definitions.get(name) + ")");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    public static class Tuple<T, U> {
        private T left;
        private U right;

        public Tuple(T left, U right) {
            this.left = left;
            this.right = right;
        }

        public T getLeft() {
            return left;
        }

        public U getRight() {
            return right;
        }
    }
}