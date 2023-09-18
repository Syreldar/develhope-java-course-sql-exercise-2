package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/newdb", "developer", "developer");
            Statement statement = connection.createStatement();

            // Ensure the table gets recreated each time.
            statement.executeUpdate("DROP TABLE students;");

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS students (
                  student_id INT(10) NOT NULL AUTO_INCREMENT,
                  last_name VARCHAR(30),
                  first_name VARCHAR(30),
                  PRIMARY KEY (student_id)
                );
            """);

            statement.executeUpdate("""
                INSERT INTO students (last_name, first_name) VALUES
                    ('Smith', 'John'),
                    ('Doe', 'Jane'),
                    ('Brown', 'Emily'),
                    ('Williams', 'Mark');
            """);

            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM students"))
            {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next())
                {
                    for (int i = 1; i <= columnCount; i++)
                    {
                        String columnName = metaData.getColumnName(i);
                        Object value = resultSet.getObject(i);
                        System.out.printf("%s: %s, ", columnName, value);
                    }

                    System.out.println();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}