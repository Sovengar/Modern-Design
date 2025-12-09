package jonathan.modern_design._shared.domain.services;

import jonathan.modern_design._shared.domain.vo.Text;

public class TextWrapper {
    public static Text wrapForConsole(String text) {
        return wrap(text, 60);
    }

    public static Text wrapForEmail(String text) {
        return wrap(text, 78);
    }

    public static Text wrapForReporting(String text) {
        return wrap(text, 72);
    }

    public static Text wrap(String text, int columnWidth) {
        return wrap(new Text(text), columnWidth);
    }

    public static Text wrap(Text text, int columnWidth) {
        if (text.fitsIn(columnWidth)) {
            return text;
        } else {
            Text wrappedText = text.wrapFirstLine(columnWidth);
            Text remainingText = text.removeFirstLine(columnWidth);
            return wrappedText.concat(wrap(remainingText, columnWidth));
        }
    }
}
