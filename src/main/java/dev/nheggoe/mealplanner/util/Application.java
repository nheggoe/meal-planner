package dev.nheggoe.mealplanner.util;

import dev.nheggoe.mealplanner.user.User;
import dev.nheggoe.mealplanner.user.inventory.Ingredient;
import dev.nheggoe.mealplanner.user.inventory.IngredientStorage;
import dev.nheggoe.mealplanner.user.inventory.InventoryManager;
import dev.nheggoe.mealplanner.user.inventory.Measurement;
import dev.nheggoe.mealplanner.user.recipe.RecipeBuilder;
import dev.nheggoe.mealplanner.user.recipe.Step;
import dev.nheggoe.mealplanner.util.command.Command;
import dev.nheggoe.mealplanner.util.command.IllegalCommandCombinationException;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import java.util.List;

/**
 * The Application class represents the main execution for the meal planning application. It
 * initializes user data, including storage, and manages user inputs to process commands.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class Application {
  private final User user;
  private final InputScanner inputScanner;
  private final OutputHandler outputHandler;
  private boolean running;

  /**
   * Initializes a new instance of the Application Sets up the user, input scanner, and output
   * handler.
   */
  public Application() {
    user = userSetup();
    inputScanner = user.getInputScanner();
    outputHandler = user.getOutputHandler();
    // userSetup();??
  }

  /**
   * Starts the application by displaying the help string and entering a loop to process user
   * commands. The loop continues until a command causes the application to exit. It handles any
   * exceptions by printing the error messages.
   */
  public void run() {
    outputHandler.printWelcomeMessage(user.getName());
    outputHandler.printHelpMessage();
    startUpCondition(); // If wish to start as a blank app, remove this method.
    running = true;
    engine();
  }

  /** Terminates the application by displaying a goodbye message and stopping the execution loop. */
  public void terminate() {
    outputHandler.printGoodbyeMessage();
    running = false;
  }

  /**
   * Instantiates and executes a user command. Uses the user instance and the current application
   * context to determine and perform the relevant command action.
   */
  private Command getCommand() {
    outputHandler.printCommandPrompt();
    user.setCommandInput(inputScanner.fetchCommand());
    return Command.of(user, this);
  }

  /**
   * Allows demo to be more efficient with already exising data. Add all the desired start-up
   * conditions here e.g., create a recipe, create ingredients, add storage locations.
   */
  private void startUpCondition() {
    InventoryManager inventoryManager = user.getInventoryManager();
    inventoryManager.createIngredientStorage("Fridge");
    inventoryManager.setCurrentStorage(inventoryManager.getStorage("fridge"));
    inventoryManager.addIngredientToCurrentStorage(
        new Ingredient("Chocolate", 300, ValidUnit.G, 10, 4));
    inventoryManager.addIngredientToCurrentStorage(new Ingredient("Flour", 5, ValidUnit.KG, 10, 4));
    inventoryManager.addIngredientToCurrentStorage(
        new Ingredient("Chocolate Chips", 0.9f, ValidUnit.KG, 10, 4));
    inventoryManager.addIngredientToCurrentStorage(
        new Ingredient("Butter", 0.9f, ValidUnit.KG, 10, 4));
    inventoryManager.addIngredientToCurrentStorage(
        new Ingredient("Granulated Sugar", 0.9f, ValidUnit.KG, 10, 4));
    inventoryManager.addIngredientToCurrentStorage(
        new Ingredient("Brown Sugar", 0.9f, ValidUnit.KG, 10, 4));

    inventoryManager.addIngredientToCurrentStorage(
        new Ingredient("Vanilla Extract", 0.9f, ValidUnit.DL, 10, 4));
    inventoryManager.addIngredientToCurrentStorage(
        new Ingredient("Milk", 0.4f, ValidUnit.L, 10, 4));
    int amountOfExpiredIngredientToGenerate = 3;
    for (int generatedIngredient = 0;
        generatedIngredient < amountOfExpiredIngredientToGenerate;
        generatedIngredient++) {
      inventoryManager.addIngredientToCurrentStorage(new Ingredient("expiredDemo"));
    }
    inventoryManager.createIngredientStorage("Cold Room");
    inventoryManager.setCurrentStorage("CoLd RoOm");
    inventoryManager.addIngredientToCurrentStorage(
        new Ingredient("Potato", 4, ValidUnit.KG, 40, 25));
    inventoryManager.createIngredientStorage("Office Fridge");
    inventoryManager.setCurrentStorage((IngredientStorage) null);

    // Default recipe
    RecipeBuilder builder = new RecipeBuilder();
    builder.setName("Cookie Dough");
    builder.setDescription(
        "Perfect for midnight cravings, stress-baking without the baking, "
            + "or impressing your friends with your no-bake skills. Warning: You may not want to share.");

    List<Measurement> step1 =
        List.of(
            new Measurement("Butter", 115, ValidUnit.G),
            new Measurement("Granulated Sugar", 100, ValidUnit.G),
            new Measurement("Brown Sugar", 50, ValidUnit.G));
    builder.addStep(
        new Step(
            "In a mixing bowl, beat the softened butter, granulated sugar, and brown sugar until smooth and fluffy.",
            step1));

    List<Measurement> step2 =
        List.of(
            new Measurement("Milk", 30, ValidUnit.ML),
            new Measurement("Vanilla Extract", 15, ValidUnit.ML));
    builder.addStep(new Step("Mix in the vanilla extract and milk until well combined.", step2));

    List<Measurement> step3 = List.of(new Measurement("Flour", 125, ValidUnit.G));
    builder.addStep(
        new Step("Gradually add the heat-treated flour and salt, mixing until smooth.", step3));

    List<Measurement> step4 = List.of(new Measurement("Chocolate Chips", 90, ValidUnit.G));
    builder.addStep(new Step("Stir in the chocolate chips or other desired mix-ins.", step4));

    builder.addStep(
        new Step(
            "Enjoy the cookie dough immediately, or refrigerate it in an airtight container for up to 5 days.",
            null));
    user.getRecipeManager().addRecipe(builder.getRecipe());
  }

  /**
   * Continuously processes and executes user commands while the application is running. Catches and
   * handles exceptions related to invalid commands or aborted operations. Uses the output handler
   * to display error messages and command help.
   */
  private void engine() {
    while (running) {
      try {
        Command command = getCommand();
        command.execute();
      } catch (IllegalArgumentException | AbortException e) {
        outputHandler.printOutput(e.getMessage());
      } catch (IllegalCommandCombinationException illegalCommandCombinationException) {
        outputHandler.printOutput(illegalCommandCombinationException.getMessage());
        outputHandler.printCommandHelpMessage(user.getCommandInput().getCommand());
      }
    }
  }

  /**
   * Sets up and initializes a new User instance with default configurations.
   *
   * @return a User object with the default name set to "User".
   */
  private User userSetup() {
    User createdUser = new User();
    createdUser.setName("User");
    return createdUser;
  }
}
