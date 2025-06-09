package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;

/**
 * Represents a command to clear the screen of output for a specific user. Inherits from the
 * abstract Command class and provides implementation for executing the command to clear the console
 * output.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class ClearCommand extends Command {

  /**
   * Constructs a ClearCommand instance for the specified user.
   *
   * @param user the user associated with this command.
   */
  protected ClearCommand(User user) {
    super(user);
  }

  /**
   * Executes the clear screen command by invoking the output handler to clear the console output.
   */
  @Override
  public void execute() {
    getOutputHandler().clearScreen();
  }
}
