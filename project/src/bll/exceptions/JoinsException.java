package bll.exceptions;

public class JoinsException extends Throwable{
    public JoinsException(String exceptionMessage, Exception exception){
        System.out.println(exceptionMessage + "\n" + exception);
    }
}
