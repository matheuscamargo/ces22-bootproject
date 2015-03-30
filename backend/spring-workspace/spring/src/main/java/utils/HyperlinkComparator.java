package utils;

import java.util.Comparator;

import model.Hyperlink;

//custom class for comparing Hyperlinks by date
public class HyperlinkComparator implements Comparator<Hyperlink> {
	@Override
    public int compare(Hyperlink h1, Hyperlink h2) {
        return h1.getLastEditedAt().compareTo(h2.getLastEditedAt());
    }
};
