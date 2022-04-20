package ru.job4j.pooh;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final Map<String, Map<String, Queue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = new Resp("", "501");
        if ("POST".equals(req.httpRequestType())) {
            rsl = processPOST(req);
        } else if ("GET".equals(req.httpRequestType())) {
            rsl = processGET(req);
        }
        return rsl;
    }

    private Resp processGET(Req req) {
        queue.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
        Map<String, Queue<String>> nameTopic = queue.get(req.getSourceName());
            nameTopic.putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String param = queue.getOrDefault(req.getSourceName(), new ConcurrentHashMap<>())
                    .getOrDefault(req.getParam(), new ConcurrentLinkedQueue<>())
                    .poll();
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
            for (Queue<String> value : nameTopic.values()) {
                value.offer(req.getParam());
            }
            status = "200";
        }
        return new Resp(req.getParam(), status);
    }
}
