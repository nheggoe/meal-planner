package edu.ntnu.idi.bidata.user.inventory;

import static org.junit.jupiter.api.Assertions.*;

import dev.nheggoe.mealplanner.user.inventory.Ingredient;
import dev.nheggoe.mealplanner.util.unit.UnitConverter;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Ingredient class.
 *
 * @author Nick Heggø
 * @version 2024-12-01
 */
class IngredientTest {
  private Ingredient mergedIngredient;
  private Ingredient testIngredient;

  @BeforeEach
  void beforeEach() {
    testIngredient = new Ingredient("test", 3, ValidUnit.KG, 30.0f, 4);
    mergedIngredient = new Ingredient("test", 300, ValidUnit.G, 40.0f, 4);
  }

  @Test
  void testMerge() {
    testIngredient.merge(mergedIngredient);
    Ingredient expectedMergeResult = new Ingredient("test", 3.3f, ValidUnit.KG, 70.0f, 4);
    assertEquals(expectedMergeResult.getName(), testIngredient.getName());
    assertEquals(expectedMergeResult.getAmount(), testIngredient.getAmount());
    assertEquals(expectedMergeResult.getUnit(), testIngredient.getUnit());
    assertEquals(expectedMergeResult.getValue(), testIngredient.getValue());
    assertEquals(expectedMergeResult.getExpiryDate(), testIngredient.getExpiryDate());
  }

  @Test
  void testConvert() {
    UnitConverter.convertIngredient(testIngredient, ValidUnit.G);
    assertEquals(ValidUnit.G, testIngredient.getUnit());
    assertEquals(3000, testIngredient.getAmount());
  }

  @Test
  void testSetAmount() {
    assertThrows(IllegalArgumentException.class, () -> testIngredient.setAmount(-123));
  }

  @Test
  void testEquals() {
    Ingredient ingredient1 = new Ingredient("test", 3.3f, ValidUnit.KG, 70.0f, 4);
    Ingredient ingredient2 = new Ingredient("test", 3.3f, ValidUnit.KG, 70.0f, 4);
    assertEquals(ingredient1, ingredient2);
  }
}
