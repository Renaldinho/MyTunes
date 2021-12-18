package bll.exceptions;

public class PlayListException extends Throwable{
    String exceptionMessage;
    public PlayListException(String exceptionMessage, Exception exception){
        System.out.println(exceptionMessage + "\n" + exception);
        this.exceptionMessage=exceptionMessage;
    }

    public String getExceptionMessage() {
        return this.exceptionMessage;
    }
}
