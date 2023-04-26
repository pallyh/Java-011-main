package itstep.learning.ioc;

import com.google.inject.name.Named;


public class Service4v1 implements IService4 {
    @Override
    public void show() {
        System.out.println( "I am " + this ) ;
    }
}
