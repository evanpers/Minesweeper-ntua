public class InvalidValueException extends Exception {

    InvalidValueException(){
        Graphs.popUpMessage("InvalidValueException");
    }

    @Override
    public String toString(){
        return "InvalidValueException";
    }
}
