package dev.nheggoe.mealplanner.user;

/**
 * Represents an entity that can provide a name as a string.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public interface Printable {
  /**
   * Retrieves the name associated with the implementing entity.
   *
   * @return the name as a string.
   */
  String getName();
}
