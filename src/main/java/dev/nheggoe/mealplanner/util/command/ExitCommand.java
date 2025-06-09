package dev.nheggoe.mealplanner.util.command;

import dev.nheggoe.mealplanner.user.User;
import dev.nheggoe.mealplanner.util.Application;

/**
 * The ExitCommand class is responsible for terminating the application upon execution. It extends
 * the Command class to provide a concrete implementation of the execute method.
 *
 * @author Nick Heggø
 * @version 2024-12-12
 */
public class ExitCommand extends Command {

  private final Application app;

  /**
   * Constructs a new ExitCommand object, which terminates the application when executed.
   *
   * @param user The user associated with the command.
   * @param app The application instance that will be terminated upon execution.
   */
  public ExitCommand(User user, Application app) {
    super(user);
    this.app = app;
  }

  /**
   * Executes the exit command to terminate the application. This method calls the terminate method
   * on the application instance, effectively stopping the application.
   */
  @Override
  public void execute() {
    app.terminate();
  }
}
