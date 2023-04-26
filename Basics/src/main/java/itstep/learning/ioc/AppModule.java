package itstep.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind( IService3.class ).to( Service3v1.class ) ;

        // Если необходимо несколько реализаций одного интерфейса, то их разделяют именами
        bind( IService4.class )
                .annotatedWith( Names.named( "v1" ) )
                .to( Service4v1.class )
                .in( Singleton.class ) ;
        bind( IService4.class )
                .annotatedWith( Names.named( "v2" ) )
                .to( Service4v2.class )
                .in( Singleton.class ) ;

        // привязка к реализации (объекту)
        bind( String.class )
                .annotatedWith( Names.named( "MySqlConnectionString" ) )
                .toInstance( "The MySQL Connection String" ) ;
        bind( String.class )
                .annotatedWith( Names.named( "OracleConnectionString" ) )
                .toInstance( "The Oracle Connection String" ) ;
    }
}
