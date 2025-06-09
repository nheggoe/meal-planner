package dev.nheggoe.mealplanner.util;

/**
 * AbortException is a custom exception used to indicate that an operation has been aborted. This
 * exception can be thrown when a process needs to be terminated while in input mode simply by
 * typing "abort".
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class AbortException extends RuntimeException {

  /**
   * Constructs an AbortException with a default message indicating that the operation has been
   * aborted.
   */
  public AbortException() {
    super("Operation aborted.");
  }

  /**
   * Constructs an AbortException with a specified detail message.
   *
   * @param message the detail message explaining the reason for the exception.
   */
  public AbortException(String message) {
    super(message);
  }
}
