package dk.jrpe.monitor.source.httpaccess.simulate;

import java.net.MalformedURLException;
import java.util.Random;

import dk.jrpe.monitor.source.httpaccess.client.CXFWebServiceClient;
import dk.jrpe.monitor.source.httpaccess.to.HTTPAccessTOFactory;
import dk.jrpe.monitor.webservice.cxf.CXFWebServiceMode;

public class SimulatorCXFSyncWebServiceClient {
    public static void main(String[] args) {
    	try {
			new SimulatorCXFSyncWebServiceClient().simulate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private final CXFWebServiceClient client;
    
    public SimulatorCXFSyncWebServiceClient() throws MalformedURLException {
        this.client = new CXFWebServiceClient(CXFWebServiceMode.SYNC);
	}

	public void simulate() throws Exception {
        Random random = new Random();
        while (true) {
        	this.client.sendToServer(HTTPAccessTOFactory.createSimulated());
        	int sleepTime = random.nextInt(500);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
        }
    }
}