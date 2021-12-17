package bll.exceptions;

public class CategoriesException extends Throwable{

    String exceptionMessage;


    int id=0;
    public CategoriesException(String exceptionMessage, Exception exception){
        System.out.println(exceptionMessage + "\n" + exception);
        this.exceptionMessage=exceptionMessage;
    }
    public CategoriesException(String exceptionMessage, Exception exception,int id){
        System.out.println(exceptionMessage + "\n" + exception);
        this.exceptionMessage=exceptionMessage;
        this.id=id;
    }
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public int getId() {
        return id;
    }

}
