package dev.nheggoe.mealplanner.util;

import dev.nheggoe.mealplanner.user.inventory.Ingredient;
import java.util.Random;

/**
 * A utility class providing methods to manipulate strings and objects.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class Utility {
  private static Random random;

  private Utility() {}

  /**
   * Converts the input string to lowercase if it is not null.
   *
   * @param s the input string to be converted.
   * @return the lowercase version of the input string if not null, otherwise null.
   */
  public static String createKey(String s) {
    return (s == null) ? null : s.strip().toLowerCase();
  }

  /**
   * Generates a lowercase key for the given Ingredient.
   *
   * @param o the Ingredient object
   * @return the lowercase name of the Ingredient, or null if the Ingredient is null.
   */
  public static String createKey(Ingredient o) {
    return (o == null) ? null : o.getName().strip().toLowerCase();
  }

  /**
   * Determines the ordinal suffix for a given number.
   *
   * @param number the number to determine the ordinal suffix for
   * @return a string representing the ordinal suffix ("st", "nd", "rd", or "th")
   */
  public static String getOrdinalSuffix(int number) {
    if (number % 100 >= 11 && number % 100 <= 13) {
      return "th";
    }
    return switch (number % 10) {
      case 1 -> "st";
      case 2 -> "nd";
      case 3 -> "rd";
      default -> "th";
    };
  }

  /**
   * Capitalizes the first letter of each word in the provided input string.
   *
   * @param input the string to process, where each word's first letter will be capitalized
   * @return a new string with each word's first letter capitalized, or the original string if it is
   *     null or empty
   */
  public static String capitalizeEachWord(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    String[] words = input.split("\\s+"); // Splitting on spaces
    StringBuilder capitalizedSentence = new StringBuilder();

    for (String word : words) {
      if (!word.isEmpty()) {
        capitalizedSentence
            .append(Character.toUpperCase(word.charAt(0)))
            .append(word.substring(1))
            .append(" ");
      }
    }

    return capitalizedSentence.toString().strip(); // Trim the trailing space
  }

  /**
   * Determines if the given input indicates continuation based on specific keywords.
   *
   * @param input the input string to check for continuation confirmation; can be null
   * @return true if the input contains confirmation keywords ("yes", "y", "ja"), false otherwise
   */
  public static boolean isContinuationConfirmed(String input) {
    return switch (input) {
      case String s when s.toLowerCase().contains("yes") -> true;
      case String s when s.toLowerCase().contains("y") -> true;
      case String s when s.toLowerCase().contains("ja") -> true;
      case null, default -> false;
    };
  }

  /**
   * Returns a singleton instance of the Random class.
   *
   * @return a singleton Random object.
   */
  public static Random getInstanceOfRandom() {
    if (random == null) {
      random = new Random();
    }
    return random;
  }

  /**
   * Rounds a float value to two decimal places.
   *
   * @param input the float value to be rounded
   * @return the rounded float value with two decimal places
   */
  public static float roundToTwoDecimal(float input) {
    return (float) Math.round(input * 100) / 100;
  }
}
