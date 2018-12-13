public class GarbageTruckMain {

    private void run( double longitude, double latitude) {
        GarbageTruck garbageTruck = new GarbageTruck(new Location(longitude, latitude));



    }
    public static void main(String [] args){

        double longitude;
        double latitude;
        if (args.length == 0) {
            longitude = 66.00;
            latitude = 66.00;
        } else {
            longitude = Double.parseDouble(args[0]);
            latitude = Double.parseDouble(args[1]);
        }
        new GarbageTruckMain().run(longitude, latitude);

    }
}
