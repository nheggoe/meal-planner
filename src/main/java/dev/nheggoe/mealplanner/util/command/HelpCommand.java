package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;

/**
 * HelpCommand class is responsible for processing help-related commands.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class HelpCommand extends Command {

  /**
   * Constructs a new HelpCommand object for the specified user. The HelpCommand class is
   * responsible for processing help-related commands.
   *
   * @param user The user for whom the help command is being created and processed.
   */
  public HelpCommand(User user) {
    super(user);
  }

  /**
   * Constructs a HelpCommand instance and prints the help message for a specific command.
   *
   * @param user The user for whom the help command is being created and processed.
   * @param command The specific command for which the help message is displayed.
   */
  public HelpCommand(User user, ValidCommand command) {
    super(user);
    getOutputHandler().printCommandHelpMessage(command);
  }

  /**
   * Processes the subcommand for the HelpCommand. If a subcommand is present in the user's input,
   * it prints the specific help message for that subcommand. Otherwise, it prints the general help
   * message.
   */
  @Override
  public void execute() {
    if (hasSubcommand()) {
      String subcommandInput = getSubcommand();
      getOutputHandler().printCommandHelpMessage(CommandRegistry.findCommand(subcommandInput));
    } else {
      getOutputHandler().printHelpMessage();
      getOutputHandler().printOutput("Additionally, try 'help' + {command}");
    }
  }
}
