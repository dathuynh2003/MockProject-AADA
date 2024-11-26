package ojt.aada.mockproject.utils;

import android.annotation.SuppressLint;

import ojt.aada.mockproject.R;

public class FunctionUtils {
    public static int displayStar(boolean isFavorite) {
        return isFavorite ? R.drawable.ic_yellow_star_rate_50dp : R.drawable.ic_star_border_50dp;
    }

    @SuppressLint("DefaultLocale")
    public static String displayRating(double rating) {
        return String.format("%.1f/10.0", rating);
    }
}
