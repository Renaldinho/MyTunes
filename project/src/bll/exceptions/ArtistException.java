package bll.exceptions;

public class ArtistException extends Throwable {
    public ArtistException(String exceptionMessage, Exception exception){
        System.out.println(exceptionMessage +"\n" + exception);
    }
}
