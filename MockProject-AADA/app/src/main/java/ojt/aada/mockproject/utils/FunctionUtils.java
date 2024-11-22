package ojt.aada.mockproject.utils;

import ojt.aada.mockproject.R;

public class FunctionUtils {
    public static int displayStar(boolean isFavorite) {
        return isFavorite ? R.drawable.ic_yellow_star_rate_50dp : R.drawable.ic_star_border_50dp;
    }

    public static String displayRating(double rating) {
        return rating+"/10.0";
    }
}
