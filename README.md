# Student Q&A

A role-based question and answer discussion platform for students, built as a four-person team project for CSE 360 at Arizona State University. It is a desktop app written in Java with a JavaFX interface and an embedded H2 database.

## Features

- **Five user roles:** Admin, Instructor, Staff, Reviewer, and Student, each with its own home page and permissions
- **Questions and answers:** students post questions, answer them, and sort both lists
- **Reviews and feedback:** reviewers can review answers, and users can see review updates and feedback
- **Trusted reviewers:** students can request reviewers and manage a trusted reviewer list
- **Private messages** between users
- **Account management:** invitation codes, one-time passwords, role assignment by admins, and password changes
- **Input validation:** a password strength evaluator and a username recognizer

## Tech stack

- Java and JavaFX for the GUI
- H2 embedded database through JDBC
- JUnit 5 for tests
- MVC-style structure with code reviews across the team

## Project layout

```
src/application      JavaFX pages, models, and JUnit test classes
src/databasePart1    DatabaseHelper, the JDBC layer for the H2 database
docs/                Generated javadoc
State Diagram.asta   State diagram for the app
```

## Running it

1. Install a JDK with JavaFX available, plus the H2 and JUnit 5 jars.
2. Open the project in Eclipse or IntelliJ and add those jars to the module path.
3. Run `application.StartCSE360`.

On first launch the database is empty, so the app opens the admin setup page. The database file is created at `~/FoundationDatabase`.

## Tests

JUnit test classes live next to the code in `src/application`. They cover login and authentication flows, role-based access, and Q&A workflows.

## Team

Built by a four-person ASU student team.
