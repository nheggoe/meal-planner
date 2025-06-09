package dev.nheggoe.mealplanner.user.inventory;

import dev.nheggoe.mealplanner.util.input.UnitInput;
import dev.nheggoe.mealplanner.util.unit.UnitConverter;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import java.util.List;
import java.util.Objects;

/**
 * Represents a measurement with a name, amount, unit, and ingredient type.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class Measurement {
  private String name; // Ingredient Name
  private float amount;
  private ValidUnit unit;
  private IngredientType ingredientType; // set automatically, based on ValidUnit.

  /**
   * Default constructor for the Measurement class. Initializes a new instance of Measurement with
   * default values.
   */
  public Measurement() {}

  /**
   * Constructs a Measurement with the specified name and input details.
   *
   * @param name the name of the measurement
   * @param input the unit input containing amount and unit details, must not be null
   */
  public Measurement(String name, UnitInput input) {
    setName(name);
    setAmount(input.getAmount());
    setUnit(input.getUnit());
  }

  /**
   * Constructs a Measurement with the specified name, amount, and unit.
   *
   * @param amount the amount of the measurement must be non-negative
   * @param unit the unit of measurement must not be null or unknown
   */
  public Measurement(String name, float amount, ValidUnit unit) {
    setName(name);
    setAmount(amount);
    setUnit(unit);
  }

  @Override
  public String toString() {
    return getAmount() + " " + getUnit().name().toLowerCase() + " " + getName();
  }

  // IntelliJ Generated
  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Measurement that)) {
      return false;
    }
    return Float.compare(amount, that.amount) == 0
        && Objects.equals(name, that.name)
        && unit == that.unit
        && ingredientType == that.ingredientType;
  }

  // IntelliJ Generated
  @Override
  public int hashCode() {
    int result = Objects.hashCode(name);
    result = 31 * result + Float.hashCode(amount);
    result = 31 * result + Objects.hashCode(unit);
    result = 31 * result + Objects.hashCode(ingredientType);
    return result;
  }

  /**
   * Merges the specified Measurement instance with the current one. The units are automatically
   * adjusted for consistency. If the merged amount reaches or exceeds 1000, it converts to a
   * standard unit.
   *
   * @param measurementToMerge the Measurement instance to merge with the current one, must not be
   *     null
   * @throws IllegalArgumentException if measurementToMerge is null
   */
  public void merge(Measurement measurementToMerge) {
    if (measurementToMerge == null) {
      throw new IllegalArgumentException("Measurement cannot be null!");
    }

    UnitConverter.autoMergeUnit(measurementToMerge, this.unit);
    float mergedAmount = this.getAmount() + measurementToMerge.getAmount();
    this.setAmount(mergedAmount);
    if (mergedAmount >= 1000) {
      UnitConverter.convertToStandard(this);
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Object> getStandardMeasurement() {
    return UnitConverter.getStandardData(this);
  }

  /**
   * Retrieves the amount of the measurement.
   *
   * @return the amount as a float.
   */
  public float getAmount() {
    return amount;
  }

  /**
   * Sets the amount for the measurement. Throws IllegalArgumentException if the amount is negative.
   *
   * @param amount the amount to set must be non-negative
   */
  public void setAmount(float amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    this.amount = amount;
  }

  /**
   * Retrieves the type of ingredient as a string.
   *
   * @return the ingredient type as a string.
   */
  public String getIngredientType() {
    return ingredientType.toString();
  }

  /**
   * Retrieves the valid unit of measurement.
   *
   * @return the valid unit as an instance of ValidUnit.
   */
  public ValidUnit getUnit() {
    return unit;
  }

  /**
   * Sets the valid unit for this measurement.
   *
   * @param unit the unit of measurement
   * @throws IllegalArgumentException if the unit is null or UNKNOWN
   */
  public void setUnit(ValidUnit unit) {
    if (unit == null) {
      throw new IllegalArgumentException("Unit cannot be null.");
    }
    if (unit == ValidUnit.UNKNOWN) {
      throw new IllegalArgumentException("Invalid unit, please try again.");
    }
    this.unit = unit;
    autoSetIngredientType();
  }

  /**
   * Automatically sets the ingredient type based on the valid unit. Throws IllegalArgumentException
   * if the valid unit is null or unknown.
   */
  private void autoSetIngredientType() {
    if (unit == null) {
      throw new IllegalArgumentException("Ingredient type cannot be null.");
    }

    switch (getUnit()) {
      case KG, G -> this.ingredientType = IngredientType.SOLID;
      case L, DL, ML -> this.ingredientType = IngredientType.LIQUID;
      default -> throw new IllegalArgumentException("Measurement Unit is unknown.");
    }
  }

  /** Represents the property of ingredient. */
  private enum IngredientType {
    SOLID,
    LIQUID
  }
}
