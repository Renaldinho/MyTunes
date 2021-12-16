package bll.exceptions;


public class SongException extends Throwable{


    String exceptionMessage;
    public SongException(String exceptionMessage, Exception exception){
        this.exceptionMessage=exceptionMessage;
        System.out.println(exceptionMessage + "\n" + exception);
    }
    public String getExceptionMessage() {
        return this.exceptionMessage;
    }
}
