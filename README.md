# Gaussian Elimination with Scaled Partial Pivoting

A Java desktop application for solving systems of linear equations using Gaussian elimination with scaled partial pivoting.

Developed as an academic project for the Algebra and Discrete Mathematics course at UNIR.

## Features

- A graphical interface built with Java Swing.
- Gaussian elimination with scaled partial pivot selection.
- Back substitution to calculate the solution.
- Results containing the upper triangular matrix, transformed right-hand side and solution vector.
- Input validation and exceptions for invalid dimensions and singular matrices.
- File-based logging.
- Unit tests with JUnit 5.

## Technology

- Java 21
- Java Swing
- JUnit 5
- Java Platform Module System

The application follows an MVC structure, separating the interface, controller and numerical calculation logic.

## Project structure

```text
src/
├── app/          Application entry point
├── config/       Image configuration
├── controller/   Interface and calculation coordination
├── exception/    Custom exceptions
├── logic/        Solver and data models
├── test/         JUnit tests
├── view/         Swing interface
└── module-info.java

Ficheros/
└── gauss.jpg     Application image
```

Key classes:

- `app.AppGaussiana`: starts the application.
- `controller.GaussController`: coordinates user interaction and calculations.
- `logic.GaussSolver`: implements the numerical algorithm.
- `test.GaussSolverTest`: tests the solver.

## Getting started

### Requirements

- JDK 21
- An IDE with support for Java modules and JUnit 5

The repository currently has no Maven or Gradle build configuration. Dependencies must be configured in the IDE.

### Clone the repository

```bash
git clone https://github.com/rubiwan/gaussiana.git
cd gaussiana
```

### Configure IntelliJ IDEA

1. Open the repository folder as a project.
2. Set the project and module SDK to JDK 21.
3. Mark `src` as the Sources Root.
4. Open **File → Project Structure → Libraries**.
5. Add a library using **From Maven**:

   ```text
   org.junit.jupiter:junit-jupiter:5.11.4
   ```

6. Attach the library to the application module with Compile scope.
7. Rebuild the project.

JUnit is required during compilation because the test classes share the source directory and the module declares:

```java
module Gaussiana {
    requires java.desktop;
    requires org.junit.jupiter.api;
}
```

### Run the application

Run the main method in:

```text
app.AppGaussiana
```

Set the working directory to the repository root so that the application can access the `Ficheros` directory.

## Tests

Run the JUnit test class:

```text
test.GaussSolverTest
```

If IntelliJ does not display the run action, create a JUnit run configuration with this class, the application module and JDK 21.

The tests cover:

- Systems with known solutions.
- Diagonal systems.
- Negative values.
- Small pivots requiring scaled partial pivoting.
- Rejection of a nearly singular pivot.
- Singular matrices.
- Zero rows.
- Incompatible matrix and vector dimensions.

## Validation

Checked locally with JDK 21:

- Project compilation completed successfully.
- The JUnit test suite passed.
- The application entry point ran successfully.

These checks do not constitute automated testing of every interaction in the graphical interface.

## Current limitations

- Dependency setup currently requires manual IDE configuration.
- Application and test sources share the same Java module.
- Singularity checks use a fixed numerical tolerance; the solver does not estimate the condition number of a matrix.

## Author

[Anabel Díaz](https://github.com/rubiwan)