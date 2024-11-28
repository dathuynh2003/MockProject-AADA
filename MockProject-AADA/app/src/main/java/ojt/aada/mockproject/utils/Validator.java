package ojt.aada.mockproject.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validator {
    public static boolean isValidEmail(String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidDate(String dateStr, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(dateStr);
            //dateStr > curDate -> false
            if (date != null && date.after(new Date())) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isDobValid(String dob) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date currentDate = new Date(); // Get current date

        try {
            Date dobDate = sdf.parse(dob); // Parse the input date string
            return !dobDate.after(currentDate); // Return false if DOB is after current date
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Return false in case of a parsing error
        }
    }

    public static String convertDate(Date date, String pattern) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
