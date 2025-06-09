package edu.ntnu.idi.bidata.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.nheggoe.mealplanner.util.AbortException;
import dev.nheggoe.mealplanner.util.InputScanner;
import dev.nheggoe.mealplanner.util.command.ValidCommand;
import dev.nheggoe.mealplanner.util.input.CommandInput;
import dev.nheggoe.mealplanner.util.input.UnitInput;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * The InputScannerTest class contains unit tests for the InputScanner class. These tests validate
 * the methods for reading and interpreting user input from various input streams, ensuring correct
 * functionality and handling of edge cases.
 *
 * @author Nick Heggø
 * @version 2024-11-30
 */
class InputScannerTest {

  @AfterEach
  void restoreSystemIn() {
    System.setIn(System.in); // Restore to the original System.in after each test
  }

  @Test
  void testAbortException() {
    System.setIn(new ByteArrayInputStream("AbOrt ".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertThrows(AbortException.class, inputScanner::nextLine);
  }

  @Test
  void testNextLine() {
    System.setIn(
        new ByteArrayInputStream(
            "lISt   testSuBcOmmand    test   uSEr iNput stRing   ".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertEquals("lISt   testSuBcOmmand    test   uSEr iNput stRing", inputScanner.nextLine());
  }

  @Test
  void testNextLineNegative() {
    System.setIn(new ByteArrayInputStream("".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertThrows(IllegalArgumentException.class, inputScanner::nextLine);
  }

  @Test
  void testNextInteger() {
    System.setIn(new ByteArrayInputStream("123".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertEquals(123, inputScanner.nextInteger());
  }

  @Test
  void testNextIntegerNegative() {
    System.setIn(new ByteArrayInputStream("123.45".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertThrows(NumberFormatException.class, inputScanner::nextInteger);
  }

  @Test
  void testNextFloat() {
    System.setIn(new ByteArrayInputStream(".2349".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertEquals(0.2349f, inputScanner.nextFloat());
  }

  @Test
  void testNextFloatNegative() {
    System.setIn(new ByteArrayInputStream("string".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertThrows(NumberFormatException.class, inputScanner::nextFloat);
  }

  @Test
  void testFetchCommand() {
    System.setIn(
        new ByteArrayInputStream(
            "lISt   testSuBcOmmand    test   uSEr iNput stRing   ".getBytes()));
    InputScanner inputScanner = new InputScanner();
    CommandInput commandInput = inputScanner.fetchCommand();
    assertEquals(ValidCommand.LIST, commandInput.getCommand());
    assertEquals("list", commandInput.getCommand().name().toLowerCase());
    assertEquals("testsubcommand", commandInput.getSubcommand());
    assertEquals("test   uSEr iNput stRing", commandInput.getArgument());
  }

  @Test
  void testFetchCommandNegative() {
    System.setIn(new ByteArrayInputStream("lst something".getBytes()));
    InputScanner inputScanner = new InputScanner();
    CommandInput commandInput = inputScanner.fetchCommand();
    assertEquals(ValidCommand.UNKNOWN, commandInput.getCommand());
    assertEquals("something", commandInput.getSubcommand());
  }

  @Test
  void testFetchUnit() {
    System.setIn(new ByteArrayInputStream("123.45 kg".getBytes()));
    InputScanner inputScanner = new InputScanner();
    UnitInput input = inputScanner.fetchUnit();
    assertEquals(123.45f, input.getAmount());
    assertEquals(ValidUnit.KG, input.getUnit());
  }

  @Test
  void testFetchUnitNegativeOne() {
    System.setIn(new ByteArrayInputStream("k".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertThrows(IllegalArgumentException.class, inputScanner::fetchUnit);
  }

  @Test
  void testFetchUnitNegativeTwo() {
    System.setIn(new ByteArrayInputStream("123".getBytes()));
    InputScanner inputScanner = new InputScanner();
    assertThrows(IllegalArgumentException.class, inputScanner::fetchUnit);
  }
}
