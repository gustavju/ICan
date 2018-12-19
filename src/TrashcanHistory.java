import java.util.ArrayList;

public class TrashcanHistory {
    private String trashcanId;
    private Location location;
    private ArrayList<TrashcanHistoryEntry> history;

    public TrashcanHistory(String trashcanId, Location location) {
        this.trashcanId = trashcanId;
        this.location = location;
        this.history = new ArrayList<TrashcanHistoryEntry>();
    }

    public void addEntry(TrashcanHistoryEntry trashcanHistoryEntry) {
        history.add(trashcanHistoryEntry);
    }

    public String getTrashcanId() {
        return trashcanId;
    }

    public String toJSON() {
        if (history.size() == 0)
            return "{ \"trashcanId\": \"" + trashcanId + "\", \"location\": " + location.toJSON() + "}";
        else
            return "{ \"trashcanId\": \"" + trashcanId + "\", \"location\": " + location.toJSON() + ", \"TrashcanHistoryEntry\":" + history.get(history.size() - 1).toJSON() + "}";
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
