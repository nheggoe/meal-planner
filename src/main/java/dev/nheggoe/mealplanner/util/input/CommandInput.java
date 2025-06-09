package dev.nheggoe.mealplanner.util.input;

import dev.nheggoe.mealplanner.util.command.ValidCommand;

/**
 * Represents a command input consisting of a main command, a subcommand, and an argument.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class CommandInput {
  private ValidCommand command;
  private String subcommand;
  private String argument;

  /**
   * Constructs a Command object with the specified primary command word and a sub-command.
   *
   * @param command The main command word represented by the ValidCommand enum.
   * @param subcommand The optional sub-command as a string.
   */
  public CommandInput(ValidCommand command, String subcommand, String argument) {
    setCommand(command);
    setSubcommand(subcommand);
    setArgument(argument);
  }

  /**
   * Retrieves the main command word associated with this UserInput instance.
   *
   * @return The main command word represented by the ValidCommand enum.
   */
  public ValidCommand getCommand() {
    return command;
  }

  /**
   * Sets the primary command associated with this UserInput instance.
   *
   * @param validCommand The main command word represented by the ValidCommand enum.
   */
  private void setCommand(ValidCommand validCommand) {
    this.command = validCommand;
  }

  /**
   * Retrieves the subcommand associated with this UserInput instance.
   *
   * @return the subcommand as a string, or null if no subcommand is present.
   */
  public String getSubcommand() {
    return subcommand;
  }

  /**
   * Sets the subcommand associated with this UserInput instance.
   *
   * @param subcommand The optional sub-command as a string.
   */
  private void setSubcommand(String subcommand) {
    this.subcommand = subcommand;
  }

  /**
   * Retrieves the full input string associated with this user command.
   *
   * @return the input string representing the entire user input.
   */
  public String getArgument() {
    return argument;
  }

  /**
   * Sets the full input string associated with this user command.
   *
   * @param argument the input string representing the entire user input.
   */
  public void setArgument(String argument) {
    this.argument = argument;
  }

  /**
   * Checks if the UserCommand object contains a subcommand.
   *
   * @return true if a subcommand is present, false otherwise.
   */
  public boolean hasSubcommand() {
    return subcommand != null;
  }

  /**
   * Checks if the command associated with this UserCommand is of type UNKNOWN.
   *
   * @return true if the command is UNKNOWN, false otherwise.
   */
  public boolean isUnknown() {
    return command == ValidCommand.UNKNOWN;
  }
}
