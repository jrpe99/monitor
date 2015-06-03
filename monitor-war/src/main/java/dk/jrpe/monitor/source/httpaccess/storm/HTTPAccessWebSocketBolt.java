package dk.jrpe.monitor.source.httpaccess.storm;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import dk.jrpe.monitor.db.httpaccess.to.HTTPAccessTO;
import dk.jrpe.monitor.source.httpaccess.client.HTTPAccessWebSocketClient;

@SuppressWarnings("serial")
public class HTTPAccessWebSocketBolt extends BaseRichBolt {
    private OutputCollector collector;
    private HTTPAccessWebSocketClient client;
    
    @SuppressWarnings("rawtypes")
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        try {
			this.client = new HTTPAccessWebSocketClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    public void execute(Tuple tuple) {
        List<Object> values = tuple.getValues();
        values.stream().map((value) -> (HTTPAccessTO)value).forEach((to) -> {
            System.out.println("BOLT Thread : " + Thread.currentThread().getName() + " process HTTP Access IP: " + to.getIpAddress());
            try {
				this.client.sendToServer(to);
			} catch (Exception e) {
				e.printStackTrace();
			}
        });
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("number"));
    }

    @Override
    public void cleanup() {
    }
}