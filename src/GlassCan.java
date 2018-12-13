public class GlassCan extends Trashcan {
    private boolean flammable = false;

    @Override
    public void specificCalc() {
    }

    @Override
    public String toString() {
        return "GlassCan{" +
                "location=" + location +
                ", trashcanId='" + trashcanId + '\'' +
                '}';
    }
}
