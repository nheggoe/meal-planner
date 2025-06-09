package dev.nheggoe.mealplanner.user.recipe;

/**
 * RecipeBuilder is a helper class for incrementally constructing Recipe objects. It allows setting
 * the recipe's name, description, and step-by-step instructions.
 *
 * @author Nick Heggø
 * @version 2024-12-05
 */
public class RecipeBuilder {

  Recipe recipe;

  /** Constructs a new RecipeBuilder instance and initializes the internal Recipe object. */
  public RecipeBuilder() {
    reset();
  }

  /**
   * Resets the current Recipe object to a new instance, clearing all existing data such as name,
   * description, and steps.
   */
  public void reset() {
    recipe = new Recipe();
  }

  /**
   * Sets the name of the recipe being constructed.
   *
   * @param name the desired name for the recipe; cannot be null
   * @throws IllegalArgumentException if the name is null
   */
  public void setName(String name) {
    recipe.setName(name);
  }

  /**
   * Sets the description of the recipe being constructed.
   *
   * @param description the desired description for the recipe; cannot be null or empty
   * @throws IllegalArgumentException if the description is null or empty
   */
  public void setDescription(String description) {
    recipe.setDescription(description);
  }

  /**
   * Adds a cooking step to the current recipe.
   *
   * @param step the step to add, containing instructions and optional measurements
   */
  public void addStep(Step step) {
    recipe.addStep(step);
  }

  /**
   * Validates the essential information of a Recipe object, ensuring that the name, description,
   * and steps are not null or empty. Throws an IllegalArgumentException if any of these conditions
   * are violated.
   *
   * @throws IllegalArgumentException if the recipe name is null or blank, if the recipe description
   *     is null or blank, or if the recipe steps list is null or empty.
   */
  private void assertRecipeInfo() {
    if (recipe.getName() == null || recipe.getName().isBlank()) {
      throw new IllegalArgumentException("Name of recipe cannot be empty.");
    }
    if (recipe.getDescription() == null || recipe.getDescription().isBlank()) {
      throw new IllegalArgumentException("Description of the recipe cannot be empty.");
    }
    if (recipe.getSteps() == null || recipe.getSteps().isEmpty()) {
      throw new IllegalArgumentException("Recipe steps cannot be empty.");
    }
  }

  /**
   * Returns the constructed Recipe object after validating its information. Resets the internal
   * state for new recipe construction.
   *
   * @return the fully constructed Recipe object with validated information.
   * @throws IllegalArgumentException if the recipe name, description, or steps are invalid.
   */
  public Recipe getRecipe() {
    assertRecipeInfo();
    Recipe createdRecipe = recipe;
    reset();
    return createdRecipe;
  }
}
