package dev.nheggoe.mealplanner.user.inventory;

import dev.nheggoe.mealplanner.util.InputScanner;
import dev.nheggoe.mealplanner.util.OutputHandler;
import dev.nheggoe.mealplanner.util.Utility;
import dev.nheggoe.mealplanner.util.input.UnitInput;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import java.util.*;

/**
 * Manages multiple ingredient storages and provides functionalities for ingredient and storage
 * management.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class InventoryManager {

  private final InputScanner inputScanner;
  private final OutputHandler outputHandler;

  private final Map<String, IngredientStorage> storageMap;
  private final Stack<IngredientStorage> history;
  private IngredientStorage currentStorage;

  /**
   * Constructs an InventoryManager object with the specified input and output handlers. Initializes
   * the storage map and history for tracking inventory states.
   *
   * @param inputScanner The InputScanner instance for reading user inputs.
   * @param outputHandler The OutputHandler instance for displaying outputs.
   */
  public InventoryManager(InputScanner inputScanner, OutputHandler outputHandler) {
    this.inputScanner = inputScanner;
    this.outputHandler = outputHandler;
    storageMap = new HashMap<>();
    history = new Stack<>();
  }

  /**
   * Retrieves the ingredient storage associated with the specified storage name.
   *
   * @param storageName The name of the ingredient storage to retrieve.
   * @return The corresponding IngredientStorage object if it exists in the storage map.
   */
  public IngredientStorage getStorage(String storageName) {
    return storageMap.get(Utility.createKey(storageName));
  }

  /**
   * Adds an ingredient to the current storage after validating inventory availability. Takes input
   * from the user and creates an Ingredient object.
   */
  public Ingredient createIngredient(String ingredientName) {
    outputHandler.printOutput("#### Add ingredient ####");
    UnitInput amountAndUnit = collectAmountAndUnit();
    float amount = amountAndUnit.getAmount();
    ValidUnit unit = amountAndUnit.getUnit();
    float value = collectIngredientValue();
    int daysUntilExpiry = collectDaysUntilExpiry();

    return new Ingredient(ingredientName, amount, unit, value, daysUntilExpiry);
  }

  /**
   * Searches for the specified ingredient across all storages and displays the results, including
   * the storage name and the list of matching ingredients in a bullet format.
   *
   * @param ingredientName The name of the ingredient to search for in all storages.
   */
  public void findIngredientFromAll(String ingredientName) {
    for (IngredientStorage storage : storageMap.values()) {
      List<Ingredient> ingredientList = storage.findIngredient(ingredientName);
      if (ingredientList != null && !ingredientList.isEmpty()) {
        outputHandler.printOutput(storage.getStorageName() + ":");
        outputHandler.printList(ingredientList, "bullet");
      }
    }
  }

  /**
   * Identifies and returns the names of ingredient storages where all specified measurements are
   * sufficiently available.
   *
   * @param measurements A list of Measurement objects representing the required ingredients and
   *     their quantities.
   * @return A list of strings containing the names of storages that meet the requirements.
   */
  public List<String> findSufficientStorages(List<Measurement> measurements) {
    ArrayList<IngredientStorage> listOfSufficientStorage = new ArrayList<>();
    for (IngredientStorage storage : storageMap.values()) {
      if (storage.isIngredientEnough(measurements)) {
        listOfSufficientStorage.add(storage);
      }
    }
    return listOfSufficientStorage.stream().map(IngredientStorage::getStorageName).toList();
  }

  /**
   * Adds an ingredient to the current storage.
   *
   * @param ingredientToBeAdded The ingredient to be added to the storage.
   */
  public void addIngredientToCurrentStorage(Ingredient ingredientToBeAdded) {
    assertInventoryIsAvailable();
    currentStorage.addIngredient(ingredientToBeAdded);
  }

  /**
   * Removes an ingredient from the current storage after validating its existence. If multiple
   * ingredients match, prompts the user to select one.
   *
   * @param ingredientName The name of the ingredient to remove from the current storage.
   * @throws IllegalArgumentException if the ingredient does not exist in the current storage, or if
   *     an invalid index is provided during selection.
   */
  public void removeIngredientFromCurrent(String ingredientName) {
    assertInventoryIsAvailable();
    List<Ingredient> ingredientList = findIngredientFromCurrent(ingredientName);
    if (ingredientList == null) {
      throw new IllegalArgumentException(
          "There is no " + ingredientName + " at " + currentStorage.getStorageName());
    }

    // passed all checks
    if (ingredientList.size() == 1) {
      currentStorage.removeIngredient(ingredientList.getFirst());
    } else {
      outputHandler.printOutput("Please select the ingredient to delete:");
      outputHandler.printList(ingredientList, "numbered");
      outputHandler.printInputPrompt();
      int index = inputScanner.collectValidInteger();
      if (index > ingredientList.size() || index < 1) {
        throw new IllegalArgumentException("Invalid index, please try again:");
      }
      // user input from 1, index starts from 0 (N.B. index -1)
      currentStorage.removeIngredient(ingredientList.get(index - 1));
    }
    outputHandler.printOperationStatus(true, "removed", ingredientName);
  }

  /**
   * Finds and retrieves a list of ingredients from the current storage that match the name.
   *
   * @param ingredientName The name of the ingredient to search for in the current storage.
   * @return A list of Ingredient objects matching the specified name found in the current storage.
   */
  public List<Ingredient> findIngredientFromCurrent(String ingredientName) {
    assertInventoryIsAvailable();
    return currentStorage.getIngredientList(ingredientName);
  }

  /**
   * Removes the storage with the specified name from the storage map.
   *
   * @param storageName The name of the storage to be removed.
   * @return true if the storage was successfully removed, or false if it did not exist.
   */
  public boolean removeStorage(String storageName) {
    return storageMap.remove(Utility.createKey(storageName)) != null;
  }

  /**
   * Removes all expired ingredients from the current storage and calculates their total value.
   *
   * @return The total value of the removed expired ingredients as a float.
   */
  public float removeAllExpired() {
    assertInventoryIsAvailable();
    List<Ingredient> expired = currentStorage.getAllExpired();
    currentStorage.removeExpired();
    return expired.stream().map(Ingredient::getValue).reduce(0.0f, Float::sum);
  }

  /**
   * Adds a new ingredient storage to the storage map with the provided name. The storage name is
   * converted to lowercase before being used as the key in the map.
   *
   * @param storageName The name of the storage to be added.
   */
  public void createIngredientStorage(String storageName) {
    storageMap.put(Utility.createKey(storageName), new IngredientStorage(storageName));
  }

  /**
   * Retrieves an overview of ingredients in the current storage.
   *
   * @return A list of strings representing the names of ingredients in the current storage.
   */
  public List<String> getIngredientOverview() {
    assertInventoryIsAvailable();
    return currentStorage.getIngredientOverview();
  }

  /**
   * Retrieves the names of all ingredient storages in the inventory.
   *
   * @return A list containing the names of all storages currently in the storage map.
   */
  public List<String> getStorageOverview() {
    return storageMap.values().stream().map(IngredientStorage::getStorageName).toList();
  }

  /**
   * Calculates the total value of all ingredients across all storages and returns it as a string.
   *
   * @return A string indicating the total value of the inventory rounded to two decimal places,
   *     followed by "kr."
   */
  public String getTotalValue() {
    float sum =
        storageMap.values().stream().map(IngredientStorage::getAllValue).reduce(0.0f, Float::sum);
    sum = Math.round(sum * 100) / 100.0f;
    return "Inventory has total value of: " + sum + " kr.";
  }

  /**
   * Compiles and returns a string representation of the entire inventory, including information
   * from all storages in the inventory map.
   *
   * @return A formatted string detailing all storages and their respective contents.
   */
  public String getInventoryString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("###### Inventory #######");
    storageMap.values().forEach(storage -> stringBuilder.append(storage.getStorageString()));
    return stringBuilder.toString();
  }

  /**
   * Retrieves the history of previous ingredient storages.
   *
   * @return A stack containing the history of IngredientStorage objects.
   */
  public Stack<IngredientStorage> getHistory() {
    return history;
  }

  /**
   * Retrieves a string representation of the current storage, including its contents and details.
   * Throws an exception if no storage is currently selected.
   */
  public String getStorageString() {
    if (currentStorage == null) {
      throw new IllegalArgumentException("You're currently not in a storage, use the 'go' command");
    }
    String stringBuilder = "#### Current Storage ###" + currentStorage.getStorageString();
    return stringBuilder;
  }

  /**
   * Compiles and returns a formatted string listing all ingredient storages. The string contains a
   * header and each storage name on a new line, prefixed with "# ".
   *
   * @return A string representation of all storage names, formatted with a header and prefixes.
   */
  public String getStorageNameString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("####### Storages #######");
    storageMap.values().stream()
        .map(IngredientStorage::getStorageName)
        .forEach(name -> stringBuilder.append("\n# ").append(name));
    return stringBuilder.toString();
  }

  /**
   * Retrieves the currently selected ingredient storage.
   *
   * @return The current IngredientStorage object.
   */
  public IngredientStorage getCurrentStorage() {
    return currentStorage;
  }

  /**
   * Sets the current storage to the specified IngredientStorage instance.
   *
   * @param storage The IngredientStorage object to be set as the current storage.
   */
  public void setCurrentStorage(IngredientStorage storage) {
    currentStorage = storage;
  }

  /**
   * Sets the current storage to the specified storage by name. If a storage with the given name
   * exists, it is set as the current storage.
   *
   * @param storageName The name of the storage to set as the current storage.
   */
  public void setCurrentStorage(String storageName) {
    IngredientStorage storage = getStorage(Utility.createKey(storageName));
    if (storage != null) {
      currentStorage = storage;
    }
  }

  /**
   * Retrieves a string representation of all expired ingredients in the current inventory.
   *
   * @return A string listing all expired ingredients.
   */
  public String getExpiredString() {
    assertInventoryIsAvailable();
    List<Ingredient> listOfExpired = currentStorage.getAllExpired();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("####### Expired ########");
    listOfExpired.stream()
        .map(Ingredient::toString)
        .forEach(string -> stringBuilder.append("\n").append(string));
    return stringBuilder.toString();
  }

  /**
   * Collects and returns the number of days until expiry based on user input. Ensures the input is
   * a valid integer greater than or equal to -1.
   *
   * @return The number of days until expiry as an integer.
   */
  private int collectDaysUntilExpiry() {
    int daysTilExpiry;
    do {
      outputHandler.printInputPrompt("Please enter the days until expiry:");
      daysTilExpiry = inputScanner.collectValidInteger();
    } while (daysTilExpiry < -1);
    return daysTilExpiry;
  }

  /**
   * Collects a valid ingredient value from the user. Prompts the user until a non-negative float
   * value is entered.
   *
   * @return The collected ingredient value as a float.
   */
  private float collectIngredientValue() {
    float value;
    do {
      outputHandler.printInputPrompt("Please enter the ingredient value:");
      value = inputScanner.collectValidFloat();
    } while (value < 0.0f);
    return value;
  }

  /**
   * Collects a numerical amount and its corresponding unit from user input. Repeatedly prompts the
   * user until valid input is provided.
   *
   * @return A UnitInput object containing the parsed amount and unit.
   */
  private UnitInput collectAmountAndUnit() {
    UnitInput input = null;
    boolean validInput = false;
    while (!validInput) {
      outputHandler.printInputPrompt("Please enter the amount with unit:");
      try {
        input = inputScanner.fetchUnit();
        validInput = true;
      } catch (IllegalArgumentException ignored) {
        // ignored
      }
    }

    return input;
  }

  /**
   * Ensures that the current inventory is not null.
   *
   * @throws IllegalArgumentException if no inventory is currently selected.
   */
  private void assertInventoryIsAvailable() {
    if (currentStorage == null) {
      throw new IllegalArgumentException(
          "You are currently not in an inventory," + " please use the 'go' command.");
    }
  }
}
