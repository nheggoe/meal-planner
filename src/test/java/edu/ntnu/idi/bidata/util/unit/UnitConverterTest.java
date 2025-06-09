package edu.ntnu.idi.bidata.util.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.nheggoe.mealplanner.user.inventory.Measurement;
import dev.nheggoe.mealplanner.util.unit.UnitConverter;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the UnitConverter class. This class contains various test methods to verify the
 * correct functionality of unit conversion methods within the UnitConverter class.
 *
 * @author Nick Heggø
 * @version 2024-12-05
 */
class UnitConverterTest {

  UnitConverter unitConverter;
  Measurement solidMeasurement;
  Measurement liquidMeasurement;

  @BeforeEach
  void beforeEach() {
    solidMeasurement = new Measurement("test1", 12345.6f, ValidUnit.G);
    liquidMeasurement = new Measurement("test2", 123.456f, ValidUnit.DL);
  }

  @Test
  void convertToStandard() {
    UnitConverter.convertToStandard(solidMeasurement);
    UnitConverter.convertToStandard(liquidMeasurement);
    assertEquals(12.35f, solidMeasurement.getAmount());
    assertEquals(ValidUnit.KG, solidMeasurement.getUnit());
    assertEquals(12.35f, liquidMeasurement.getAmount());
    assertEquals(ValidUnit.L, liquidMeasurement.getUnit());
  }

  @Test
  void convertToGrams() {
    UnitConverter.convertToGrams(solidMeasurement);
    assertEquals(12345.6f, solidMeasurement.getAmount());
    assertEquals(ValidUnit.G, solidMeasurement.getUnit());
  }

  @Test
  void convertToKG() {
    UnitConverter.convertToKG(solidMeasurement);
    assertEquals(12.35f, solidMeasurement.getAmount());
    assertEquals(ValidUnit.KG, solidMeasurement.getUnit());
  }

  @Test
  void convertToLiter() {
    UnitConverter.convertToLiter(liquidMeasurement);
    assertEquals(12.35f, liquidMeasurement.getAmount());
  }

  @Test
  void convertToDeciLiter() {
    UnitConverter.convertToDeciLiter(liquidMeasurement);
    assertEquals(123.46f, liquidMeasurement.getAmount());
    assertEquals(ValidUnit.DL, liquidMeasurement.getUnit());
  }

  @Test
  void convertToMilliLiter() {
    UnitConverter.convertToMilliLiter(liquidMeasurement);
    assertEquals(12345.6f, liquidMeasurement.getAmount());
    assertEquals(ValidUnit.ML, liquidMeasurement.getUnit());
  }
}
