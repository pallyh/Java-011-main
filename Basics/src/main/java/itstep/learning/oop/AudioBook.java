package itstep.learning.oop;

public class AudioBook extends Literature implements Playable {
    private String speaker ;

    public AudioBook() {
    }

    public AudioBook(String title, String speaker) {
        this.speaker = speaker ;
        super.setTitle( title ) ;
    }

    @Override
    public String toString() {
        return String.format( "AudioBook: '%s', speaker: '%s'",
                super.getTitle(), this.speaker ) ;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    @Override
    public void play() {
        System.out.println( "Playing " + this.toString() ) ;
    }
}
