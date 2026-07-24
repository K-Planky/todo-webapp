<h1 align="center">todo-webapp</h1>

<p align="center">
  <strong>A multi-user to-do list web app built on plain Java Servlets and embedded Tomcat, with no framework doing the heavy lifting.</strong>
</p>

<p align="center">
  <img alt="Java 21" src="https://img.shields.io/badge/java-21-orange.svg">
  <img alt="Jakarta Servlets" src="https://img.shields.io/badge/jakarta-servlet%206-blue.svg">
  <img alt="PostgreSQL" src="https://img.shields.io/badge/postgresql-16%2B-336791.svg">
  <img alt="License: MIT" src="https://img.shields.io/badge/license-MIT-blue.svg">
</p>

## What it does

Register an account, log in, and manage your own private to-do list: create, edit, complete, and delete tasks. Every user sees only their own todos. Passwords are hashed with BCrypt, and authentication is enforced by a servlet filter in front of every protected route.

The point of the project was to build a real database-backed web app on the bare Servlet API, so every layer is visible and hand-wired rather than hidden behind Spring.

## Stack

- **Java 21**, **Jakarta Servlets 6** on **embedded Tomcat** (the app is a runnable fat JAR, not a WAR).
- **PostgreSQL** with a **HikariCP** connection pool.
- **BCrypt** for password hashing.
- **JSP + JSTL** views with Bootstrap for layout.

## Architecture

Dependencies point one direction only:

```
Servlet / Filter  ->  Service  ->  Repository (DAO)  ->  JDBC + HikariCP  ->  PostgreSQL
```

- A repository never touches `HttpServletRequest`; a service never writes to the response.
- `Webapp.main()` is the composition root: it builds the one pooled `DataSource`, then the repositories, then the services, and injects them.
- Servlets receive only the services they actually use, through small capability interfaces (`SecurityServiceAware`, `UserServiceAware`, `TodoServiceAware`) resolved by `ServletRouter`. No servlet is handed a collaborator it does not need.
- All SQL uses `PreparedStatement` inside try-with-resources, scoped to the current user.

## Running it

Requires Java 21+, Maven, and a PostgreSQL database.

1. Create the schema:

   ```bash
   psql "$DB_URL" -f db/schema.sql
   ```

2. Set the database connection through environment variables (nothing is hardcoded):

   ```bash
   export DB_URL="jdbc:postgresql://localhost:5432/todo"
   export DB_USER="your_user"
   export DB_PASSWORD="your_password"
   ```

3. Build and run:

   ```bash
   mvn clean package
   java -jar target/todo-webapp-1.0.0-jar-with-dependencies.jar
   ```

The app starts embedded Tomcat and serves the site locally.

## License

MIT. See [LICENSE](LICENSE).
