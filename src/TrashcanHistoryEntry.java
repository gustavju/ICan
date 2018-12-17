import java.util.Date;

public class TrashcanHistoryEntry {
    private Date timestamp;
    private double temperature;
    private double trashLevel;
    private CanStatus canStatus;
    private Date lastEmptied;

    public TrashcanHistoryEntry(double temperature, double trashLevel, CanStatus canStatus, Date lastEmptied) {
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

    public String toJSON() {
        return "{" +
                "\"timestamp\":\"" + timestamp +
                "\", \"temperature\":\"" + temperature +
                "\", \"trashLevel\":\"" + trashLevel +
                "\", \"lastEmptied\":\"" + lastEmptied +
                "\"}";
    }
}
