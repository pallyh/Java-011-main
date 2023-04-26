package itstep.learning.db;

import java.sql.*;
import java.util.Random;

/**
 * Демонстрация работы с базами данных
 */
public class DbDemo {
    public void run() {
        // регистрируем драйвер (обязательно для JDK <= 8)
        try {
            Class.forName( "com.mysql.cj.jdbc.Driver" ) ;
        }
        catch( ClassNotFoundException ex ) {
            System.err.println( "ClassNotFoundException: " + ex.getMessage() ) ;
            return ;
        }

        // Подключение
        Connection connection ;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/java_011?useUnicode=true&characterEncoding=UTF-8",
                    "user_011", "pass_011"
            ) ;
        }
        catch( SQLException ex ) {
            System.err.println( "Connection error: " + ex.getMessage() ) ;
            return ;
        }
        System.out.println( "Connection OK" ) ;

        // Запросы: раздельно запросы с результатом и без результата
        String sql = "CREATE TABLE  IF NOT EXISTS  rands ( id CHAR(36) PRIMARY KEY, num INT, str VARCHAR(32) ) " +
                "ENGINE=INNODB DEFAULT CHARSET=utf8 " ;
        try( Statement statement = connection.createStatement() ) {
            statement.executeUpdate( sql ) ;
        }
        catch( SQLException ex ) {
            System.err.println( "CREATE error: " + ex.getMessage() ) ;
            return ;
        }
        System.out.println( "CREATE OK" ) ;

        Random random = new Random() ;
        //                             параметры: 1  2
        sql = "INSERT INTO rands VALUES ( UUID(), ?, ? )" ;   // подготовленный запрос - с параметрами
        try( PreparedStatement preparedStatement = connection.prepareStatement( sql ) ) {
            // на место первого "?" ставим число
            preparedStatement.setInt( 1, random.nextInt( 1000 ) ) ;  // !! отсчет параметров начинается с 1 (не с 0)
            // на место второго - строку
            preparedStatement.setString( 2, "str" + random.nextInt( 1000 ) ) ;
            // после заполнения всех параметров выполняем запрос
            preparedStatement.executeUpdate() ;
        }
        catch( SQLException ex ) {
            System.err.println( "INSERT error: " + ex.getMessage() ) ;
            return ;
        }
        System.out.println( "INSERT OK" ) ;

        // запросы с возвратом результата
        sql = "SELECT id, num, str FROM rands" ;
        try( Statement statement = connection.createStatement() ) {
            ResultSet res = statement.executeQuery( sql ) ;
            while( res.next() ) {  // ~fetch  ~Read
                System.out.println( res.getString(1) + " " + res.getInt(2) + " " + res.getString(3) ) ;
            }
            res.close() ;
        }
        catch( SQLException ex ) {
            System.err.println( "SELECT error: " + ex.getMessage() ) ;
            return ;
        }
        // задание: использовать параметрический запрос в который передается число, а в выборку
        //  попадают только те данные, у которых num меньше этого числа
        // Д.З. то же самое, но в запрос передается строка, а выборка должна содержать введенную строку
        //  как подстроку в поле str (поисковый режим). Саму строку запросить у пользователя в консоли
    }
}
/*
Работа с БД. JDBC.
Коннекторы - библиотеки для работы БД.
1. Создаем БД и пользователя для нее
    CREATE DATABASE java_011 ;
    CREATE USER 'user_011'@'localhost' IDENTIFIED BY 'pass_011' ;
    GRANT ALL PRIVILEGES ON java_011.* TO user_011 ;
    FLUSH PRIVILEGES ;
2. Скачиваем коннектор
    а) как JAR файл с сайта mysql
    б) как зависимость Maven (https://mvnrepository.com/artifact/mysql/mysql-connector-java)
        выбираем последнюю версию,
        копируем текст зависимости
        вставляем его в pom.xml (в секцию <dependencies>)
        обновляем Maven зависимости
 */
