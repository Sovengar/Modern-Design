package jonathan.modern_design._shared.domain.vo;

public class Text {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    public Text wrapFirstLine(int columnWidth) {
        validateColumnWidth(columnWidth);
        var wrappingPoint = getWrappingPoint(columnWidth);
        return new Text(text.substring(0, Math.min(wrappingPoint, text.length())).trim() + "\n");
    }

    public Text removeFirstLine(int columnWidth) {
        validateColumnWidth(columnWidth);

        if (text.length() <= columnWidth) {
            return new Text("");
        }

        var wrappingPoint = getWrappingPoint(columnWidth);
        return new Text(text.substring(wrappingPoint).trim());
    }

    public boolean fitsIn(int columnWidth) {
        validateColumnWidth(columnWidth);
        return text.length() <= columnWidth;
    }

    public Text concat(Text other) {
        return new Text(text + other.text);
    }

    private int getWrappingPoint(int columnWidth) {
        validateColumnWidth(columnWidth);

        int lastSpace = text.lastIndexOf(" ");

        if (lastSpace == -1) {
            return columnWidth;
        }

        return Math.min(lastSpace, columnWidth);
    }

    private void validateColumnWidth(final int columnWidth) {
        if (columnWidth <= 0) {
            throw new IllegalArgumentException("columnWidth must be positive");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Text text1 = (Text) other;
        return text.equals(text1.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public String toString() {
        return text;
    }

}
