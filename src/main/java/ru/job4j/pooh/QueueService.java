package ru.job4j.pooh;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, Queue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if ("queue".equals(req.getPoohMode()) && "POST".equals(req.httpRequestType())) {
            rsl = processPOST(req);
        } else if ("queue".equals(req.getPoohMode()) && "GET".equals(req.httpRequestType())) {
            rsl = processGET(req);
        }
        return rsl;
    }

    private Resp processGET(Req req) {
        String param = queue.get(req.getSourceName()).poll();
        String status = "204";
        if (param == null) {
            param = "";
        } else {
            status = "200";
        }
        return new Resp(param, status);
    }

    private Resp processPOST(Req req) {
        queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
        String status = "204";
        if (queue.get(req.getSourceName()).offer(req.getParam())) {
            status = "200";
        }
        return new Resp(req.getParam(), status);
    }
}
