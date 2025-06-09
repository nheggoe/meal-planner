package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;

/**
 * The StatsCommand class extends the Command class and is responsible for executing a command that
 * prints user statistics.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class StatsCommand extends Command {

  /**
   * Constructs a StatsCommand object associated with the specified user.
   *
   * @param user the user for whom the command will operate.
   */
  public StatsCommand(User user) {
    super(user);
  }

  /** Executes the stats command by invoking printStats to display user statistics. */
  @Override
  public void execute() {
    printStats();
  }

  /**
   * Prints the total value of wasted ingredients to the output handler. Retrieves the wasted value
   * from the user and formats it into a string message. The message displays the total value in
   * currency and prints the message.
   */
  private void printStats() {
    float wastedValue = getUser().getWastedValue();
    String output = "Ingredient of total value " + wastedValue + " kr has been wasted.";
    getOutputHandler().printOutput(output);
  }
}
