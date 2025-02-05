package space.guild.infrastructure.mongo;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import space.guild.testing.KafkaMongoTest;
import space.guild.infrastructure.mongo.data.MongoSpiceReport;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpiceReportRepositoryTest extends KafkaMongoTest {

    @Inject
    private SpiceReportRepository spiceReportRepository;

    @Override
    public List<FileForCollection<?>> dataForInit() {
        try {
            URI spiceReportDataFileUri = null;
            spiceReportDataFileUri = this.getClass().getClassLoader().getResource("datasets/spiceReports.json").toURI();
            return List.of(
                    new FileForCollection<>("spice-report-10190",
                            MongoSpiceReport.class,
                            Path.of(spiceReportDataFileUri))
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAll() {
        var reports = spiceReportRepository.findAll();
        assertEquals(2, reports.size());
        var names = reports.stream().map(MongoSpiceReport::reportName).toArray();
        assertArrayEquals(new String[]{"Arrakis 10187-MAY-14","Arrakis 10189-MAR-22"}, names);
    }

    @Test
    void saveAndFindAll() {
        //given
        //when
        spiceReportRepository.save(new MongoSpiceReport(null, "Arrakis 10190-JAN-14", "We have a lot of Spice !"));
        //then
        var reports = spiceReportRepository.findAll().stream().filter(r -> "Arrakis 10190-JAN-14".equals(r.reportName())).toList();
        assertEquals(1, reports.size());
    }


    @Override
    public MongoSetup mongoSetup() {
        return new MongoSetup("SPACEGUILD", List.of("spice-report-10190"));
    }
}
