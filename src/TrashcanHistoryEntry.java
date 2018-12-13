import java.util.Date;

public class TrashcanHistoryEntry {
    private Date timestamp;
    private double temperature;
    private double trashLevel;
    private Date lastEmptied;

    public TrashcanHistoryEntry(double temperature, double trashLevel, Date lastEmptied) {
        this.timestamp = new Date();
        this.temperature = temperature;
        this.trashLevel = trashLevel;
        this.lastEmptied = lastEmptied;
    }

    @Override
    public String toString() {
        return "TrashcanHistoryEntry{" +
                "timestamp=" + timestamp +
                ", temperature=" + temperature +
                ", trashLevel=" + trashLevel +
                ", lastEmptied=" + lastEmptied +
                '}';
    }
}
