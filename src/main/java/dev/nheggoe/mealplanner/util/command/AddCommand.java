package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;
import dev.nheggoe.mealplanner.user.inventory.Ingredient;
import dev.nheggoe.mealplanner.user.recipe.Recipe;
import dev.nheggoe.mealplanner.user.recipe.RecipeBuilder;

/**
 * The AddCommand class extends the Command class and is responsible for handling the "add" command
 * by processing subcommands related to adding new items such as locations and storage entries for a
 * given user.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class AddCommand extends Command {

  /**
   * Constructs a new AddCommand object for the given user. This constructor initializes the
   * necessary parts and processes the command related to adding new entries.
   *
   * @param user The user for whom the add command is being created and processed.
   */
  public AddCommand(User user) {
    super(user);
  }

  /**
   * Processes subcommands related to adding new entries. Depending on the subcommand, it delegates
   * to appropriate methods. If an unrecognized subcommand is provided, illegalCommand() is called.
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
   * Processes the given subcommand by delegating to the appropriate method. Handles "storage" and
   * "inventory" with addStorage(), "ingredient" with addIngredient(), and "recipe" with
   * addRecipe(). Calls illegalCommand() for unrecognized subcommands.
   */
  private void processSubcommand() {
    switch (getSubcommand()) {
      case "storage", "inventory" -> addStorage();
      case "ingredient" -> addIngredient();
      case "recipe" -> addRecipe();
      default -> illegalCommand();
    }
  }

  /**
   * Adds a new ingredient storage by prompting for a name if necessary, creating the storage in the
   * inventory manager, and printing the operation status.
   */
  private void addStorage() {
    if (isArgumentEmpty()) {
      setArgument("Please enter a name for the new storage:");
    }
    getInventoryManager().createIngredientStorage(getArgument());
    getOutputHandler().printOperationStatus(true, "added", getArgument());
  }

  /** Creates a new Ingredient using the inventory manager and adds it to the inventory. */
  private void addIngredient() {
    if (isArgumentEmpty()) {
      setArgument("Please enter a name for the ingredient:");
    }
    Ingredient createdIngredient = getInventoryManager().createIngredient(getArgument());
    getInventoryManager().addIngredientToCurrentStorage(createdIngredient);
    getOutputHandler().printOperationStatus(true, "added", getArgument());
  }

  /** Creates and adds a new recipe using the recipe manager. */
  private void addRecipe() {
    Recipe createdRecipe = getRecipeManager().constructRecipe(new RecipeBuilder());
    getOutputHandler().printOperationStatus(true, "added", createdRecipe.getName());
    getRecipeManager().addRecipe(createdRecipe);
  }
}
