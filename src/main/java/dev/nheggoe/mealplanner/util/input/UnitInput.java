package dev.nheggoe.mealplanner.util.input;

import dev.nheggoe.mealplanner.util.unit.ValidUnit;

/** Represents an input consisting of a numerical amount and its corresponding measurement unit. */
public class UnitInput {
  private ValidUnit unit;
  private float amount;

  /**
   * Constructs a UnitInput object with the specified amount and unit.
   *
   * @param amount The numerical value representing the quantity.
   * @param unit The measurement unit represented by the ValidUnit enum.
   */
  public UnitInput(float amount, ValidUnit unit) {
    setAmount(amount);
    setUnit(unit);
  }

  /**
   * Retrieves the currently assigned measurement unit.
   *
   * @return The measurement unit represented by the ValidUnit enum.
   */
  public ValidUnit getUnit() {
    return unit;
  }

  /**
   * Sets the valid measurement unit associated with this UserInput instance.
   *
   * @param validUnit The measurement unit represented by the ValidUnit enum.
   */
  private void setUnit(ValidUnit validUnit) {
    this.unit = validUnit;
  }

  /**
   * Retrieves the numerical amount associated with this UnitInput instance.
   *
   * @return The amount as a float.
   */
  public float getAmount() {
    return amount;
  }

  /**
   * Sets the numerical amount for this UnitInput instance.
   *
   * @param unitAmount The numerical value representing the quantity.
   */
  private void setAmount(float unitAmount) {
    this.amount = unitAmount;
  }
}
