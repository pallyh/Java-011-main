package itstep.learning.ioc;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Service2 {
    @Inject
    private IService3 service3 ;

    public void show() {
        System.out.println( "I am " + this + ". I have " + service3 ) ;
    }
}
