public class PaperCan extends Trashcan {
    @Override
    public void specificCalc() {

    }

    @Override
    public String typeAsJson() {
        return "\"type:\":\"paper\"";
    }

    @Override
    public String toString() {
        return "{" +
                "location=" + location +
                ", trashcanId='" + trashcanId + '\'' +
                '}';
    }
}
