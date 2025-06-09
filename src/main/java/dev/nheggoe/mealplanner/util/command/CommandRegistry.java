package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.util.Utility;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers and stores valid commands for look-up and retrieval.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class CommandRegistry {

  private static final Map<String, ValidCommand> COMMAND_MAP = new HashMap<>();

  static {
    initializeValidCommand();
  }

  private CommandRegistry() {}

  /**
   * Finds and returns the corresponding ValidCommand for the given input string. If the input is
   * not associated with a valid command, returns ValidCommand.UNKNOWN.
   *
   * @param input the string input for which a corresponding ValidCommand is sought
   * @return the ValidCommand corresponding to the input string, or ValidCommand.UNKNOWN if no match
   *     is found
   */
  public static ValidCommand findCommand(String input) {
    return COMMAND_MAP.getOrDefault(Utility.createKey(input), ValidCommand.UNKNOWN);
  }

  /**
   * Initializes the command map by adding all valid commands, except UNKNOWN, to it. Utilizes the
   * ValidCommand enum values to populate the map for quick retrieval.
   */
  private static void initializeValidCommand() {
    Arrays.stream(ValidCommand.values())
        .filter(command -> command != ValidCommand.UNKNOWN)
        .forEach(CommandRegistry::addCommandToMap);
  }

  /**
   * Adds a valid command to the command map using a generated key.
   *
   * @param command the ValidCommand to be added to the map
   */
  private static void addCommandToMap(ValidCommand command) {
    String key = Utility.createKey(command.name());
    COMMAND_MAP.put(key, command);
  }

  /**
   * Retrieves a list of command keys derived from the names of the ValidUnit enumeration constants.
   * The keys are the lowercase versions of the enumeration constant names.
   *
   * @return a list of strings representing the command keys of all ValidUnit enum values.
   */
  public static List<String> getCommandList() {
    return Arrays.stream(ValidUnit.values()).map(Enum::name).map(Utility::createKey).toList();
  }
}
