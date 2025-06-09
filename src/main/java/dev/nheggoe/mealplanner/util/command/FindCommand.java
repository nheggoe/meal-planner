package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.Printable;
import dev.nheggoe.mealplanner.user.User;
import dev.nheggoe.mealplanner.user.inventory.Ingredient;
import dev.nheggoe.mealplanner.user.recipe.Recipe;
import dev.nheggoe.mealplanner.util.OutputHandler;
import java.util.List;

/**
 * The FindCommand class is responsible for handling the "find" command issued by a user. This
 * command allows users to search for specific ingredients or recipes within the system. It extends
 * the abstract Command class and provides concrete implementations for processing "find"
 * subcommands.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class FindCommand extends Command {

  /**
   * Constructs a new FindCommand object for the specified user. The FindCommand class is used to
   * handle the "find" command issued by a user. This command allows users to search for specific
   * ingredients or recipes within the system.
   *
   * @param user The user for whom the "find" command is being created and processed.
   */
  public FindCommand(User user) {
    super(user);
  }

  /**
   * Processes the subcommand of the find command by determining the action to take based on the
   * subcommand value.
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
   * Determines and processes the action to take based on the subcommand value. Delegates to the
   * appropriate method for "ingredient" and "recipe" subcommands. Calls illegalCommand() for
   * unsupported subcommands.
   */
  private void processSubcommand() {
    switch (getSubcommand()) {
      case "ingredient" -> findIngredient();
      case "recipe" -> findRecipe();
      default -> illegalCommand();
    }
  }

  private void findIngredient() {
    if (isArgumentEmpty()) {
      List<String> overview = getInventoryManager().getIngredientOverview();
      getOutputHandler().printList(overview, "bullet");
      setArgument("Please enter the ingredient name to find:");
    }
    List<Ingredient> matchingIngredients =
        getInventoryManager().findIngredientFromCurrent(getArgument());
    // first case, none matching.
    if (matchingIngredients.isEmpty()) {
      getOutputHandler()
          .printOutputWithLineBreak(getArgument() + " isn't present at any of the storages.");
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getArgument()).append(" is present at:");
      matchingIngredients.forEach(
          storageName -> stringBuilder.append("\n  * ").append(storageName));
      getOutputHandler().printOutputWithLineBreak(stringBuilder.toString());
    }
  }

  private void findRecipe() {
    if (isArgumentEmpty()) {
      List<String> overview = getRecipeManager().getRecipeOverview();
      getOutputHandler().printList(overview, "bullet");
      setArgument("Please enter the recipe name:");
    }
    List<Recipe> matchingRecipes = getRecipeManager().findRecipe(getArgument());
    printDetails(matchingRecipes);
  }

  private void printDetails(List<?> matchingObjects) {
    OutputHandler outputHandler = getOutputHandler();
    if (matchingObjects.stream().allMatch(Printable.class::isInstance)) {
      List<Printable> printable = matchingObjects.stream().map(o -> (Printable) o).toList();
      switch (printable.size()) {
        case 0 -> outputHandler.printOutput("Cannot find any matching.");
        case 1 ->
            outputHandler.printOutputWithLineBreak(matchingObjects.getFirst().toString() + "\n");
        default -> printAll(matchingObjects, outputHandler);
      }
    }
  }

  /**
   * Prints all matching objects as a numbered list and prompts the user to choose one for output.
   *
   * @param matchingObjects the list of objects to be displayed and selected from
   * @param outputHandler the handler responsible for printing output and receiving input
   */
  private void printAll(List<?> matchingObjects, OutputHandler outputHandler) {
    List<String> names = matchingObjects.stream().map(o -> ((Printable) o).getName()).toList();
    outputHandler.printList(names, "numbered");
    int index = -1;
    while (index >= matchingObjects.size() || index < 1) {
      outputHandler.printInputPrompt("Please choose the recipe to show details:");
      index = getInputScanner().collectValidInteger();
    }
    outputHandler.printOutputWithLineBreak(
        matchingObjects.get(index - 1).toString() + "\n"); // Index start at 0, printed start at 1
  }
}
