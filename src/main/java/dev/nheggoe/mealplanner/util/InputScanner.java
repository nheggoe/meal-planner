package dev.nheggoe.mealplanner.util;

import dev.nheggoe.mealplanner.util.command.CommandRegistry;
import dev.nheggoe.mealplanner.util.input.CommandInput;
import dev.nheggoe.mealplanner.util.input.UnitInput;
import dev.nheggoe.mealplanner.util.unit.UnitRegistry;
import dev.nheggoe.mealplanner.util.unit.ValidUnit;
import java.util.Scanner;

/**
 * The InputScanner class is responsible for reading and interpreting user input from the terminal
 * window. It is designed to parse input into predefined commands, subcommands, and additional input
 * strings.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class InputScanner {
  private final OutputHandler outputHandler;
  private final Scanner scanner;

  /**
   * Default constructor for InputScanner. Initializes the internal scanner, command registry, and
   * unit registry.
   */
  public InputScanner() {
    scanner = new Scanner(System.in);
    outputHandler = new OutputHandler();
  }

  /**
   * Constructs an InputScanner instance using the provided OutputHandler. Initializes the internal
   * scanner, command registry, and unit registry.
   *
   * @param outputHandler the OutputHandler used for managing output display
   */
  public InputScanner(OutputHandler outputHandler) {
    scanner = new Scanner(System.in);
    this.outputHandler = outputHandler;
  }

  /**
   * Constructs an InputScanner instance. Initializes the internal scanner, command registry, and
   * unit registry.
   *
   * @param scannerSource the Scanner object to be used for input parsing.
   */
  public InputScanner(Scanner scannerSource) {
    scanner = scannerSource;
    outputHandler = new OutputHandler();
  }

  /**
   * Fetches and processes user input as a command. Scans the input, tokenizes it, and converts it
   * into a UserInput object.
   *
   * @return a UserInput object representing the parsed command input.
   */
  public CommandInput fetchCommand() {
    String scannedLine = nextLine();
    String[] tokenized = tokenizeInput(scannedLine);
    return createCommandInput(tokenized);
  }

  /**
   * Fetches and processes user input as a unit. The input is scanned, tokenized, and converted into
   * a UserInput object.
   *
   * @return a UserInput object representing the parsed unit input.
   */
  public UnitInput fetchUnit() {
    String scannedLine = nextLine();
    String[] tokenized = tokenizeInput(scannedLine);
    assertUnitInput(tokenized);
    return createUnitInput(tokenized);
  }

  /**
   * Continuously prompts the user for a valid unit input until a correct format is provided. If the
   * input is invalid, prompts the user with an error message.
   *
   * @return a UnitInput object that represents the valid unit input.
   */
  public UnitInput collectValidUnitInput() {
    UnitInput unitInput = null;
    while (unitInput == null) {
      try {
        unitInput = fetchUnit();
      } catch (IllegalArgumentException e) {
        outputHandler.printInputPrompt(
            "Invalid input format," + " accepted format are: {float} + {unit}");
      }
    }
    return unitInput;
  }

  /**
   * Continuously prompts the user for input until valid input is provided. The input is validated
   * using the getUserInput method, and if invalid, the user is re-prompted until a valid input is
   * entered.
   *
   * @return the valid input string provided by the user.
   */
  public String collectValidString() {
    String input = null;
    while (input == null) {
      try {
        input = nextLine();
      } catch (IllegalArgumentException e) {
        outputHandler.printInputPrompt(e.getMessage());
      }
    }
    return input;
  }

  /**
   * Continuously prompts the user for a valid floating-point input until a non-negative value is
   * provided. If the input is invalid, retries are prompted with an error message.
   *
   * @return a valid non-negative floating-point number entered by the user.
   */
  public float collectValidFloat() {
    float input = -1.0f;
    while (input < 0f) {
      try {
        input = nextFloat();
      } catch (IllegalArgumentException illegalArgumentException) {
        outputHandler.printInputPrompt(illegalArgumentException.getMessage());
      }
    }
    return input;
  }

  /**
   * Continuously prompts the user until a valid non-negative integer is provided. Invalid inputs
   * trigger an error message and re-prompt the user.
   *
   * @return a valid non-negative integer entered by the user.
   */
  public int collectValidInteger() {
    int result = -1;
    while (result < 0) {
      try {
        result = nextInteger();
      } catch (IllegalArgumentException illegalArgumentException) {
        outputHandler.printInputPrompt(illegalArgumentException.getMessage());
      }
    }
    return result;
  }

  /**
   * Reads and returns the next trimmed line from the input.
   *
   * @return the next line of input as a trimmed string.
   * @throws IllegalArgumentException if no input is found.
   */
  public String nextLine() {
    assertEmptyLine();
    String inputLine = scanner.nextLine().strip();
    assertEmptyInput(inputLine);
    assertAbort(inputLine);
    return inputLine;
  }

  /**
   * Prompts the user for a floating-point number input. If the input is blank or cannot be parsed
   * as a float, an IllegalArgumentException is thrown.
   *
   * @return the floating-point number entered by the user.
   * @throws IllegalArgumentException if the input is blank or cannot be parsed as a float.
   */
  public float nextFloat() {
    assertEmptyLine();
    return Float.parseFloat(nextLine());
  }

  /**
   * Parses the next line of input and returns it as an integer.
   *
   * @return the next input line converted to an integer.
   * @throws IllegalArgumentException if no input is found or the input cannot be parsed as an int.
   */
  public int nextInteger() {
    assertEmptyLine();
    return Integer.parseInt(nextLine());
  }

  /**
   * Validates the provided tokens array to ensure it contains at least two elements.
   *
   * @param tokens an array of strings representing the user input tokens
   * @throws IllegalArgumentException if the array has fewer than two elements
   */
  private void assertUnitInput(String[] tokens) {
    if (tokens.length < 2) {
      throw new IllegalArgumentException("Missing unit inputs.");
    }
  }

  /**
   * Checks if the provided input is equal to "abort" (case-insensitive) and throws an
   * AbortException if true.
   *
   * @param input the input string to be validated
   * @throws AbortException if the input equals "abort" (ignoring case)
   */
  private void assertAbort(String input) {
    if (input.strip().equalsIgnoreCase("abort")) {
      throw new AbortException();
    }
  }

  /**
   * Asserts that the input from the scanner is not empty.
   *
   * @throws IllegalArgumentException if no input is found.
   */
  private void assertEmptyLine() {
    if (!scanner.hasNextLine()) {
      throw new IllegalArgumentException("There are no lines to scan.");
    }
  }

  /**
   * Asserts that the input string is not empty.
   *
   * @param input the string input to be validated
   * @throws IllegalArgumentException if the input is empty or consists only of whitespace
   */
  private void assertEmptyInput(String input) {
    if (input.isBlank()) {
      throw new IllegalArgumentException("Input cannot be empty.");
    }
  }

  /**
   * Splits an input line into a maximum of three tokens based on whitespace.
   *
   * @param inputLine the input string to be tokenized.
   * @return an array containing up to three tokens extracted from the input string.
   */
  private String[] tokenizeInput(String inputLine) {
    return inputLine.split("\\s+", 3);
  }

  /**
   * Processes user input tokens and creates a UserInput object based on those tokens.
   *
   * @param tokens an array of strings representing the user's input, split by whitespace. The first
   *     token is treated as the command, the second as the subcommand, and the third as the full
   *     input string.
   * @return a UserInput object containing the parsed command, subcommand, and full input string.
   */
  private CommandInput createCommandInput(String[] tokens) {
    String command = (tokens.length > 0) ? tokens[0].toLowerCase() : null;
    String subcommand = (tokens.length > 1) ? tokens[1].toLowerCase() : null;
    String inputString = (tokens.length > 2) ? tokens[2] : null;
    return new CommandInput(CommandRegistry.findCommand(command), subcommand, inputString);
  }

  /**
   * Processes user input tokens to create a UserInput object for units.
   *
   * @param tokens an array of strings representing the user's input. The first token should be the
   *     unit amount, and the second token should be the unit type.
   * @return a UserInput object containing the unit amount and its type.
   */
  private UnitInput createUnitInput(String[] tokens) {
    float unitAmount = (tokens.length > 0) ? Float.parseFloat(tokens[0]) : -1;
    String unitString = (tokens.length > 1) ? tokens[1].toLowerCase() : null;
    ValidUnit unit = UnitRegistry.findUnit(unitString);
    return new UnitInput(unitAmount, unit);
  }
}
