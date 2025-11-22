package util;

public final class ValidatorUtil {
    private ValidatorUtil() {}

    public static boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isPositive(int n) {
        return n > 0;
    }

    public static boolean isInteger(String s) {
        if (s == null) return false;
        try { Integer.parseInt(s.trim()); return true; } catch (Exception e) { return false; }
    }
}
