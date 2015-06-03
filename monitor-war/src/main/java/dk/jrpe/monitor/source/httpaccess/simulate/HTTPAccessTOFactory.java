package dk.jrpe.monitor.source.httpaccess.simulate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import dk.jrpe.monitor.db.httpaccess.to.HTTPAccessTO;
import dk.jrpe.monitor.db.httpaccess.to.JsonHTTPAccessTO;
import dk.jrpe.monitor.webservice.endpoint.generated.HTTPAccessData;

/**
 *
 * @author Jörgen Persson
 */
public class HTTPAccessTOFactory {

    private HTTPAccessTOFactory() {
    }

    public static HTTPAccessData createSimulatedForCXF(HTTPAccessTO to) {
    	HTTPAccessData httpAccessData = new HTTPAccessData();
    	httpAccessData.setAction(to.getAction());
    	httpAccessData.setDate(to.getDate());
    	httpAccessData.setDateTime(to.getDateTime());
    	httpAccessData.setDateToMinute(to.getDateToMinute());
    	httpAccessData.setHttpStatus(to.getHttpStatus());
    	httpAccessData.setIpAddress(to.getIpAddress());
    	httpAccessData.setRequests(to.getRequests());
    	httpAccessData.setUrl(to.getUrl());
    	return httpAccessData;
    }
    
    public static JsonHTTPAccessTO createSimulatedForJSON(HTTPAccessTO to) {
    	return new JsonHTTPAccessTO(to);
    }
    
    public static HTTPAccessTO createSimulated() {
        Random random = new Random();
        int index = random.nextInt(SimulationConstants.HTTP_STATUS_LIST.size());
        String httpStatus = SimulationConstants.HTTP_STATUS_LIST.get(index);
        
        index = random.nextInt(9) * 10;
        String ipAddress = "1" + index + ".";
        index = random.nextInt(9) * 10;
        ipAddress = ipAddress + "132.100." + "2" + index;
        
        index = random.nextInt(SimulationConstants.ACTION_LIST.size());
        String action = SimulationConstants.ACTION_LIST.get(index);
        
        index = random.nextInt(SimulationConstants.URL_LIST.size());
        String url = SimulationConstants.URL_LIST.get(index);
        
        ZonedDateTime now = ZonedDateTime.now().withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        String date = formatter.format(now);
            
        formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String dateToMinute = formatter.format(now);
        
        now = ZonedDateTime.now();
        
        //httpStatus, ipAddress, date, dateToMinute
        return new HTTPAccessTO.Builder()
                .setIPAdress(ipAddress)
                .setHttpStatus(httpStatus)
                .setDate(date)
                .setDateToMinute(dateToMinute)
                .setDateTime(""+now.toEpochSecond())
                .setAction(action)
                .setUrl(url)
                .build();
    }
}
