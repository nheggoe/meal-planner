package dev.nheggoe.mealplanner.user.recipe;

import dev.nheggoe.mealplanner.user.inventory.Measurement;
import dev.nheggoe.mealplanner.util.InputScanner;
import dev.nheggoe.mealplanner.util.OutputHandler;
import dev.nheggoe.mealplanner.util.Utility;
import dev.nheggoe.mealplanner.util.input.UnitInput;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the creation, storage, and retrieval of recipes. Provides functionality for constructing
 * recipes interactively, managing a recipe collection, and searching or modifying recipes in the
 * cookbook.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class RecipeManager {

  private final InputScanner inputScanner;
  private final OutputHandler outputHandler;

  private final CookBook cookBook;

  /**
   * Constructs a RecipeManager with the provided inputScanner and outputHandler. Initializes a new
   * CookBook instance.
   *
   * @param inputScanner an InputScanner instance for reading user inputs
   * @param outputHandler an OutputHandler instance for displaying output to users
   */
  public RecipeManager(InputScanner inputScanner, OutputHandler outputHandler) {
    this.inputScanner = inputScanner;
    this.outputHandler = outputHandler;
    cookBook = new CookBook();
  }

  /**
   * Creates a new recipe by prompting the user for the recipe details.
   *
   * @return the newly created Recipe object
   */
  public Recipe constructRecipe(RecipeBuilder recipeBuilder) {
    initialRecipeName(recipeBuilder);
    initialRecipeDescription(recipeBuilder);
    initialSteps(recipeBuilder);
    return recipeBuilder.getRecipe();
  }

  public List<Recipe> getAllRecipe() {
    return cookBook.getAllRecipe();
  }

  /**
   * Adds a recipe to the cookbook.
   *
   * @param recipeToBeAdded the recipe to be added to the cookbook
   */
  public void addRecipe(Recipe recipeToBeAdded) {
    cookBook.addRecipe(recipeToBeAdded);
  }

  /**
   * Finds and returns a list of recipes whose names contain the specified substring.
   *
   * @param name the name or part of a name to search for; must not be null or blank.
   * @return a list of Recipe objects with names matching the specified substring.
   */
  public List<Recipe> findRecipe(String name) {
    return cookBook.findRecipesContainingName(name);
  }

  /**
   * Retrieves an overview of all recipe names in the cookbook.
   *
   * @return a list of strings containing the names of all recipes in the cookbook.
   */
  public List<String> getRecipeOverview() {
    return cookBook.getRecipeOverview();
  }

  /**
   * Removes a specified recipe from the cookbook.
   *
   * @param recipeToRemove the recipe to be removed; must not be null
   */
  public void removeRecipe(Recipe recipeToRemove) {
    cookBook.removeRecipe(recipeToRemove);
  }

  /**
   * Prompts the user to input a name for the recipe and sets it in the RecipeBuilder.
   *
   * @param recipeBuilder the RecipeBuilder instance used to set the recipe's name
   */
  private void initialRecipeName(RecipeBuilder recipeBuilder) {
    outputHandler.printInputPrompt("Please enter a name for the recipe:");
    recipeBuilder.setName(inputScanner.collectValidString());
  }

  /**
   * Prompts the user to enter a description for the recipe and sets it in the RecipeBuilder.
   *
   * @param recipeBuilder the RecipeBuilder instance used to set the recipe's description
   */
  private void initialRecipeDescription(RecipeBuilder recipeBuilder) {
    outputHandler.printInputPrompt("Please enter the recipe description:");
    recipeBuilder.setDescription(inputScanner.collectValidString());
  }

  /**
   * Iteratively prompts the user to add steps to a recipe until the user opts to stop.
   *
   * @param recipeBuilder the RecipeBuilder instance used to construct the recipe by adding steps
   */
  private void initialSteps(RecipeBuilder recipeBuilder) {
    boolean keepAdding = true;
    int stepCount = 1;
    while (keepAdding) {
      initialIndividuateStep(recipeBuilder, stepCount);
      outputHandler.printInputPrompt("Do you want to add more steps? (Y/n)");
      keepAdding = Utility.isContinuationConfirmed(inputScanner.collectValidString());
      stepCount++;
    }
  }

  /**
   * Adds a step to the recipe being constructed using the given RecipeBuilder. Prompts the user for
   * step instructions and associated measurements.
   *
   * @param recipeBuilder the RecipeBuilder instance for constructing the recipe
   * @param stepCount the current step number for which instructions are being added
   */
  private void initialIndividuateStep(RecipeBuilder recipeBuilder, int stepCount) {
    outputHandler.printInputPrompt("Please enter the instruction for step " + stepCount + ":");
    String instruction = inputScanner.collectValidString();
    outputHandler.printInputPrompt("Does step nr " + stepCount + " have any ingredients? (Y/n)");
    ArrayList<Measurement> measurements = null;
    if (Utility.isContinuationConfirmed(inputScanner.collectValidString())) {
      measurements = getMeasurements(stepCount);
    }
    recipeBuilder.addStep(new Step(instruction, measurements));
  }

  /**
   * Iteratively collects measurements from the user by prompting for ingredient details, until the
   * user decides to stop.
   *
   * @param stepCount the current recipe step number for which measurements are being collected
   * @return an ArrayList of Measurement objects representing the collected ingredient measurements
   */
  private ArrayList<Measurement> getMeasurements(int stepCount) {
    boolean keepAdding = true;
    ArrayList<Measurement> measurements = new ArrayList<>();
    while (keepAdding) {
      initialIndividuateMeasurement(measurements);
      outputHandler.printInputPrompt("Add more ingredients to step " + stepCount + " ? (Y/n)");
      keepAdding = Utility.isContinuationConfirmed(inputScanner.collectValidString());
    }
    return measurements;
  }

  /**
   * Prompts the user to input an ingredient name and a unit measurement, then adds a new
   * Measurement to the provided list.
   *
   * @param measurements the list of Measurement objects to which a new measurement will be added
   */
  private void initialIndividuateMeasurement(ArrayList<Measurement> measurements) {
    outputHandler.printInputPrompt("Please enter the ingredient name:");
    String ingredientName = inputScanner.collectValidString();
    outputHandler.printInputPrompt("Please enter the amount + unit:");
    UnitInput unitInput = inputScanner.collectValidUnitInput();
    measurements.add(new Measurement(ingredientName, unitInput));
  }
}
