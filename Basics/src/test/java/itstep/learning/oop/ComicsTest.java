package itstep.learning.oop;

import junit.framework.TestCase;

import java.text.ParseException;
import java.util.Date;

public class ComicsTest extends TestCase {

    public void testToString() {
        try {
            Comics sample = new Comics( "Marvel", 1, "13.09.2022" ) ;
            assertEquals( sample.toString(), "Comics: Marvel No 1 (13.09.22)" ) ;
        }
        catch( ParseException ignored ) {
            fail( "Date parse error" ) ;
        }
    }

    public void testGetNumber() {
        try {
            Comics sample = new Comics( "Marvel", 1, "13.09.2022" ) ;
            assertEquals( sample.getNumber(), 1 ) ;
        }
        catch( ParseException ignored ) {
            fail( "Date parse error" ) ;
        }
    }

    public void testSetNumber() {
        Comics sample = new Comics() ;
        sample.setNumber( 10 ) ;
        assertEquals( sample.getNumber(), 10 ) ;
    }

    public void testGetDate() {
        Date date = new Date() ;
        Comics sample = new Comics() ;
        sample.setDate( date ) ;
        assertEquals( sample.getDate(), date ) ;
    }

    public void testSetDate() {
        Date date = new Date() ;
        Comics sample = new Comics() ;
        sample.setDate( date ) ;
        assertEquals( sample.getDate(), date ) ;
    }
}