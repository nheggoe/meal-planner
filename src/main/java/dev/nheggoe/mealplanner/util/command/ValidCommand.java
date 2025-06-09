package dev.nheggoe.mealplanner.util.command;

import java.util.Arrays;
import java.util.List;

/**
 * Holds the every valid command.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public enum ValidCommand {
  UNKNOWN("Unknown command, see 'help'"),

  HELP(
      """
      Valid print commands are:
       help | help {valid command}"""),

  ADD(
      """
      Valid add commands are:
       add storage | add ingredient | add recipe"""),

  FIND(
      """
      Valid find commands are:
       find ingredient {ingredient name} | find recipe {recipe name}"""),

  STATS(""),

  GO(
      """
      Valid go commands are:
       go to {storage name} | go back"""),

  LIST(
      """
      Valid list commands are:
       list all | list storage | list recipe | list ingredient
       list expired | list available | list value | list name"""),

  REMOVE(
      """
      Valid remove commands are:
       remove storage {storage name}
       remove ingredient {ingredient name}
       remove recipe {recipe name}
       remove expired"""),

  CLEAR("This command will clear the terminal window."),

  EXIT("This command will terminate the application.");

  private final String helpString;

  ValidCommand(String helpString) {
    this.helpString = helpString;
  }

  public String getHelpString() {
    return helpString;
  }

  public static List<String> getCommands() {
    return Arrays.stream(ValidCommand.values())
        .filter(command -> command != HELP)
        .filter(command -> command != EXIT)
        .filter(command -> command != UNKNOWN)
        .filter(command -> command != CLEAR)
        .map(ValidCommand::name)
        .map(String::toLowerCase)
        .toList();
  }
}
