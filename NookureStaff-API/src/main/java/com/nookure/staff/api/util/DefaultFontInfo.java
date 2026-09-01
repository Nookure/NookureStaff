package com.nookure.staff.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum DefaultFontInfo {
    A('A', 5),
    a('a', 5),
    B('B', 5),
    b('b', 5),
    C('C', 5),
    c('c', 5),
    D('D', 5),
    d('d', 5),
    E('E', 5),
    e('e', 5),
    F('F', 5),
    f('f', 4),
    G('G', 5),
    g('g', 5),
    H('H', 5),
    h('h', 5),
    I('I', 3),
    i('i', 1),
    J('J', 5),
    j('j', 5),
    K('K', 5),
    k('k', 4),
    L('L', 5),
    l('l', 1),
    M('M', 5),
    m('m', 5),
    N('N', 5),
    n('n', 5),
    n_virgulilla('ñ', 5),
    N_virgulilla('Ñ', 5),
    O('O', 5),
    o('o', 5),
    P('P', 5),
    p('p', 5),
    Q('Q', 5),
    q('q', 5),
    R('R', 5),
    r('r', 5),
    S('S', 5),
    s('s', 5),
    T('T', 5),
    t('t', 4),
    U('U', 5),
    u('u', 5),
    V('V', 5),
    v('v', 5),
    W('W', 5),
    w('w', 5),
    X('X', 5),
    x('x', 5),
    Y('Y', 5),
    y('y', 5),
    Z('Z', 5),
    z('z', 5),
    NUM_1('1', 5),
    NUM_2('2', 5),
    NUM_3('3', 5),
    NUM_4('4', 5),
    NUM_5('5', 5),
    NUM_6('6', 5),
    NUM_7('7', 5),
    NUM_8('8', 5),
    NUM_9('9', 5),
    NUM_0('0', 5),
    EXCLAMATION_POINT('!', 1),
    AT_SYMBOL('@', 6),
    NUM_SIGN('#', 5),
    DOLLAR_SIGN('$', 5),
    PERCENT('%', 5),
    UP_ARROW('^', 5),
    AMPERSAND('&', 5),
    ASTERISK('*', 5),
    LEFT_PARENTHESIS('(', 4),
    RIGHT_PERENTHESIS(')', 4),
    MINUS('-', 5),
    UNDERSCORE('_', 5),
    PLUS_SIGN('+', 5),
    EQUALS_SIGN('=', 5),
    LEFT_CURL_BRACE('{', 4),
    RIGHT_CURL_BRACE('}', 4),
    LEFT_BRACKET('[', 3),
    RIGHT_BRACKET(']', 3),
    COLON(':', 1),
    SEMI_COLON(';', 1),
    DOUBLE_QUOTE('"', 3),
    SINGLE_QUOTE('\'', 1),
    LEFT_ARROW('<', 4),
    RIGHT_ARROW('>', 4),
    START_QUESTION_MARK('¿', 5),
    FINISH_QUESTION_MARK('?', 5),
    SLASH('/', 5),
    BACK_SLASH('\\', 5),
    LINE('|', 1),
    TILDE('~', 5),
    TICK('`', 2),
    PERIOD('.', 1),
    COMMA(',', 1),
    SPACE(' ', 3),
    DEFAULT('a', 4),
    // Accented spanish characters
    ACCENTED_a('á', 5),
    ACCENTED_A('Á', 5),
    ACCENTED_e('é', 5),
    ACCENTED_E('É', 5),
    ACCENTED_i('í', 3),
    ACCENTED_I('Í', 3),
    ACCENTED_o('ó', 5),
    ACCENTED_O('Ó', 5),
    ACCENTED_u('ú', 5),
    ACCENTED_U('Ú', 5);
    private static final int CENTER_PX = 154;
    private static final Pattern pattern = Pattern.compile("<([^>]*)>");
    private final char character;
    private final int length;

    DefaultFontInfo(char character, int length) {
        this.character = character;
        this.length = length;
    }

    public char getCharacter() {
        return this.character;
    }

    public int getLength() {
        return this.length;
    }

    public int getBoldLength() {
        if (this == DefaultFontInfo.SPACE) return this.getLength();
        return this.length + 1;
    }

    public static DefaultFontInfo getDefaultFontInfo(char c) {
        for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
            if (dFI.getCharacter() == c) return dFI;
        }

        return DefaultFontInfo.DEFAULT;
    }

    public static String centerIfContains(String message) {
        if (message.contains("{center}")) {
            message = message.replace("{center}", "");
            if (message.contains("\n")) {
                String[] lines = message.split("\n");
                StringBuilder centered = new StringBuilder();
                for (String line : lines) {
                    centered.append(DefaultFontInfo.center(line)).append("\n");
                }
                message = centered.toString();
            } else {
                message = DefaultFontInfo.center(message);
            }
        }

        return message;
    }

    public static String center(String message) {
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : removeTags(message).toCharArray()) {
            if (c == ' ') continue;

            if (c == '§') {
                previousCode = true;
            }

            if (previousCode && c == 'l') {
                isBold = true;
                previousCode = false;
            }

            if (previousCode && c == 'r') {
                isBold = false;
                previousCode = false;
            }

            DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
            messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
            messagePxSize++;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;

        StringBuilder finalSb = new StringBuilder();

        for (int compensated = 0; compensated < toCompensate; compensated += spaceLength) {
            finalSb.append(" ");
        }

        return finalSb + message.replace("[|", "<").replace("|]", ">");
    }

    public static String removeTags(String input) {
        Matcher matcher = pattern.matcher(input);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String tagContent = matcher.group(1);
            switch (tagContent.toLowerCase()) {
                case "bold":
                case "b":
                    matcher.appendReplacement(result, Matcher.quoteReplacement("§l"));
                    break;
                case "/bold":
                case "/b":
                    matcher.appendReplacement(result, Matcher.quoteReplacement("§r"));
                    break;
                default:
                    matcher.appendReplacement(result, "");
                    break;
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
