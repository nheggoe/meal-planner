package edu.ntnu.idi.bidata.user.inventory;

import static org.junit.jupiter.api.Assertions.*;

import dev.nheggoe.mealplanner.user.inventory.Ingredient;
import dev.nheggoe.mealplanner.user.inventory.IngredientStorage;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Nick Heggø
 * @version 2024-12-01
 */
class IngredientStorageTest {
  static IngredientStorage ingredientStorage;
  private final PrintStream originalOut = System.out;
  private final ByteArrayOutputStream outTest = new ByteArrayOutputStream();

  @BeforeEach
  void BeforeEach() {
    System.setOut(new PrintStream(outTest));
    ingredientStorage = new IngredientStorage("Test Storage");
  }

  @AfterEach
  void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  void testEdgeCase() {
    assertThrows(IllegalArgumentException.class, () -> ingredientStorage.addIngredient(null));
    ingredientStorage.getIngredientList((Ingredient) null);
    ingredientStorage.getIngredientList((String) null);
    ingredientStorage.removeExpired();
  }

  @Test
  void testGetAllExpired() {
    ingredientStorage.addIngredient(new Ingredient("expiredDemo"));
    List<Ingredient> expiredList = ingredientStorage.getAllExpired();
    assertEquals(1, expiredList.size());
  }

  @Test
  void testAddIngredient() {
    Ingredient ingredient = new Ingredient("testIngredient", 23, ValidUnit.KG, 20, 4);
    assertFalse(ingredientStorage.isIngredientPresent("TESTINGREDIENT"));
    ingredientStorage.addIngredient(ingredient);
    assertTrue(ingredientStorage.isIngredientPresent("TESTINGREDIENT"));
  }

  @Test
  void restRemoveIngredient() {
    Ingredient ingredient = new Ingredient("testIngredient", 23, ValidUnit.KG, 20, 4);
    ingredientStorage.addIngredient(ingredient);
    assertTrue(ingredientStorage.isIngredientPresent("TESTINGREDIENT"));
    ingredientStorage.removeIngredient(ingredient);
    assertFalse(ingredientStorage.isIngredientPresent("TESTINGREDIENT"));
  }

  @Test
  void testMergeIngredient() {
    Ingredient ingredient1 = new Ingredient("testIngredient", 2, ValidUnit.KG, 20, 4);
    Ingredient ingredient2 = new Ingredient("testIngredient", 300, ValidUnit.G, 20, 4);
    ingredientStorage.addIngredient(ingredient1);
    ingredientStorage.addIngredient(ingredient2);
    String name = ingredient1.getName();
    LocalDate expiryDate = ingredient1.getExpiryDate();
    Ingredient ingredient = ingredientStorage.findIngredient(name, expiryDate);
    assertEquals(2.3f, ingredient.getAmount());
  }
}
