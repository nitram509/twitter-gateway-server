package net.nitram509.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EnvironmentConfig implements Mandatory, Optional {

  @Override
  public String consumerKey() {
    return System.getenv("twitter4j.oauth.consumerKey");
  }

  @Override
  public String consumerSecret() {
    return System.getenv("twitter4j.oauth.consumerSecret");
  }

  @Override
  public String defaultHashTag() {
    return System.getenv("defaultHashTag");
  }

  /**
   * Example:
   * DATABASE_URL = "postgres://user3123:passkja83kd8@ec2-117-21-174-214.compute-1.amazonaws.com:6212/db982398"
   *
   * @see <a href="https://devcenter.heroku.com/articles/heroku-postgresql#connecting-in-java">
   * Heroku Postgres - Connecting in Java
   * </a>
   */
  @Override
  public Connection getConnection() {
    URI dbUri = getConnectionUri();
    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + (dbUri.getPort() > 0 ? ":" + dbUri.getPort() : "") + dbUri.getPath();
    try {
      return DriverManager.getConnection(dbUrl, username, password);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public URI getConnectionUri() {
    try {
      final String database_url = System.getenv("DATABASE_URL");
      if (database_url == null) {
        throw new IllegalStateException("Environment variable DATABASE_URL is missing. Provide one, example DATABASE_URL='postgres://user:pass@host:port/database'");
      }
      return new URI(database_url);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}
