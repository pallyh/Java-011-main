package itstep.learning.oop;

public class Book extends Literature implements Printed {
    private String author ;

    public Book( String author, String title ) {
        this.author = author ;
        super.setTitle( title ) ;
    }

    public String getAuthor() {
        return author ;
    }

    public void setAuthor( String author ) {
        this.author = author ;
    }

    @Override  // Annotation
    public String toString() {
        return this.author + ": " + super.toString() ;
    }
}
/*
    Parent{method}    Child:Parent{override method}
    Parent obj1 = new Parent() ;  obj1.method-->Parent::method
    Parent obj2 = new Child() ;  obj2.method-->(virtual)-Child
                                            -->(non-virtual)-Parent
 */