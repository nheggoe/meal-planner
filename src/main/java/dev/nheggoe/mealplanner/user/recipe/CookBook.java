package dev.nheggoe.mealplanner.user.recipe;

import java.util.HashSet;
import java.util.List;

/**
 * Represents a collection of recipes, allowing for adding, removing, and searching recipes.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class CookBook {

  private final HashSet<Recipe> recipes;

  /** Initializes a new CookBook object with an empty collection of recipes. */
  public CookBook() {
    recipes = new HashSet<>();
  }

  /**
   * Checks if a given recipe is present in the cookbook.
   *
   * @param recipe the recipe to be checked
   * @return true if the recipe is in the cookbook, false otherwise
   */
  public boolean isRecipePresent(Recipe recipe) {
    return recipes.contains(recipe);
  }

  /**
   * Retrieves all recipes currently stored in the cookbook.
   *
   * @return a list of Recipe objects, representing all recipes in the cookbook.
   */
  public List<Recipe> getAllRecipe() {
    return recipes.stream().toList();
  }

  /**
   * Adds a new recipe to the cookbook.
   *
   * @param recipe the recipe to add; must not be null and must not already exist in the collection
   * @throws IllegalArgumentException if the recipe is null or already exists
   */
  public void addRecipe(Recipe recipe) {
    if (recipe == null) {
      throw new IllegalArgumentException("Recipe is null.");
    }
    if (isRecipePresent(recipe)) {
      throw new IllegalArgumentException("Recipe already exist!");
    }
    recipes.add(recipe);
  }

  /**
   * Searches for recipes containing the specified name (case insensitive).
   *
   * @param name the name or partial name to search for in recipe names; must not be null or blank.
   * @return a list of Recipe objects whose names contain the specified name.
   */
  public List<Recipe> findRecipesContainingName(String name) {
    return recipes.stream()
        .filter(recipe -> recipe.getName().toLowerCase().contains(name.strip().toLowerCase()))
        .toList();
  }

  /**
   * Retrieves an overview of all recipe names in the cookbook.
   *
   * @return a list of strings representing the names of all recipes in the cookbook.
   */
  public List<String> getRecipeOverview() {
    return recipes.stream().map(Recipe::getName).toList();
  }

  /**
   * Removes the specified recipe from the collection of recipes.
   *
   * @param recipeToRemove the recipe to be removed; must not be null.
   */
  public void removeRecipe(Recipe recipeToRemove) {
    recipes.remove(recipeToRemove);
  }
}
