package dk.jrpe.monitor.db.cassandra.dao.httpaccess;

import dk.jrpe.monitor.db.cassandra.CassandraConnectionHandler;
import dk.jrpe.monitor.db.httpaccess.to.HTTPAccessTO;


public class CassandraHTTPAccessWriteDAO extends CassandraDAO {

    public CassandraHTTPAccessWriteDAO(CassandraConnectionHandler conn) {
        super(conn);
    }
    
    /**
     * See {@link dk.jrpe.monitor.db.datasource.HttpAccessDataSource#saveHttpAccess(HTTPAccessTO, int)}
     */
    public void saveHttpAccess(HTTPAccessTO to, int hour) {
        StringBuilder cql = new StringBuilder();
        cql.append("INSERT INTO httpaccess.http_access (hour,ip_address,http_status,action,url,date_to_minute) ");
        cql.append("VALUES ");
        cql.append("(");
        cql.append(hour).append(",");
        cql.append("'").append(to.getIpAddress()).append("',");
        cql.append("'").append(to.getHttpStatus()).append("',");
        cql.append("'").append(to.getAction()).append("',");
        cql.append("'").append(to.getUrl()).append("',");
        cql.append("'").append(to.getDateTime()).append("'");
        cql.append(")");
        conn.execute(cql.toString());
    }

    /**
     * See {@link dk.jrpe.monitor.db.datasource.HttpAccessDataSource#updateHttpSuccess(HTTPAccessTO)}
     */
    public void updateHttpSuccess(HTTPAccessTO to) {
        StringBuilder cql = new StringBuilder();
        cql.append("UPDATE httpaccess.http_success ");
        cql.append("SET ");
        cql.append("requests=requests+1 ");
        cql.append("WHERE ");
        cql.append("ip_address='").append(to.getIpAddress()).append("'");
        conn.execute(cql.toString());
    }

    /**
     * See {@link dk.jrpe.monitor.db.datasource.HttpAccessDataSource#updateHttpSuccessPerMinute(HTTPAccessTO)}
     */
    public void updateHttpSuccessPerMinute(HTTPAccessTO to) {
        StringBuilder cql = new StringBuilder();
        cql.append("UPDATE httpaccess.http_success_per_minute ");
        cql.append("SET ");
        cql.append("requests=requests+1 ");
        cql.append("WHERE ");
        cql.append("date='").append(to.getDate()).append("' and ");
        cql.append("date_to_minute='").append(to.getDateToMinute()).append("'");
        conn.execute(cql.toString());
    }

    /**
     * See {@link dk.jrpe.monitor.db.datasource.HttpAccessDataSource#updateHttpFailed(HTTPAccessTO)}
     */
    public void updateHttpFailed(HTTPAccessTO to) {
        StringBuilder cql = new StringBuilder();
        cql.append("UPDATE httpaccess.http_failed ");
        cql.append("SET ");
        cql.append("requests=requests+1 ");
        cql.append("WHERE ");
        cql.append("ip_address='").append(to.getIpAddress()).append("'");
        conn.execute(cql.toString());
    }

    /**
     * See {@link dk.jrpe.monitor.db.datasource.HttpAccessDataSource#updateHttpFailedPerMinute(HTTPAccessTO)}
     */
    public void updateHttpFailedPerMinute(HTTPAccessTO to) {
        StringBuilder cql = new StringBuilder();
        cql.append("UPDATE httpaccess.http_failed_per_minute ");
        cql.append("SET ");
        cql.append("requests=requests+1 ");
        cql.append("WHERE ");
        cql.append("date='").append(to.getDate()).append("' and ");
        cql.append("date_to_minute='").append(to.getDateToMinute()).append("'");
        conn.execute(cql.toString());
    }
}
