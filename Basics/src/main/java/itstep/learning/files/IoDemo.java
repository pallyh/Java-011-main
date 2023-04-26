package itstep.learning.files;

import itstep.learning.ConsoleColors;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Файлы, часть 2 - хранение данных в файлах
 */
public class IoDemo {
    public void run() {
        String name = "results.txt" ;
        Path path = Paths.get( name ) ;   // Path(nio) -- File(io)
        if( Files.exists( path ) ) {
            try( InputStream inputStream = Files.newInputStream( path ) ) {
                StringBuilder sb = new StringBuilder() ;
                int code ;
                while( ( code = inputStream.read() ) != -1 ) {
                    sb.append( (char) code ) ;
                }
                System.out.println( ConsoleColors.YELLOW + "File content:" + ConsoleColors.RESET ) ;
                System.out.println( ConsoleColors.CYAN + sb.toString() + ConsoleColors.RESET ) ;
            }
            catch( IOException ex ) {
                System.err.println( ex.getMessage() ) ;
            }
        }
        else {
            // ~using - AutoClosable
            try( FileWriter writer = new FileWriter( name ) ) {
                writer.write( "Some results: \nLine 1\n Line 2" ) ;
                writer.flush() ;
            }
            catch( IOException ex ) {
                System.err.println( ex.getMessage() ) ;
            }
            System.out.println( ConsoleColors.YELLOW + "File created" + ConsoleColors.RESET ) ;
        }
    }
}
/*
а) сгенерировать файл со случайным кол-вом строк, длина которых также случайна
б) реализовать поиск самой длинной строки в этом файле (вывести номер строки, ее длину и саму строку)
 */