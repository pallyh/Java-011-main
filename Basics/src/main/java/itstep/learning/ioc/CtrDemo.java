package itstep.learning.ioc;

import com.google.inject.Inject;
import com.google.inject.name.Named;

// демонстрация инъекции через конструктор
public class CtrDemo {
    private final IService3 service3 ;  // через конструктор можно инъектировать в константные поля
    private final String connectionString ;

    @Inject
    public CtrDemo(
            IService3 service3,
            @Named("OracleConnectionString" ) String connectionString ) {
        this.service3 = service3 ;
        this.connectionString = connectionString ;
    }

    public void show() {
        System.out.println( "CtrDemo has " + service3 + " " + connectionString ) ;
    }
}
