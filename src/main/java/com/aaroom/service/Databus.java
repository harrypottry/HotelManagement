package com.aaroom.service;

import com.aaroom.service.impl.RedisCacheService;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.*;

@Component
public class Databus {

    private static final Logger LOG = LoggerFactory.getLogger(Databus.class);

    private volatile boolean running = false;

    @Value("${canal.host}")
    private String host;

    @Value("${canal.port}")
    private Integer port;

    @Value("${canal.instance}")
    private String instance;

    @Value("${canal.username}")
    private String username;

    @Value("${canal.password}")
    private String password;

    private CanalConnector connector;

    @Autowired
    private RedisCacheService redisCacheService;

    private Thread localThread;

    private static final String MYSQL_TRUE = "1";

    /**
     * 只有这些属性发生变化才触发更新
     */
    private static final Set<String> FIELDS_STARTUP = new HashSet<>(
            Arrays.asList("type", "name", "companyName", "concept", "residence", "product", "geo", "fundStage",
                    "fundState", "stage", "type", "hidden", "credit", "completeness", "weight", "adscore"));

    private static final Set<String> FIELDS_USER = new HashSet<>(
            Arrays.asList("name", "gender", "intro", "description", "residence", "address", "geo", "level", "founder",
                    "leader", "concept", "achievement", "credit", "completeness", "weight"));

    private static final Set<String> FIELDS_TAG = new HashSet<>(Arrays.asList("name", "extra", "alias", "pinyin"));

    private static final String COL_HEAT = "heat";

    public void start() {
        if (running) {
            LOG.warn("databus is already running");
            return;
        }

        connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(host, port), instance,
                username, password);

        connector.connect();
        connector.subscribe("aaroom\\..*");
        connector.rollback();

        running = true;
            while (running) {
                try {
                    int batchSize = 10;
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();

                    if (batchId == -1 || size == 0) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    process(batchId, message);
                } catch (Exception e) {
                    if (LOG.isErrorEnabled())
                        LOG.error("处理消息错误", e);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    private void process(long batchId, Message message) {

        List<Entry> entries = message.getEntries();

        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
                    || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            Header header = entry.getHeader();
            if (!"aaroom".equalsIgnoreCase(header.getSchemaName()))
                continue;

            String table = header.getTableName();
            EventType eventType = rowChage.getEventType();
            for (RowData rowData : rowChage.getRowDatasList()) {
                try {
                    processRow(table, eventType, rowData);
                } catch (Exception e) {
                    if (LOG.isErrorEnabled())
                        LOG.error("处理SQL出错", e);
                }
            }

        }
        connector.ack(batchId);
    }

    private void processRow(String table, EventType eventType, RowData rowData) {
        if("cloth_log".equals(table)) {
            prossessClothLog(eventType,rowData);
        }
    }

    private void prossessClothLog(EventType eventType, RowData rowData) {
        Map<String, Column> columns = getColumns(eventType, rowData);
        String id = columns.get("cloth_id").getValue();
        String possessor_type = columns.get("possessor_type").getValue();
        String possessor_id = columns.get("possessor_id").getValue();
        System.out.println("C:"+id+"insert:possessor_type:"+possessor_type+"&possessor_id"+possessor_id);
    }


    private Map<String, Column> getColumns(EventType eventType, RowData row) {
        Collection<Column> columns = null;
        switch (eventType) {
            case DELETE:
                columns = row.getBeforeColumnsList();
                break;
            case INSERT:
            case UPDATE:
                columns = row.getAfterColumnsList();
                break;
            default:
                break;
        }

        if (columns == null)
            return null;

        return asMap(columns);
    }

    private Map<String, Column> asMap(Collection<Column> columns) {
        Map<String, Column> ret = new HashMap<>();
        for (Column c : columns)
            ret.put(c.getName(), c);
        return ret;
    }

}
