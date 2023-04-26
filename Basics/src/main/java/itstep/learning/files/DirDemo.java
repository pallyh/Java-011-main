package itstep.learning.files;

import itstep.learning.ConsoleColors;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Имитация работы системной команды dir (ls)
 */
public class DirDemo {
    public void run() {
        String path = "./" ;
        File dir = new File( path ) ;  // создание объекта не равно созданию/открытию файла
        if( dir.exists() ) {   // проверка на существование
            System.out.println( path + " exists" ) ;
        }
        // определяем тип: объект File может быть связан как с файлом, так и с папкой (директорией)
        if( dir.isFile() ) {
            System.out.println( path + " is file" ) ;
        }
        else if( dir.isDirectory() ) {
            System.out.println( path + " is directory" ) ;
        }
        else {
            System.out.println( path + " is neither file nor directory" ) ;
        }
        SimpleDateFormat format = new SimpleDateFormat( "dd.MM.yy hh:mm:ss" ) ;
        // dir.list() - список файлов в папке (только имена)
        for( File file : dir.listFiles() ) {
            System.out.print(
                ConsoleColors.BLUE +
                    (file.canRead() ? "r" : "-" )
                    + ( file.canWrite() ? "w" : "-" )
                    + ( file.canExecute() ? "x  " : "-  " )
                + ConsoleColors.RESET
            ) ;

            System.out.print(
                ConsoleColors.GREEN +
                    format.format( new Date( file.lastModified() ) ) + "   "
                + ConsoleColors.RESET
            ) ;

            if( file.isFile() ) {
                System.out.print( file.length() + "\t" ) ;
            }
            else {
                System.out.print( "<DIR>\t" ) ;
            }
            System.out.println( file.getName() ) ;
        }
    }
}
/*
Работа с файлами
- работа с файлами как с файловой системой: просмотр файлов, создание папок, перенос/копирование
- использование файлов для хранения и восстановления данных
Инструменты Java по работе с файлами есть двух типов:
java.io.File
java.nio.Files
 */
