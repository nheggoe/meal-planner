package dev.nheggoe.mealplanner.user.inventory;

import dev.nheggoe.mealplanner.user.Printable;
import dev.nheggoe.mealplanner.util.Utility;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Random;

/**
 * Represents an ingredient with attributes such as name, amount, unit, expiry date, and standard
 * unit price.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class Ingredient implements Printable {

  private final Measurement measurement;

  private LocalDate expiryDate;
  private float value;

  /**
   * Constructs an Ingredient object with the specified parameters.
   *
   * @param name the name of the ingredient
   * @param amount the amount of the ingredient
   * @param unit the unit of measurement for the ingredient
   * @param value the standard price per unit of the ingredient
   * @param daysTilExpiry the number of days until the ingredient expires
   */
  public Ingredient(String name, float amount, ValidUnit unit, float value, int daysTilExpiry) {
    measurement = new Measurement();
    setName(name);
    setAmount(amount);
    setUnit(unit);
    setValue(value);
    setExpiryDate(daysTilExpiry);
  }

  /**
   * Constructs an Ingredient with an expired status, using a password for validation.
   *
   * @param password the password to validate for creating an expired ingredient
   */
  public Ingredient(String password) {
    validatePassword(password);
    measurement = new Measurement();
    createExpiredIngredient();
  }

  /**
   * Returns a string representation of the ingredient.
   *
   * @return a string indicating the ingredient's details, or an expired message if it has expired.
   */
  @Override
  public String toString() {
    return isExpired() ? getExpiredString() : getString();
  }

  // IntelliJ Generated
  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Ingredient that)) {
      return false;
    }
    return Float.compare(value, that.value) == 0
        && Objects.equals(measurement, that.measurement)
        && Objects.equals(expiryDate, that.expiryDate);
  }

  // IntelliJ Generated
  @Override
  public int hashCode() {
    int result = Objects.hashCode(measurement);
    result = 31 * result + Objects.hashCode(expiryDate);
    result = 31 * result + Float.hashCode(value);
    return result;
  }

  /**
   * Retrieves the name of the measurement.
   *
   * @return the name as a string.
   */
  public String getName() {
    return measurement.getName();
  }

  /**
   * Sets the name of the measurement. The name must not be null or empty.
   *
   * @param name the name of the measurement
   * @throws IllegalArgumentException if the name is null or empty
   */
  public void setName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    measurement.setName(name);
  }

  /**
   * Merges the specified ingredient with the current one.
   *
   * @param ingredientToMerge the ingredient to merge into the current ingredient
   */
  public void merge(Ingredient ingredientToMerge) {
    if (isValidToMerge(ingredientToMerge)) {
      Measurement measurementToMerge = ingredientToMerge.getMeasurement();
      this.value += ingredientToMerge.getValue();
      this.measurement.merge(measurementToMerge);
    }
  }

  public float getValue() {
    return value;
  }

  /**
   * Sets the unit price of the ingredient.
   *
   * @param value the unit price of the ingredient
   * @throws IllegalArgumentException if the unit price is negative or exceeds 1000
   */
  public void setValue(float value) {
    if (value < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    } else if (value > 1000) {
      throw new IllegalArgumentException("Please enter a more reasonable unit price (max 1000");
    }

    this.value = Math.round(value * 100) / 100.0f; // round it to 2 decimal places
  }

  /**
   * Checks if the ingredient has expired.
   *
   * @return true if the current date is after the expiry date, false otherwise
   */
  private boolean isExpired() {
    return LocalDate.now().isAfter(expiryDate);
  }

  private String getString() {
    int dayTilExpiry = getDaysBetween(this.getExpiryDate());
    return "  - "
        + getName()
        + ": "
        + getAmount()
        + " "
        + getUnit()
        + " - Best before: "
        + expiryDate
        + " (in "
        + dayTilExpiry
        + " days) Value: "
        + getValue()
        + " kr";
  }

  /**
   * Generates a string detailing the ingredient's name, amount, unit, expiry date, and how many
   * days it has been expired.
   *
   * @return a formatted string describing the expired ingredient
   */
  private String getExpiredString() {
    int daysExpired = Math.abs(getDaysBetween(this.getExpiryDate()));
    return "  * "
        + getName()
        + ": "
        + getAmount()
        + " "
        + getUnit()
        + " - Best before: "
        + expiryDate
        + " (Expired "
        + daysExpired
        + " days ago) Value: "
        + getValue()
        + " kr";
  }

  /**
   * Retrieves the expiry date of the ingredient. If the expiry date is not set, returns a default
   * date 999 days in the past.
   *
   * @return the LocalDate representing the expiry date.
   */
  public LocalDate getExpiryDate() {
    return (expiryDate != null) ? expiryDate : LocalDate.now().minusDays(999);
  }

  /**
   * Sets the expiry date of the ingredient based on the number of days from the current date.
   *
   * @param daysToExpiry the number of days from today until the ingredient expires
   * @throws IllegalArgumentException if the number of days to expiry is negative
   */
  public void setExpiryDate(int daysToExpiry) {
    if (daysToExpiry < 0) {
      throw new IllegalArgumentException("Days to expiry cannot be negative");
    }
    this.expiryDate = LocalDate.now().plusDays(daysToExpiry);
  }

  public Measurement getMeasurement() {
    return this.measurement;
  }

  /**
   * Retrieves the amount of the ingredient.
   *
   * @return the ingredient amount as a float
   */
  public float getAmount() {
    return getMeasurementAmount();
  }

  /**
   * Sets the amount for this ingredient.
   *
   * @param amount the amount to set
   */
  public void setAmount(float amount) {
    measurement.setAmount(amount);
  }

  /**
   * Retrieves the measurement amount associated with the ingredient.
   *
   * @return the measurement amount as a float
   */
  private float getMeasurementAmount() {
    return measurement.getAmount();
  }

  /**
   * Retrieves the valid measurement unit of the ingredient.
   *
   * @return the valid measurement unit as an instance of ValidUnit
   */
  private ValidUnit getMeasurementUnit() {
    return measurement.getUnit();
  }

  /**
   * Retrieve the unit of the ingredient.
   *
   * @return the unit of the ingredient if it is set,
   */
  public ValidUnit getUnit() {
    return getMeasurementUnit();
  }

  /**
   * Sets the valid unit for the measurement of the ingredient.
   *
   * @param unit the valid unit to be set
   * @throws IllegalArgumentException if the unit is null or UNKNOWN
   */
  public void setUnit(ValidUnit unit) {
    measurement.setUnit(unit);
  }

  /**
   * Calculate the number of days between the current date and a specified future date.
   *
   * @param untilDate the future date until which the number of days is to be calculated
   * @return the number of days between the current date and the specified future date
   * @throws IllegalArgumentException if the untilDate is null
   */
  private int getDaysBetween(LocalDate untilDate) {
    return (int) ChronoUnit.DAYS.between(LocalDate.now(), untilDate);
  }

  /**
   * Validates the password for the backdoor constructor.
   *
   * @param password the password to validate
   * @throws RuntimeException if the provided password does not match the required value
   */
  private void validatePassword(String password) {
    final String expiredPassword = "expiredDemo";
    if (!password.equals(expiredPassword)) {
      throw new IllegalArgumentException("Wrong password for demo constructor. Action prohibited.");
    }
  }

  /**
   * Creates an expired ingredient with randomized attributes. Randomly selects the name and amount
   * for an expired ingredient. Sets the appropriate unit based on the selected name. Assigns a
   * random expiry date within a specific range.
   */
  private void createExpiredIngredient() {
    Random random = Utility.getInstanceOfRandom();
    String[] expiredNames = {"Expired Milk", "Expired Chicken", "Expired Egg", "Moldy Bread"};

    String name = expiredNames[random.nextInt(expiredNames.length)];
    float amount = random.nextInt(1, 4);
    ValidUnit validUnit = (name.equals("Expired Milk")) ? ValidUnit.L : ValidUnit.KG;

    setName(name);
    setAmount(amount);
    setUnit(validUnit);
    setValue(random.nextFloat(50f, 144f));
    // setters do not allow the expiry date to be date before today.
    expiryDate = LocalDate.now().minusDays(random.nextInt(4, 17));
  }

  /**
   * Checks whether the specified ingredient is valid to merge with the current ingredient.
   *
   * @param ingredientToMerge the ingredient to check for merging compatibility
   * @return true if the ingredient is valid to merge, false otherwise
   */
  private boolean isValidToMerge(Ingredient ingredientToMerge) {
    return isNonNull(ingredientToMerge)
        && hasSameName(ingredientToMerge)
        && hasSameExpiryDate(ingredientToMerge);
  }

  /**
   * Checks if the given ingredient and its name are not null.
   *
   * @param ingredient the ingredient to be checked
   * @return true if the ingredient and its name are not null, false otherwise
   */
  private boolean isNonNull(Ingredient ingredient) {
    return ingredient != null && ingredient.getName() != null;
  }

  /**
   * Checks if the name of the current ingredient matches the name of the specified ingredient.
   *
   * @param ingredientToMerge the ingredient to compare with the current ingredient
   * @return true if both ingredients have the same name, false otherwise
   */
  private boolean hasSameName(Ingredient ingredientToMerge) {
    return this.getName().equals(ingredientToMerge.getName());
  }

  /**
   * Checks if the expiry date of the current ingredient is the same as the expiry date of the
   * specified ingredient.
   *
   * @param ingredientToMerge the ingredient to compare expiry dates with
   * @return true if both ingredients have the same expiry date, false otherwise
   */
  private boolean hasSameExpiryDate(Ingredient ingredientToMerge) {
    return this.expiryDate.isEqual(ingredientToMerge.expiryDate);
  }
}
