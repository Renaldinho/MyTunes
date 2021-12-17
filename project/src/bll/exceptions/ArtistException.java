package bll.exceptions;

public class ArtistException extends Throwable {
    String exceptionMessage;

    public ArtistException(String exceptionMessage, Exception exception){
         this.exceptionMessage=exceptionMessage;
        System.out.println(exceptionMessage +"\n" + exception);
    }
    public String getExceptionMessage() {
        return exceptionMessage;
    }

}
