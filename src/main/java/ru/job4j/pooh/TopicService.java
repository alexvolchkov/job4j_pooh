package ru.job4j.pooh;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final Map<String, Map<String, Queue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if ("topic".equals(req.getPoohMode()) && "POST".equals(req.httpRequestType())) {
            rsl = processPOST(req);
        } else if ("topic".equals(req.getPoohMode()) && "GET".equals(req.httpRequestType())) {
            rsl = processGET(req);
        }
        return rsl;
    }

    private Resp processGET(Req req) {
        queue.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
        Map<String, Queue<String>> nameTopic = queue.get(req.getSourceName());
            nameTopic.putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String param = queue.get(req.getSourceName()).get(req.getParam()).poll();
            String status = "200";
        if (param == null) {
            param = "";
        }
        return new Resp(param, status);
    }

    private Resp processPOST(Req req) {
        Map<String, Queue<String>> nameTopic = queue.get(req.getSourceName());
        String status = "204";
        if (nameTopic != null) {
            for (String s : nameTopic.keySet()) {
                nameTopic.get(s).offer(req.getParam());
            }
            status = "200";
        }
        return new Resp(req.getParam(), status);
    }
}
