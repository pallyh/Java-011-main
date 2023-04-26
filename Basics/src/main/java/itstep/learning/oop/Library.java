package itstep.learning.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Library {
    private List<Literature> funds ;   // List - interface for collection

    public Library() {
        funds = new ArrayList<>() ;   // ArrayList - конкретный тип, <> - diamond operator
    }

    public void add( Literature literature ) {
        funds.add( literature ) ;
    }
    public void printFunds() {
        if( funds.size() == 0 ) {
            System.out.println( "Funds are empty" ) ;
        }
        else {
            for( Literature literature : funds ) {
                System.out.println( literature ) ;
            }
        }
    }
    public void showPrinted() {
        for( Literature literature : funds ) {
            if( literature instanceof Printed ) {
                System.out.println( literature.toString() ) ;
            }
        }
    }

    public void playAll() {
        for( Literature literature : funds ) {
            if( literature instanceof Playable ) {
                ((Playable) literature).play() ;
            }
        }
    }
}
/* ООП - Объекты + взаимодействие
 - Инкапсуляция
 - Наследование (литература --> книги, журналы, газеты)
 - Полиморфизм
 = Абстрагирование (книги, журналы, газеты --> литература)

 Book         Journal         Newspaper
  Author       Number          Date      --> Personal
  Title        Title           Title     --> General  --> Literature

Literature(title), Book(author)
? можно ли вызывать book.setTitle("..") ? Да
? можно ли вызывать literature.setAuthor() ? Нет
? можно ли присвоить this.title=".." (внутри класса Literature) ? Да
? можно ли присвоить this.title=".." (внутри класса Book) ? Нет
? Так есть ли поле title у объекта "книга" ? Да, но доступа к этому полю нет, только через аксессоры
? можно ли в классе Book описать свое поле title ? Да, можно
? Что при этом происходит? Поле перекрывает или заменяет родительское? Перекрывает,
    создается два поля title, одно this.title, второе super.title (скрыто по доступу)
? Если не переопределить геттер, какое значение вернет book.getTitle() ? super
book = new Book()
book.title = "1" (this.title)
book.getTitle() --> "" (super.title)

--Literature--
[title,get,set] -a-> [[title,get,set] + author,get,set] v (правильно)
                -б-> [[title],get,set + author,get,set]

[[title,get,set] + author,get,set,method{title}]  title-super
[[title,get,set] + author,get,set,method{title},title]  title-this
Два примера выше: method{title} без изменения кода переключится на другой title (this)
если его описать в классе-наследнике. Или наоборот, переключится на super, если
убрать его определение в наследнике.
- рекомендуется использовать приставки this или super перед всеми сущностями
- работа с данными (полями) должна ограничиваться тем классом, в котором они описаны
 */
