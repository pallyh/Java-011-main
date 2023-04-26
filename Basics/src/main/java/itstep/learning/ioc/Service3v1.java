package itstep.learning.ioc;

import com.google.inject.Singleton;

@Singleton
public class Service3v1 implements IService3 {
    @Override
    public void show() {
        System.out.println( "I am " + this ) ;
    }
}
