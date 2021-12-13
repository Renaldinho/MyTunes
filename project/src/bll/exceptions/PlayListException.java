package bll.exceptions;

public class PlayListException extends Throwable{
    public PlayListException(String exceptionMessage, Exception exception){
        System.out.println(exceptionMessage + "\n" + exception);
    }
}
