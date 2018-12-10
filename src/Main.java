public class Main {

    private void run() {
        Trashcan t1 = new Trashcan(new Location(59.406897, 17.944906));
        Trashcan t2 = new Trashcan(new Location(59.405450, 17.956345));
        Trashcan t3 = new Trashcan(new Location(59.401946, 17.946584));

        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan1", t1.getLevel(), t1.getTemperature()));
        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan2", t2.getLevel(), t2.getTemperature()));
        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan3", t3.getLevel(), t3.getTemperature()));

        t1.addTrash();
        t2.addTrash();
        t3.addTrash();
        t1.fakeTemperature();
        t2.fakeTemperature();
        t3.fakeTemperature();

        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan1", t1.getLevel(), t1.getTemperature()));
        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan2", t2.getLevel(), t2.getTemperature()));
        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan3", t3.getLevel(), t3.getTemperature()));

        t1.empty();
        t2.empty();
        t3.empty();
        t1.startTrashFire();
        t2.startTrashFire();
        t3.startTrashFire();

        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan1", t1.getLevel(), t1.getTemperature()));
        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan2", t2.getLevel(), t2.getTemperature()));
        System.out.println(
            String.format("%s Level:%f, Temp:%f", "Trashcan3", t3.getLevel(), t3.getTemperature()));
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
