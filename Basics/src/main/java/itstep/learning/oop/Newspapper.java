package itstep.learning.oop;

import java.sql.Date;

public class Newspapper extends Literature implements Printed {
    /*
    Описать класс Newspaper
    Реализовать в нем поле для даты выхода газеты +
    Реализовать методы-аксессоры, а также toString +
    Добавить в фонды несколько объектов класса Newspaper    
     убедиться в работоспособности функции вывода фондов 
     */
    private String author ;
    private Date newsDate;
    private String newsDateString;

    public Newspapper( String author, String title ) {
        this.author = author ;
        super.setTitle( title ) ;
    }

    public String getAuthor() {
        return author ;
    }

    public void setAuthor( String author ) {
        this.author = author ;
    }

    public Date getDate(){
        
        return newsDate;
    }

    public void setDate(Date newsDate){
        this.newsDate = newsDate;
    }
   
    public void dateToString() {
        newsDateString = newsDate.toString();
    }

    @Override  
        public String toString() {
        return "date realise : "+ newsDateString + " Author : " + this.author  ;
    }
}