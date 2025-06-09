package dev.nheggoe.mealplanner.util.unit;

import dev.nheggoe.mealplanner.util.Utility;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers and stores valid units for look-up and retrieval.
 *
 * @author Nick Heggø
 * @version 2024-12-04
 */
public class UnitRegistry {
  private static final Map<String, ValidUnit> UNIT_MAP = new HashMap<>();

  private UnitRegistry() {}

  static {
    initializeValidUnits();
  }

  /**
   * Finds and returns the corresponding ValidUnit for a given input string.
   *
   * @param input the string representation of a unit to be looked up.
   * @return the matching ValidUnit if found; otherwise, ValidUnit.UNKNOWN.
   */
  public static ValidUnit findUnit(String input) {
    return UNIT_MAP.getOrDefault(Utility.createKey(input), ValidUnit.UNKNOWN);
  }

  /**
   * Retrieves a list of string representations for valid units. Each unit is converted into a
   * standardized key format.
   *
   * @return a list of strings representing valid measurement unit keys.
   */
  public static List<String> getUnitList() {
    return Arrays.stream(ValidUnit.values()).map(Enum::name).map(Utility::createKey).toList();
  }

  /**
   * Initializes the UNIT_MAP by registering valid measurement units. Filters out the UNKNOWN unit
   * and registers the rest into the map using the UnitRegistry::addUnitToMap method.
   */
  private static void initializeValidUnits() {
    Arrays.stream(ValidUnit.values())
        .filter(unit -> unit != ValidUnit.UNKNOWN)
        .forEach(UnitRegistry::addUnitToMap);
  }

  /**
   * Adds a ValidUnit to the UNIT_MAP using a generated key.
   *
   * @param unit the ValidUnit to be added to the map.
   */
  private static void addUnitToMap(ValidUnit unit) {
    String key = Utility.createKey(unit.name());
    UNIT_MAP.put(key, unit);
  }
}
