package bll.exceptions;

public class CategoriesException extends Throwable{
    public CategoriesException(String exceptionMessage, Exception exception){
        System.out.println(exceptionMessage + "\n" + exception);
    }
}
