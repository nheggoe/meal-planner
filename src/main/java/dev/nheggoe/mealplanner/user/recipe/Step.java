package dev.nheggoe.mealplanner.user.recipe;

import dev.nheggoe.mealplanner.user.inventory.Measurement;
import java.util.List;
import java.util.Objects;

/**
 * Represents a step in a recipe with cooking instructions and ingredients. Each step requires the
 * instruction, but the list of measurements is optional.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class Step {
  private String instruction;
  private List<Measurement> measurements;

  /**
   * Constructs a Step with the specified instruction and list of measurements.
   *
   * @param instruction the cooking instruction for this step; must not be null or blank
   * @param measurements the list of Measurement objects associated with this step
   */
  public Step(String instruction, List<Measurement> measurements) {
    setMeasurements(measurements);
    setInstruction(instruction);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getInstruction());
    if (hasMeasurements()) {
      measurements.forEach(measurement -> stringBuilder.append("\n   - ").append(measurement));
    }
    return stringBuilder.toString();
  }

  // IntelliJ Generated
  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Step step)) {
      return false;
    }

    return Objects.equals(instruction, step.instruction)
        && Objects.equals(measurements, step.measurements);
  }

  // IntelliJ Generated
  @Override
  public int hashCode() {
    int result = Objects.hashCode(instruction);
    result = 31 * result + Objects.hashCode(measurements);
    return result;
  }

  /**
   * Retrieves the map of ingredient measurements for this step.
   *
   * @return a map where the key is a string representing the ingredient name, and the value is a
   *     Measurement object.
   */
  public List<Measurement> getMeasurements() {
    return measurements;
  }

  /**
   * Sets the list of measurements for the step.
   *
   * @param measurements the list of Measurement objects to set can be null or empty.
   */
  private void setMeasurements(List<Measurement> measurements) {
    this.measurements = measurements;
  }

  /**
   * Retrieves the cooking instruction for this step.
   *
   * @return the cooking instruction.
   */
  public String getInstruction() {
    return instruction;
  }

  /**
   * Sets the cooking instruction for this step.
   *
   * @param instruction The cooking instruction to be set.
   */
  public void setInstruction(String instruction) {
    if (instruction == null) {
      throw new IllegalArgumentException("Step instruction cannot be null.");
    }
    if (instruction.isBlank()) {
      throw new IllegalArgumentException("Step instruction cannot be blank.");
    }
    this.instruction = instruction;
  }

  private boolean hasMeasurements() {
    return getMeasurements() != null && !getMeasurements().isEmpty();
  }
}
