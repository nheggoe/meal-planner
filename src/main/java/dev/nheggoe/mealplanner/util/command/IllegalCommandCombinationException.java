package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.util.Utility;
import dev.nheggoe.mealplanner.util.input.CommandInput;

/**
 * Exception thrown to indicate an illegal combination of command and subcommand. This exception is
 * a runtime exception thrown when a combination of command and subcommand is not valid within the
 * application's context. When this exception is thrown, a help message related to the invalid
 * command is printed using the provided OutputHandler instance.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class IllegalCommandCombinationException extends RuntimeException {

  /**
   * Constructor for IllegalCommandCombinationException. Initializes an exception with a message
   * indicating the invalid combination of commands.
   *
   * @param command the main command that caused the exception
   * @param subcommand the subcommand that caused the exception
   */
  public IllegalCommandCombinationException(ValidCommand command, String subcommand) {
    super(
        "Invalid command combination: '%s' + '%s'"
            .formatted(Utility.createKey(command.name()), subcommand));
  }

  /**
   * Constructs an IllegalCommandCombinationException with a message indicating an invalid
   * combination of a command and subcommand.
   *
   * @param commandInput the input containing the main command and subcommand
   */
  public IllegalCommandCombinationException(CommandInput commandInput) {
    this(commandInput.getCommand(), commandInput.getSubcommand());
  }
}
