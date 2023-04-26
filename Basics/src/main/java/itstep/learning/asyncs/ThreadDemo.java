package itstep.learning.asyncs;


public class ThreadDemo {
    private Runnable runnable1 ;
    private Runnable runnable2 ;
    public void run() throws InterruptedException {
        System.out.println( "Thread Demo start" ) ;
        int x = 10 ;
        // region runnable1
        runnable1 = new Runnable() {
            private int y = x ;
            @Override
            public void run() {
                System.out.println( "runnable1 start, y = " + y ) ;
                try {
                    Thread.sleep( 1000 ) ;
                }
                catch( InterruptedException ex ) {
                    System.out.println( "Thread interrupted: " + ex.getMessage() ) ;
                }
                System.out.println( "runnable1 finish" ) ;
            }
        } ;
        // endregion

        runnable2 = () -> {   // функциональный интерфейс - реализация интерфейса с одним методом
            System.out.println( "runnable2 start" ) ;
            try {
                Thread.sleep( 100 ) ;
            }
            catch( InterruptedException ex ) {
                System.out.println( "Thread2 interrupted: " + ex.getMessage() ) ;
            }
            System.out.println( "runnable2 finish" ) ;
        } ;
        Runnable runnable3 = () -> {
            System.out.println( "runnable3 start" ) ;
            long w = 0;
            try {
                while( w < 1e10 ) {
                    w++;
                    if( Thread.currentThread().isInterrupted() )
                        throw new InterruptedException() ;
                }
            }
            catch( Exception ex ) {
                System.out.println( "Thread3 interrupted: " + ex.getMessage() ) ;
            }
            System.out.println( "runnable3 finish" ) ;
        } ;
        Thread thread3 = new Thread( runnable3 ) ;
        thread3.start() ;
        Thread thread2 = new Thread( runnable2 ) ;
        thread2.start() ;
        new ParamThread( 10 ).start() ;
        Thread thread1 = new Thread( runnable1 ) ;
        thread1.setPriority( Thread.MIN_PRIORITY ) ;
        thread1.start() ;  // ! run - синхронный запуск, start - асинхронный
        Thread.sleep( 10 ) ;
        thread1.interrupt() ;  // отмена потока 1 (в теле потока 1 будет выброшено InterruptedException из-за sleep)
        thread3.interrupt() ;  // в теле потока 3 нет sleep, в нем будет установлен isInterrupted() состояние которого постоянно проверяется
        thread2.join() ;  // ожидание завершения потока (вызывающий поток блокируется)
        System.out.println( "Thread Demo finish" ) ;
        /*
        for(i){
            new Thread( ()->{...} ).start();   // несколько операций new ()->{...}
        }
        runnable2 = ()->{...};  // одна операция
        for(i) {
            new Thread(runnable2).start();
        }
        */
    }

    static class ParamThread extends Thread {  // передача параметров в поток обычно реализуется
        // как поля объекта, наследующего Thread
        private final int param ;

        public ParamThread( int param ) {
            this.param = param ;
        }

        @Override
        public void run() {  // ! перегружаем метод run(), но вызываем поток методом start()
            System.out.println( "ParamThread with " + param ) ;
        }
    }
}
/*
Асинхронное программирование. Многопоточность.

Асинхронное программирование - подход, при котором возможно параллельное
(одновременное) выполнение кода.
- сетевое (код выполняется на разных узлах - ПК)
- многопроцессное (параллельно работают несколько процессов)
- многопоточное (параллельные потоки одного процесса)
- многозадачное (использование программных сущностей "задач" - Task, Promise, Future)

Многопоточность - использование системных ресурсов "потоков"
В ряде языков (в т.ч. WinAPI) поток запускается на функции/методе
В Java более строгий ООП и для старта потока используют объекты (с одним методом)
традиционно - это интерфейс Runnable

Синхронизация - создание условий при котором код НЕ выполняется параллельно.
В случае с потоками используется блокировка потоков
 */
/*
Реализовать:
- сделать чай (3000 мс)
- сделать тосты (1000 мс)
- поджарить яичницу (2000 мс)
Организовать вызов в разных потоках таким образом, чтобы приготовление заняло мин. время
и все задачи были выполнены
* Реализовать задачи в 2х потоках (есть только 2 конфорки на плите)
  --тосты-- JOIN--- яичница ---
  ------- чайник --------------
 */
