# To-Do List

A to-do list that allows you to create, edit, and delete tasks.

## Running the program

You need to set up the database, backend, and frontend to run the program.

### 1. Database

Download [postgres](https://www.postgresql.org/download/).

#### Postgres setup:

-   user = `postgres`
-   password = `` (meaning no password.)
-   database = `postgres`
-   port = `5432`
-   server = `localhost`

#### Postgres table:

The java server expects to work with a table that adheres to the following structure:

```sql
CREATE TABLE public.tasks
(
    id smallserial NOT NULL,
    name character varying(64) NOT NULL,
    info text,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.tasks
    OWNER to postgres;
```

#### Postgres JDBC:

In order for java to connect to postgres, the postgres JDBC driver must be set up. The driver is already in this project at: `to-do-list/backend/src/main/java/com/example/postgresql-42.7.4.jar`. You can use this .jar for export to $CLASSPATH, which is essential for running the java server.

(Make sure the server for postgres is actually active before connecting/attempting to run the java server.)

### 2. Backend

Download [java](https://www.oracle.com/java/technologies/downloads/).

After downloading java, you can run the backend server through the subsequent steps:

1. Open a terminal
2. Change directory into `to-do-list/backend/src/main/java/com/example`
3. Run `export CLASSPATH=path/to/postgresql/jdbc/driver`. Make sure to replace `path/to/postgresql/jdbc/driver` with the path to the actual postgresql jdbc driver on your machine.
4. Run `java Main.java`. This starts the server on **localhost:8000**

### 3. Frontend

Download [Node.js and npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm).

After downloading node and npm, you can run the frontend server through the subsequent steps:

1. Open a terminal
2. Change directory into `to-do-list/frontend`
3. Run `npm install`. This installs necessary packages
4. Run `npm run dev`. This starts the server on **localhost:3000**
