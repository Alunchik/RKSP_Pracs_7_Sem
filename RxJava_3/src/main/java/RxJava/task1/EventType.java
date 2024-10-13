package RxJava.task1;

public enum EventType {
    TEMPERATURE_EXCEEDED("Temperature threshold exceed"),
    CO2_EXCEEDED("CO2 threshold exceed"),
    ALARM("ALARM!!!"),
    OK("");


    EventType(String text){
        this.text=text;
    }
    public String text;

    public String getText(){
        return text;
    }
}
