package ojt.aada.mockproject_aada.utils;

import ojt.aada.mockproject_aada.R;

public class FunctionUtils {
    public static int displayStar(boolean isFavorite) {
        return isFavorite ? R.drawable.ic_yellow_star_rate_50dp : R.drawable.ic_star_border_50dp;
    }

    public static String displayRating(double rating) {
        return rating+"/10.0";
    }
}
