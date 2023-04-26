package itstep.learning.ioc;

public class Service4v2 implements IService4 {
    @Override
    public void show() {
        System.out.println( "I am " + this ) ;
    }
}