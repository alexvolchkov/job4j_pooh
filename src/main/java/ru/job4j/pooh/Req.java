package ru.job4j.pooh;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        final int minParam = 4;
        String[] array = content.split("/|" + System.lineSeparator());
        if (array.length < minParam) {
            throw new IllegalArgumentException("Не верный запрос");
        }
        String httpRequestType = array[0].strip();
        String poohMode = array[1].strip();
        String sourceName;
        String param = "";
        if ((!"POST".equals(httpRequestType) && !"GET".equals(httpRequestType))
                || (!"topic".equals(poohMode) && !"queue".equals(poohMode))) {
            throw new IllegalArgumentException("Не верный запрос");
        }
        if ("GET".equals(httpRequestType) && "topic".equals(poohMode)) {
            sourceName = array[2].strip();
            String tempParam = array[3].strip();
            param = tempParam.substring(0, tempParam.indexOf(" HTTP"));
        } else {
            String tempSourceName = array[2].strip();
            sourceName = tempSourceName.substring(0, tempSourceName.indexOf(" HTTP"));
            for (int i = 3; i < array.length; i++) {
                if (array[i].contains("=")) {
                    param = array[i].strip();
                    break;
                }
            }
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
