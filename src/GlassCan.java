public class GlassCan extends Trashcan {
    private boolean flammable = false;

    @Override
    public void specificCalc() {
    }

    @Override
    public String toString() {
        return "{" +
                "location=" + location +
                ", trashcanId='" + trashcanId + '\'' +
                '}';
    }
}
