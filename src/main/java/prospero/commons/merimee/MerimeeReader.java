package prospero.commons.merimee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prospero.commons.merimee.Protection.Type;
import au.com.bytecode.opencsv.CSVReader;

public final class MerimeeReader {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MerimeeReader.class);

    public static final int MAGIC_MONUMENT_COUNT = 44236;
    
    private static final Pattern PROTECTION_PATTERN = Pattern.compile("(\\d{4}(?:/\\d{2}/\\d{2})?) : ((?:inscrit)|(?:classé)) MH");
    
    private static final SimpleDateFormat YEAR_DATE_PATTERN = new SimpleDateFormat("yyyy");
    
    private static final SimpleDateFormat FULL_DATE_PATTERN = new SimpleDateFormat("yyyy/MM/dd");
    
    private static final Map<String, Protection.Type> PROTECTIONS = new HashMap<>(2);
    static {
        PROTECTIONS.put("inscrit", Type.INSCRIT);
        PROTECTIONS.put("classé", Type.CLASSE);
    }

    public Map<String, Monument> read(final File merimeeFile) throws FileNotFoundException, IOException {
        final HashMap<String, Monument> monuments = new HashMap<>(MAGIC_MONUMENT_COUNT);
        try (final InputStreamReader reader = new InputStreamReader(new FileInputStream(merimeeFile), "ISO-8859-1")) {
            try (final CSVReader csvReader = new CSVReader(reader, '\t', '\0', 1)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    final Monument monument = this.readMonument(line);
                    monuments.put(monument.getRef(), monument);
                }
            }
        }
        return monuments;
    }
    
    private Monument readMonument(final String[] line) {
        if (line.length >= 15) {
            final Monument monument = new Monument();
            monument.setRef(line[0]);
            monument.setEtud(line[1]);
            monument.setLoca(line[2]);
            monument.setReg(line[3]);
            monument.setDept(line[4]);
            monument.setCom(line[5]);
            monument.setInsee(line[6]);
            monument.setTico(line[7]);
            monument.setAdrs(line[8]);
            monument.setStat(line[9]);
            monument.setAffe(line[10]);
            monument.setPpro(line[11]);
            monument.setDpro(line[12]);
            monument.setAutr(line[13]);
            monument.setScle(line[14]);
            this.analyzeProtections(monument);
            return monument;
        } else {
            throw new IllegalArgumentException("Unexpected field count");
        }
    }
    
    private void analyzeProtections(final Monument monument) {
        final String dpro = monument.getDpro().replaceAll("\\s+", " ").replaceAll("/+", "/");
        final Matcher matcher = PROTECTION_PATTERN.matcher(dpro);
        boolean matched = false;
        while (matcher.find()) {
            try {
                final String dateString = matcher.group(1);
                final Date date;
                if (dateString.length() == 4) {
                    date = YEAR_DATE_PATTERN.parse(dateString);
                } else {
                    date = FULL_DATE_PATTERN.parse(dateString);
                }
                final Protection.Type type = PROTECTIONS.get(matcher.group(2));
                final Protection protection = new Protection(date, type);
                monument.addProtection(protection);
                matched = true;
            } catch (final ParseException e) {
                throw new Error("Can't parse DPRO: " + dpro, e);
            }
        }
        if (!matched) {
            LOGGER.warn("{} did not match", dpro);
        }
    }
}
