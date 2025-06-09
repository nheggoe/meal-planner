package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;
import dev.nheggoe.mealplanner.user.recipe.Recipe;
import dev.nheggoe.mealplanner.user.recipe.RecipeManager;
import dev.nheggoe.mealplanner.util.Utility;
import java.util.List;

/**
 * The RemoveCommand class extends the Command class to handle "remove" operations based on
 * user-submitted subcommands. Depending on the specified subcommand, it delegates the removal
 * operation to the appropriate method.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class RemoveCommand extends Command {

  /**
   * Constructs a new RemoveCommand instance for the specified user to handle "remove" operations.
   *
   * @param user The user associated with this command.
   */
  public RemoveCommand(User user) {
    super(user);
  }

  /**
   * Handles the processing of subcommands specific to the "remove" command. Based on the provided
   * subcommand, this method delegates the removal operation to the appropriate private method.
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
   * Processes the subcommand for the "remove" command and delegates the operation to the
   * appropriate method based on the specified subcommand. If the subcommand is invalid, an
   * exception is thrown.
   */
  private void processSubcommand() {
    switch (getSubcommand()) {
      case "storage" -> removeStorage();
      case "ingredient" -> removeIngredient();
      case "expired" -> removeExpired();
      case "recipe" -> removeRecipe();
      default -> illegalCommand();
    }
  }

  /**
   * Removes a storage location based on the provided argument. If no argument is supplied, prompts
   * the user to input a storage name. Communicates the success or failure of the operation to the
   * user.
   */
  private void removeStorage() {
    if (isArgumentEmpty()) {
      setArgument("Please enter the storage name to remove:");
    }
    boolean success = getInventoryManager().removeStorage(getArgument());
    if (success) {
      getOutputHandler()
          .printOutputWithLineBreak(
              "Successfully removed " + getArgument() + " from the application.");
    } else {
      getOutputHandler()
          .printOutput(
              "Failed to remove " + getArgument() + ", please check if the name is misspelled.");
    }
  }

  /**
   * Removes all expired items from the inventory, adds their total value to the user's wasted value
   * tracker, and outputs a summary of the removed value.
   */
  private void removeExpired() {
    float sum = Utility.roundToTwoDecimal(getInventoryManager().removeAllExpired());
    getUser().addWastedValue(sum);
    getOutputHandler()
        .printOutputWithLineBreak("Value of " + sum + " kr worth of food is now been deleted.");
  }

  /**
   * Removes an ingredient from the current inventory storage. Displays a list of existing
   * ingredients, prompts the user for an ingredient name if no argument is provided, and performs
   * the removal operation. Handles errors if the ingredient is not found or input is invalid.
   */
  private void removeIngredient() {
    getOutputHandler().printOutput("List of all stock ingredients:");
    getOutputHandler().printList(getInventoryManager().getIngredientOverview(), "bullet");
    if (isArgumentEmpty()) {
      setArgument("Please enter the ingredient name:");
    }
    getInventoryManager().removeIngredientFromCurrent(getArgument());
  }

  /**
   * Removes a recipe based on user input. If no argument is provided, prompts the user to enter the
   * recipe name. Displays a numbered list of search results for selection and removes the chosen
   * recipe from the RecipeManager.
   */
  private void removeRecipe() {
    RecipeManager recipeManager = getRecipeManager();
    if (isArgumentEmpty()) {
      getOutputHandler().printList(recipeManager.getRecipeOverview(), "bullet");
      setArgument("Please enter the recipe name:");
    }
    List<Recipe> results = recipeManager.findRecipe(getArgument());
    printNameOfFiltered(results);
    int index = getIndex(results.size());
    String recipeName = results.get(index).getName();
    recipeManager.removeRecipe(results.get(index));
    getOutputHandler().printOperationStatus(true, "removed", recipeName);
  }
}
