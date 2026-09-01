package com.nookure.staff.paper.inventory.extenion;

import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DataFormatExtension extends AbstractExtension implements Function {
    @Override
    public Map<String, Function> getFunctions() {
        return Map.of("dataFormat", this);
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        Instant date = (Instant) args.get("date");
        String format;

        if (args.containsKey("format")) {
            format = (String) args.get("format");
        } else {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault());

        return formatter.format(date);
    }

    @Override
    public List<String> getArgumentNames() {
        return List.of("date", "format");
    }
}
