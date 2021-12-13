package bll.exceptions;

public class SongException extends Throwable{
    public SongException(String exceptionMessage, Exception exception){
        System.out.println(exceptionMessage + "\n" + exception);
    }
}
