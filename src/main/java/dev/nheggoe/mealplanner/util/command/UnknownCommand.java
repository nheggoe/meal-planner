package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;

/**
 * Represents a command that is executed when a user's input cannot be matched to any known command.
 * It provides feedback to the user by printing a help message that corresponds to the unrecognized
 * command input.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class UnknownCommand extends Command {

  /**
   * Constructs an UnknownCommand instance tied to a specific user.
   *
   * @param user The user associated with this unknown command.
   */
  public UnknownCommand(User user) {
    super(user);
  }

  /**
   * Executes the unknown command by printing a help message specific to the command. This method is
   * called when the user's input does not match any recognized command.
   */
  @Override
  public void execute() {
    getOutputHandler().printCommandHelpMessage(getCommand());
  }
}
