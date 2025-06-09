package dev.nheggoe.mealplanner.user.recipe;

import dev.nheggoe.mealplanner.user.Printable;
import dev.nheggoe.mealplanner.user.inventory.Measurement;
import dev.nheggoe.mealplanner.util.Utility;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cooking recipe composed of multiple steps, each with specific instructions and
 * required ingredients.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class Recipe implements Printable {
  private final List<Step> steps;
  private String name;
  private String description;

  /**
   * Constructs a new Recipe with no name, description, or steps. Initializes an empty list for
   * storing steps.
   */
  public Recipe() {
    steps = new ArrayList<>();
  }

  /**
   * Constructs a Recipe with the specified name.
   *
   * @param name the name to be assigned to the recipe
   */
  public Recipe(String name) {
    steps = new ArrayList<>();
    setName(name);
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Recipe recipe)) {
      return false;
    }
    return steps.equals(recipe.steps)
        && name.equals(recipe.name)
        && description.equals(recipe.description);
  }

  // IntelliJ Generated
  @Override
  public int hashCode() {
    int result = steps.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + description.hashCode();
    return result;
  }

  // IntelliJ Generated
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
        .append(getName())
        .append(":")
        .append("\n")
        .append("\n")
        .append(getDescription())
        .append("\n");
    List<Step> stepList = getSteps();
    for (int i = 0; i < stepList.size(); i++) {
      int index = i + 1;
      stringBuilder
          .append("\n")
          .append(index)
          .append(Utility.getOrdinalSuffix(index))
          .append(" step: ")
          .append(stepList.get(i));
    }
    // the output will look like -> "1st step: " + step.toString()
    return stringBuilder.toString();
  }

  /**
   * Retrieves the name of the recipe.
   *
   * @return the name of the recipe
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the recipe.
   *
   * @param name the new name to assign; must not be null
   * @throws IllegalArgumentException if the provided name is null
   */
  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.name = name;
  }

  /**
   * Adds a cooking step to the recipe.
   *
   * @param step the step to be added, which includes instructions and ingredients
   */
  public void addStep(Step step) {
    steps.add(step);
  }

  /**
   * Removes a specific step from the recipe based on its index.
   *
   * @param stepNumber the index of the step to remove; must be within the bounds of the steps list
   * @throws IllegalArgumentException if the stepNumber is out of valid bounds
   */
  public void removeStep(int stepNumber) {
    assertIndexWithInBounds(stepNumber);
    steps.remove(stepNumber);
  }

  /**
   * Retrieves all measurements from the list of steps in the recipe. Combines and returns all
   * measurements found in the steps, if any.
   *
   * @return a list of Measurement objects collected from all steps in the recipe.
   */
  public List<Measurement> getAllMeasurement() {
    ArrayList<Measurement> measurements = new ArrayList<>();
    for (Step step : steps) {
      if (step.getMeasurements() != null) {
        measurements.addAll(step.getMeasurements());
      }
    }
    return measurements.stream().toList();
  }

  /**
   * Retrieves the description of the recipe.
   *
   * @return the description of the recipe, or null if not set
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the recipe.
   *
   * @param description the description to be set; must not be null or empty
   * @throws IllegalArgumentException if the description is null or empty
   */
  public void setDescription(String description) {
    if (description == null) {
      throw new IllegalArgumentException("Description cannot be null.");
    }
    if (description.isBlank()) {
      throw new IllegalArgumentException("Description cannot be empty.");
    }
    this.description = description;
  }

  /**
   * Retrieves the list of steps in the recipe.
   *
   * @return a list of Step objects representing the steps in the recipe.
   */
  public List<Step> getSteps() {
    return steps;
  }

  private void assertIndexWithInBounds(int index) {
    if (index >= steps.size()) {
      throw new IllegalArgumentException(
          "Step number out of bound, please provide the valid step number.");
    }
  }
}
