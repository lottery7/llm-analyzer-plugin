package com.lotterydev.schemas;

import java.util.ArrayList;
import java.util.List;

public class CodeQLFinding {
    String ruleId;
    Message message;
    List<Location> locations;

    public List<Finding> toFindings() {
        List<Finding> findings = new ArrayList<>();

        for (var location : locations) {
            var region = location.physicalLocation.region;
            int endLine = Math.max(region.startLine, region.endLine);
            findings.add(new Finding(ruleId, region.startLine, endLine, message.text));
        }

        return findings;
    }

    static class Message {
        String text;
    }

    static class Location {
        PhysicalLocation physicalLocation;

        static class PhysicalLocation {
            Region region;

            static class Region {
                int startLine;
                int endLine;
            }
        }
    }
}
