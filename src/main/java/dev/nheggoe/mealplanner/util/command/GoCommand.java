package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;
import dev.nheggoe.mealplanner.user.inventory.IngredientStorage;
import java.util.List;

/**
 * Represents a command to navigate to different locations or storages, or to go back to the
 * previous directory for a user.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class GoCommand extends Command {

  /**
   * Constructs a new GoCommand object for the specified user, enabling the user to navigate through
   * different locations, storages, or to go back to the previous directory.
   *
   * @param user The user for whom the GoCommand is being created and processed.
   */
  public GoCommand(User user) {
    super(user);
  }

  /** Processes the current subcommand and executes the corresponding method. */
  @Override
  public void execute() {
    if (hasSubcommand()) {
      processSubcommand();
    } else {
      new HelpCommand(getUser(), getCommand());
    }
  }

  /**
   * Processes the subcommand retrieved from the command input and executes the matching method. If
   * the subcommand is "to", it navigates to the specified storage using goTo(). If the subcommand
   * is "back", it navigates to the previous storage using goBack(). If the subcommand is invalid or
   * unrecognized, it invokes illegalCommand().
   */
  private void processSubcommand() {
    switch (getSubcommand()) {
      case "to" -> goTo();
      case "back" -> goBack();
      default -> illegalCommand();
    }
  }

  /**
   * Navigates to the specified ingredient storage within the inventory. Lists all available
   * storages and prompts the user to enter the storage name if not provided. Updates the current
   * storage to the specified one if it exists, and prints a confirmation or failure message.
   */
  private void goTo() {
    if (isArgumentEmpty()) {
      List<String> storageOverview = getInventoryManager().getStorageOverview();
      getOutputHandler().printOutput("List of available storages:");
      getOutputHandler().printList(storageOverview, "bullet");
      setArgument("Please enter the storage name:");
    }
    IngredientStorage ingredientStorage = getInventoryManager().getStorage(getArgument());
    boolean success = ingredientStorage != null;
    if (success) {
      addToHistory(getInventoryManager().getCurrentStorage());
      getInventoryManager().setCurrentStorage(ingredientStorage);
    }
    printOperationMessage(success);
  }

  /**
   * Navigates back to the previous storage in the history. If the history is empty, a message is
   * printed indicating no available history to navigate. Updates the current storage and prints a
   * message indicating the status of the operation.
   */
  private void goBack() {
    if (getHistory().isEmpty()) {
      getOutputHandler().printOutput("History is empty, there is no where to go");
    } else {
      getInventoryManager().setCurrentStorage(removeFromHistory());
      boolean status = getInventoryManager().getCurrentStorage() != null;
      printOperationMessage(status);
    }
  }

  /**
   * Adds the specified IngredientStorage to the navigation history.
   *
   * @param storage The IngredientStorage to be added to the history.
   */
  private void addToHistory(IngredientStorage storage) {
    getHistory().addLast(storage);
  }

  /**
   * Removes the last IngredientStorage from the navigation history.
   *
   * @return the last IngredientStorage that was removed from the history
   */
  private IngredientStorage removeFromHistory() {
    return getHistory().removeLast();
  }

  /**
   * Prints a success or failure message based on the result of a navigation operation.
   *
   * @param success indicates whether the operation was successful (true) or unsuccessful (false).
   */
  private void printOperationMessage(boolean success) {
    if (success) {
      getOutputHandler()
          .printOutput(
              "You are now at " + getInventoryManager().getCurrentStorage().getStorageName());
    } else {
      getOutputHandler().printOutput("Fail to locate destination " + getArgument());
    }
  }
}
