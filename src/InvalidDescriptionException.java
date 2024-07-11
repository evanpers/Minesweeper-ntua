public class InvalidDescriptionException extends Exception{
    InvalidDescriptionException(){
        Graphs.popUpMessage("InvalidDescriptionException");
    }

    @Override
    public String toString(){
        return "InvalidDescriptionException";
    }
}
