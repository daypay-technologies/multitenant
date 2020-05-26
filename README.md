
Requirements
============
* Java >= 11 (OpenJDK JVM is tested by our CI on Travis)
* MySQL 5.7

You can run the required version of the database server in a container, instead of having to install it, like this:

    docker run --name mysql-5.7 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mysql -d mysql:5.7

and stop and destroy it like this:

    docker rm -f mysql-5.7

Beware that this database container database keeps its state inside the container and not on the host filesystem.  It is lost when you destroy (rm) this container.  This is typically fine for development.  See [Caveats: Where to Store Data on the database container documentation](https://hub.docker.com/_/mysql) re. how to make it persistent instead of ephemeral.

Tomcat v9 is only required if you wish to deploy the Fineract WAR to a separate external servlet container.  Note that you do not require to install Tomcat to develop Fineract, or to run it in production if you use the self-contained JAR, which transparently embeds a servlet container using Spring Boot.  (Until FINERACT-730, Tomcat 7/8 were also supported, but now Tomcat 9 is required.)


Instructions how to run for local development
============

Run the following commands:
1. `./gradlew createDB -PdbName=fineract_tenants`
1. `./gradlew createDB -PdbName=fineract_default`
1. `./gradlew bootRun`


Instructions to build the JAR file
============
1. Extract the archive file to your local directory.
2. Run `./gradlew clean bootJar` to build a modern cloud native fully self contained JAR file which will be created at `build/libs` directory.
3. Start it using `java -jar build/libs/fineract-provider.jar` (does not require external Tomcat)


Instructions to build a WAR file
============
1. Extract the archive file to your local directory.
2. Run `./gradlew clean bootWar` to build a traditional WAR file which will be created at `build/libs` directory.
3. Deploy this WAR to your Tomcat v9 Servlet Container.

We recommend using the JAR instead of the WAR file deployment, because it's much easier.


Instructions to execute Integration Tests
============
> Note that if this is the first time to access MySQL DB, then you may need to reset your password.

Run the following commands, very similarly to how [.travis.yml](.travis.yml) does:
1. `./gradlew createDB -PdbName=fineract_tenants`
1. `./gradlew createDB -PdbName=fineract_default`
1. `./gradlew clean integrationTest`
