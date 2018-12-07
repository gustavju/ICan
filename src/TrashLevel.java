public class TrashLevel extends Sensor {

  private float level;

  TrashLevel(String Id, float level) {
    super(Id);
    this.level = level;
  }

  float getLevel() {
    return level;
  }
}
