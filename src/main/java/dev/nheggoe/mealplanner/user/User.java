package dev.nheggoe.mealplanner.user;

import dev.nheggoe.mealplanner.user.inventory.InventoryManager;
import dev.nheggoe.mealplanner.user.recipe.RecipeManager;
import dev.nheggoe.mealplanner.util.InputScanner;
import dev.nheggoe.mealplanner.util.OutputHandler;
import dev.nheggoe.mealplanner.util.input.CommandInput;

/**
 * The User class encapsulates information about a user, including their name, personal inventory,
 * cookbook, current directory, and output handler. It provides methods to interact with and manage
 * these attributes.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class User {

  private final InputScanner inputScanner;
  private final OutputHandler outputHandler;

  private final InventoryManager inventoryManager;
  private final RecipeManager recipeManager;

  private String name;
  private float wastedValue;
  private CommandInput commandInput;

  /**
   * Default constructor for the User class. Initializes the input scanner, output handler,
   * inventory manager, and recipe manager. The InputScanner facilitates user interaction. The
   * OutputHandler manages user output. The InventoryManager handles inventory tasks. The
   * RecipeManager manages recipe-related tasks.
   */
  public User() {
    outputHandler = new OutputHandler();
    inputScanner = new InputScanner(outputHandler);
    inventoryManager = new InventoryManager(inputScanner, outputHandler);
    recipeManager = new RecipeManager(inputScanner, outputHandler);
    wastedValue = 0;
  }

  /**
   * Adds a specified value to the user's wasted value tracker.
   *
   * @param wastedValue the value to be added to the wasted value total.
   */
  public void addWastedValue(float wastedValue) {
    this.wastedValue += wastedValue;
  }

  /**
   * Retrieves the name of the user.
   *
   * @return the current value of the user's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the user's name.
   *
   * @param name the new name for the user.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retrieves the current CommandInput instance associated with the user.
   *
   * @return the current CommandInput.
   */
  public CommandInput getCommandInput() {
    return this.commandInput;
  }

  /**
   * Sets the CommandInput instance for the user.
   *
   * @param commandInput the CommandInput to be associated with the user.
   */
  public void setCommandInput(CommandInput commandInput) {
    this.commandInput = commandInput;
  }

  /**
   * Provides access to the user's InputScanner instance.
   *
   * @return the InputScanner associated with the user.
   */
  public InputScanner getInputScanner() {
    return inputScanner;
  }

  /**
   * Retrieves the OutputHandler instance associated with the user.
   *
   * @return the OutputHandler for managing user output.
   */
  public OutputHandler getOutputHandler() {
    return outputHandler;
  }

  /**
   * Provides access to the user's InventoryManager instance.
   *
   * @return the InventoryManager associated with the user.
   */
  public InventoryManager getInventoryManager() {
    return inventoryManager;
  }

  /**
   * Provides access to the user's RecipeManager instance.
   *
   * @return the RecipeManager associated with the user.
   */
  public RecipeManager getRecipeManager() {
    return recipeManager;
  }

  public float getWastedValue() {
    return wastedValue;
  }
}
