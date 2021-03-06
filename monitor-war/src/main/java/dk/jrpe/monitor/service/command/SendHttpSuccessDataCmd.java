package dk.jrpe.monitor.service.command;

import dk.jrpe.monitor.db.datasource.HttpAccessDataSource;
import dk.jrpe.monitor.db.datasource.DataSourceFactory;
import dk.jrpe.monitor.db.httpaccess.to.JsonHTTPAccessTO;
import dk.jrpe.monitor.json.JSONMapper;
import dk.jrpe.monitor.source.httpaccess.to.HTTPAccessTOFactory;

/**
 * Command for sending on successful HTTP request.
 * @author Jörgen Persson
 */
public class SendHttpSuccessDataCmd extends Command {
    private final HttpAccessDataSource dataSource = DataSourceFactory.get();
    
    @Override public void execute(CommandHandler cmdHandler) {
        JsonHTTPAccessTO to = JSONMapper.toJsonHTTPAccessTO(cmdHandler.getJson());
        if(to != null) this.dataSource.updateHttpSuccess(HTTPAccessTOFactory.convertToDbTo(to));
    }
}
