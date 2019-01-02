package com.ahmetkilic.ophelia.ea_utilities.tools;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class StringUtils {

    /**
     * Reformat a phone number with spaces
     *
     * @param number phone number string
     */
    public static String formatPhoneNumberVisibility(String number) {
        if (isEmptyString(number))
            return "";
        if (number.matches("\\d+(?:\\.\\d+)?")) {
            if (number.length() == 10) {
                number = "0" + number.replace(" ", "");
            }
            if (number.length() == 11) {
                number = number.substring(0, 4) + " " + number.substring(4, 7) + " " + number.substring(7, 9) + " " + number.substring(9, 11);
            }
        }

        return number;
    }

    /**
     * Capitalize first character of string
     *
     * @param s string to capitalize
     */
    public static String capitalize(String s) {
        if (isEmptyString(s)) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * Remove html tags from a string
     *
     * @param html string to strip
     */
    public static String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Spanned str = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
            return str.toString().replaceAll("\\n", "");
        } else {
            Spanned str = Html.fromHtml(html);
            return str.toString().replaceAll("\\n", "");
        }
    }

    /**
     * If a string is null or equals to string 'null' , return empty string
     *
     * @param string string to check
     */
    public static String setEmptyStringIfNull(String string) {
        String result = "";
        if (string != null && !string.toLowerCase().equals("null"))
            result = string;
        return result;
    }

    /**
     * Check if a string is null or empty string
     *
     * @param string string to check
     */
    public static boolean isEmptyString(String string) {
        return string == null || string.equals("");
    }

    /**
     * Check if a string is numeric
     *
     * @param string string to check
     */
    public static boolean isNumeric(String string) {
        return !isEmptyString(string) && string.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Remove last character of a string
     *
     * @param string string to check
     */
    public static String removeLastCharacter(String string) {
        if (!isEmptyString(string))
            return string.substring(0, string.length() - 1);
        else
            return "";
    }

    /**
     * Remove last two character of a string
     *
     * @param string string to check
     */
    public static String removeLastTwoCharacter(String string) {
        if (!isEmptyString(string) && string.length() > 1)
            return string.substring(0, string.length() - 2);
        else
            return "";
    }

    /**
     * Remove first character of a string
     *
     * @param string string to check
     */
    public static String removeFirstCharacter(String string) {
        if (!isEmptyString(string))
            return string.substring(1, string.length());
        else
            return "";
    }

    /**
     * Remove first and last character of a string
     *
     * @param string string to check
     */
    public static String removeFirstAndLastCharacter(String string) {
        if (!isEmptyString(string) && string.length() > 1)
            return string.substring(1, string.length() - 1);
        else if (!isEmptyString(string) && string.length() > 0)
            return string;
        else
            return "";
    }
}
