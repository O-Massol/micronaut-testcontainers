package com.example;

import com.example.infrastructure.mongo.data.MongoSpiceReport;
import com.example.infrastructure.mongo.data.SpiceReportRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpiceReportRepositoryTest extends MongoTest {

    @Inject
    private SpiceReportRepository spiceReportRepository;

    @Test
    void findAll(){
        spiceReportRepository.save(new MongoSpiceReport(null, "Arrakis 10190-JAN-14", "We have a lot of Spice !"));
        var reports = spiceReportRepository.findAll();
        assertEquals( 1, reports.size());
    }

    @Override
    public MongoSetup mongoSetup() {
        return new MongoSetup("SPACEGUILD", List.of("spice-report-10190"));
    }
}
