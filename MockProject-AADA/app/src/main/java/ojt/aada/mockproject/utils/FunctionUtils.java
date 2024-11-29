package ojt.aada.mockproject.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import java.util.Date;
import java.util.Locale;

import ojt.aada.mockproject.R;

public class FunctionUtils {
    public static int displayStar(boolean isFavorite) {
        return isFavorite ? R.drawable.ic_yellow_star_rate_50dp : R.drawable.ic_star_border_50dp;
    }

    public static String displayRating(double rating) {
        return String.format(Locale.getDefault(),"%.1f/10", rating);
    }

    @BindingAdapter("bitmap")
    public static void displayAvatar(ImageView imageView, String avatar) {
        imageView.setImageBitmap(ImageValidator.base64ToBitmap(avatar));
    }

    public static String displayDateFromMillisecond(long time, String pattern) {
        return Validator.convertDate(new Date(time), pattern);


    }
}
