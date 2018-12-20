import java.util.ArrayList;

public class TrashcanHistory {
    private String trashcanId;
    private Location location;
    private ArrayList<TrashcanHistoryEntry> history;
    private boolean isConnected;

    public TrashcanHistory(String trashcanId, Location location) {
        this.trashcanId = trashcanId;
        this.location = location;
        this.history = new ArrayList<TrashcanHistoryEntry>();
        this.isConnected = true;
    }

    public void addEntry(TrashcanHistoryEntry trashcanHistoryEntry) {
        history.add(trashcanHistoryEntry);
    }

    public String getTrashcanId() {
        return trashcanId;
    }

    public TrashcanHistoryEntry getLastestHistory() {
        return history.get(history.size() - 1);
    }

    public String toJSON() {
        if (history.size() == 0)
            return "{ \"trashcanId\": \"" + trashcanId + "\", \"location\": " + location.toJSON() + ", \"isConnected\": \"" + isConnected + "\"}";
        else
            return "{ \"trashcanId\": \"" + trashcanId + "\", \"location\": " + location.toJSON() + ", \"TrashcanHistoryEntry\":" + history.get(history.size() - 1).toJSON() + ", \"isConnected\": \"" + isConnected + "\"}";
    }

    @Override
    public String toString() {
        return "TrashcanHistory{" +
                "trashcanId='" + trashcanId + '\'' +
                ", location=" + location +
                ", history=" + history +
                '}';
    }
}
