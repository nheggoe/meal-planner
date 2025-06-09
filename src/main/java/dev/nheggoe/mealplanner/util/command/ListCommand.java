package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;
import dev.nheggoe.mealplanner.user.inventory.Measurement;
import dev.nheggoe.mealplanner.user.recipe.Recipe;
import dev.nheggoe.mealplanner.util.OutputHandler;
import java.util.List;

/**
 * ListCommand is a concrete implementation of the Command class that handles various subcommands
 * related to listing information, such as inventory, location, recipes, ingredients, expired items,
 * and available recipes.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class ListCommand extends Command {

  /**
   * Constructs a ListCommand for the specified user, enabling execution of commands related to
   * listing resources.
   *
   * @param user The user associated with this command.
   */
  public ListCommand(User user) {
    super(user);
  }

  /**
   * Processes various subcommands related to listing information, such as inventory, storage,
   * recipes, ingredients, expired items, and available recipes. This method interprets the
   * user-provided subcommand and delegates the task to the corresponding method.
   *
   * @throws IllegalArgumentException if the provided subcommand is unexpected or not recognized.
   */
  @Override
  public void execute() {
    if (hasSubcommand()) {
      processSubcommand();
    } else {
      new HelpCommand(getUser(), getCommand());
    }
  }

  /**
   * Processes the subcommand provided by the user and delegates execution to corresponding methods
   * based on the subcommand type. Handles various subcommands related to listing features like
   * inventory, storage, recipes, ingredients, expired items, available recipes, and other values.
   * Calls an error handler for invalid or unrecognized subcommands.
   */
  private void processSubcommand() {
    switch (getSubcommand()) {
      case "all" -> listAll();
      case "storage" -> listStorage();
      case "recipe" -> listRecipe();
      case "ingredient" -> listIngredient();
      case "expired" -> listExpired();
      case "available" -> listAvailableRecipe();
      case "command", "commands" -> getOutputHandler().printHelpMessage();
      case "name" -> listName();
      case "value", "values" -> listValue();
      default -> illegalCommand();
    }
  }

  /**
   * Lists the total value of the inventory. Retrieves the inventory's total value from the
   * InventoryManager and outputs it using the OutputHandler.
   */
  private void listValue() {
    String message = getInventoryManager().getTotalValue();
    getOutputHandler().printOutput(message);
  }

  /**
   * Lists the names of the ingredient storages. Retrieves the names from the InventoryManager as a
   * formatted string and prints it using the OutputHandler.
   */
  private void listName() {
    getOutputHandler().printOutputWithLineBreak(getInventoryManager().getStorageNameString());
  }

  /**
   * Lists the inventory of the user. This method retrieves the user's storage data as a formatted
   * string and then prints it to the console with a line of separator characters.
   */
  private void listAll() {
    getOutputHandler().printOutputWithLineBreak(getInventoryManager().getInventoryString());
  }

  /**
   * Lists the current storage details. Retrieves a formatted string of storage information from
   * InventoryManager and outputs it using the OutputHandler with a line break.
   */
  private void listStorage() {
    getOutputHandler().printOutputWithLineBreak(getInventoryManager().getStorageString());
  }

  /**
   * Lists the total number of recipes and their names. Retrieves an overview of all existing
   * recipes from the RecipeManager. Outputs the count of recipes and their formatted names using
   * the OutputHandler. If no recipes exist, notifies the user with a relevant message.
   */
  private void listRecipe() {
    List<String> recipeList = getRecipeManager().getRecipeOverview();
    OutputHandler outputHandler = getOutputHandler();
    if (recipeList == null || recipeList.isEmpty()) {
      outputHandler.printOutput("There are currently 0 recipes in the system.");
    } else {
      outputHandler.printOutput(
          "There are currently " + recipeList.size() + " recipes in the system.");
      outputHandler.printList(recipeList, "bullet");
    }
  }

  /**
   * Lists all available ingredients in the user's inventory. Retrieves ingredient data from
   * InventoryManager and displays the formatted list using the OutputHandler.
   */
  private void listIngredient() {
    if (isArgumentEmpty()) {
      setArgument("Please enter a ingredient name:");
    }
    getInventoryManager().findIngredientFromAll(getArgument());
  }

  private void listExpired() {
    getOutputHandler().printOutputWithLineBreak(getInventoryManager().getExpiredString());
  }

  private void listAvailableRecipe() {
    OutputHandler outputHandler = getOutputHandler();
    List<Recipe> recipeList = getRecipeManager().getAllRecipe();
    if (recipeList.isEmpty()) {
      outputHandler.printOutput("There are no recipes at the moment.");
    } else {
      boolean anyAvailable = false;
      for (Recipe recipe : recipeList) {
        List<Measurement> measurements = recipe.getAllMeasurement();
        List<String> listOfSufficientStorages =
            getInventoryManager().findSufficientStorages(measurements);
        if (!listOfSufficientStorages.isEmpty()) {
          outputHandler.printOutput("There is enough ingredient for " + recipe.getName() + " at:");
          outputHandler.printList(listOfSufficientStorages, "bullet");
          anyAvailable = true;
        }
      }
      if (!anyAvailable) {
        getOutputHandler().printOutput("You don't have enough ingredient at the moment.");
      }
    }
  }
}
