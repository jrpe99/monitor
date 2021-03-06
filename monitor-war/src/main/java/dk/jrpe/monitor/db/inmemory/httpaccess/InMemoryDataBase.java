package dk.jrpe.monitor.db.inmemory.httpaccess;

import dk.jrpe.monitor.db.httpaccess.to.HTTPAccessTO;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * Singelton<br>
 * Implementation of an in-memory database for HTTP access data with 
 * the same "schema" as the Cassandra database schema.<br>
 * See {@link dk.jrpe.monitor.db.cassandra.schema.HTTPAccessSchema}
 * @author Jörgen Persson
 */
public class InMemoryDataBase {
    private static class InstanceHolder {
        static final InMemoryDataBase instance = new InMemoryDataBase();
    }

    /*
      <IP Address, RequestCount>
    */
    private final ConcurrentHashMap<String,Long> httpSuccess = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,Long> httpFailed = new ConcurrentHashMap<>();

    /*
      HTTP Access success <Date to minute, RequestCount>
    */
    private final ConcurrentSkipListMap<String,Long> httpSuccessPerMinute = new ConcurrentSkipListMap<>();
    /*
      HTTP Access failed <Date to minute, RequestCount>
    */
    private final ConcurrentSkipListMap<String,Long> httpFailedPerMinute = new ConcurrentSkipListMap<>();

    /**
     * Return the instance of the in memory database.
     * @return the instance for the in memory database
     */
    public static InMemoryDataBase getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * Clear the database - remove ALL data
     */
    public void clear() {
    	this.httpSuccess.clear();
    	this.httpFailed.clear();
    	this.httpSuccessPerMinute.clear();
    	this.httpFailedPerMinute.clear();
    }
    
    public List<HTTPAccessTO> getHttpSuccess() {
        synchronized (this.httpSuccess) {
            return this.httpSuccess.entrySet().stream()
                    .map((entry) -> new HTTPAccessTO.Builder()
                            .setIPAdress(entry.getKey())
                            .setRequests(entry.getValue()).build())
                    .collect(Collectors.toList());
        }
    }

    public List<HTTPAccessTO> getHttpFailed() {
        synchronized (this.httpFailed) {
            return this.httpFailed.entrySet().stream()
                    .map((entry) -> new HTTPAccessTO.Builder()
                            .setIPAdress(entry.getKey())
                            .setRequests(entry.getValue()).build())
                    .collect(Collectors.toList());
        }
    }

    public List<HTTPAccessTO> getHttpSuccessPerMinute(String date, String from, String to) {
        return getHttpAccessPerMinute(this.httpSuccessPerMinute.tailMap(from));
    }
    
    public List<HTTPAccessTO> getHttpFailedPerMinute(String date, String from, String to) {
        return getHttpAccessPerMinute(this.httpFailedPerMinute.tailMap(from));
    }

    public void updateHttpSuccess(HTTPAccessTO to) {
        synchronized (this.httpSuccess) {
            Long requests = this.httpSuccess.putIfAbsent(to.getIpAddress(), new Long("1"));
            if(requests != null) {
                requests++;
                this.httpSuccess.put(to.getIpAddress(), requests);
            }
        }
    }

    public void updateHttpSuccessPerMinute(HTTPAccessTO to) {
        synchronized (this.httpSuccessPerMinute) {
            Long requests = this.httpSuccessPerMinute.putIfAbsent(to.getDateToMinute(), new Long("1"));
            if(requests != null) {
                this.httpSuccessPerMinute.put(to.getDateToMinute(), ++requests);
            }
        }
    }

    public void updateHttpFailed(HTTPAccessTO to) {
        synchronized (this.httpFailed) {
            Long requests = this.httpFailed.putIfAbsent(to.getIpAddress(), new Long("1"));
            if (requests != null) {
                this.httpFailed.put(to.getIpAddress(), ++requests);
            }
        }
    }

    public void updateHttpFailedPerMinute(HTTPAccessTO to) {
        synchronized (this.httpFailedPerMinute) {
            Long requests = this.httpFailedPerMinute.putIfAbsent(to.getDateToMinute(), new Long("1"));
            if(requests != null) {
                this.httpFailedPerMinute.put(to.getDateToMinute(), ++requests);
            }
        }
    }

    private List<HTTPAccessTO> getHttpAccessPerMinute(ConcurrentNavigableMap<String, Long> periodMap) {
        return periodMap.values().stream()
            .map((value) -> new HTTPAccessTO.Builder().setRequests(value).build())
            .collect(Collectors.toList());
    }    
}
