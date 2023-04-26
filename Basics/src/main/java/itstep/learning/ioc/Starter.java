package itstep.learning.ioc;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class Starter {
    @Inject private Service1 service1 ;    // в описании класса Service1 нет аннотаций ==> Transient
    @Inject private Service1 service1v2 ;  // Transient - создание новых объектов в каждой точке инъекции
    @Inject private Service2 service2 ;    // в описании класса Service1 аннотация @Singleton
    @Inject private Service2 service2v2 ;  // - инъекция того же объекта, что и service2
    @Inject private IService3 service3 ;   // более слабая связь - через интерфейс !РЕКОМЕНДОВАНО
    @Inject @Named("v1") private IService4 service4v1 ;
    @Inject @Named("v2") private IService4 service4v2 ;

    @Inject @Named("MySqlConnectionString")
    private String myCs;
    @Inject @Named("OracleConnectionString")
    private String oraCs;

    @Inject CtrDemo ctrDemo ;

    public void run() {
        System.out.println( "DI demo" ) ;
        service1.show() ;
        service1v2.show() ;
        service2.show() ;
        service2v2.show() ;
        service3.show() ;
        service4v1.show() ;
        service4v2.show() ;
        System.out.println( myCs ) ;
        System.out.println( oraCs ) ;
        ctrDemo.show() ;
    }

}
/*
    Инверсия управления. Внедрение зависимостей. Инверсия зависимости.

    Инверсия управления - "не зная типа исходных данных не можем работать дальше"
     решение - создание абстракций. Делаем интерфейс Data{plus(Data),divide(int)} и работаем с ним
     когда станет известен конкретный тип, мы реализуем интерфейс Complex:Data

    Внедрение зависимостей - создание "центра управления" жизненным циклом зависимостей
     Задача центра - решать вопрос создания новых объектов / использования ранее созданных

    Инверсия зависимости - ослабление связей, принцип "О" - дополнять, но не изменять.
     плохо ClassA{ private ClassB dependency } - сильная связь классов А и В, + отклонение
     от "О" - нельзя дополнить зависимость не изменив ClassB (дополнение == изменение)
     решение: ClassA{ private InterfaceB dependency } ClassB:InterfaceB
     дополнение - создание нового класса ClassBv2 и замена реализации для dependency
 */
/*
Контейнеры зависимостей
- Google Guice
- Spring
- встроенная система javax / CDI

Указываем зависимость https://mvnrepository.com/artifact/com.google.inject/guice
Создаем модуль настройки зависимостей AppModule
 */
/*
Создать службу Config подключения конфигурации из файла config.ini (key=value\n key=value\n...)
реализовать Config.getParameter(name) : String|Null
Инъектировать службу в Starter, вывести параметры на экран
 */