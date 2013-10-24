package prospero.commons.tasks;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MerimeeCorrectorTaskTest {

    private final String text;
    
    private final int matches;
    
    public MerimeeCorrectorTaskTest(final String text, final int matches) {
        this.text = text;
        this.matches = matches;
    }
    
    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(
            new Object[] {"{{Mérimée|PA12345678}}", 1},
            new Object[] {"{{Mérimée|1=PA12345678}}", 1},
            new Object[] {"{{Mérimée|PA12345678|IA12345678}}", 1},
            new Object[] {"{{Mérimée|1=PA12345678|2=IA12345678}}", 1},
            new Object[] {"{{Mérimée|2=PA12345678|1=IA12345678}}", 1},
            new Object[] {"{{Mérimée|1=PA12345678|2=IA12345678|3=EA12345678}}", 1},
            new Object[] {"{{Mérimée|1=PA12345678|3=IA12345678|2=EA12345678}}", 1},
            new Object[] {"{{Mérimée|2=PA12345678|1=IA12345678|3=EA12345678}}", 1},
            new Object[] {"{{Mérimée|2=PA12345678|3=IA12345678|1=EA12345678}}", 1},
            new Object[] {"{{Mérimée|3=PA12345678|1=IA12345678|2=EA12345678}}", 1},
            new Object[] {"{{Mérimée|3=PA12345678|2=IA12345678|1=EA12345678}}", 1},
            new Object[] {"{{Mérimée|PA12345678|type=classé}}", 1},
            new Object[] {"{{Mérimée|PA12345678|type=inscrit}}", 1},
            new Object[] {"{{Mérimée|PA12345678|type=classé+inscrit}}", 1},
            new Object[] {"{{Mérimée|type=classé|PA12345678}}", 1},
            new Object[] {"{{Mérimée|1=PA12345678|type=classé}}", 1},
            new Object[] {"{{Mérimée|type=classé|1=PA12345678}}", 1},
            new Object[] {"{{Mérimée | type = classé | 1 = PA12345678 }}", 1},
            new Object[] {"{{Mérimée | type = classé | 1 = PA12345678\n}}", 1},
            new Object[] {"{{Mérimée|PA12345678}}\n\n{{Mérimée|IA12345678}}", 2}
        );
    }
    
    @Test
    public void test() {
        int matches = 0;
        final Matcher mériméeMatcher = MerimeeCorrectorTask.MÉRIMÉE_PATTERN.matcher(text);
        while (mériméeMatcher.find()) {
            matches++;
            for (int i = 1; i <= mériméeMatcher.groupCount(); i++) {
                final String template = mériméeMatcher.group(1);
                System.out.println(template);
                final Matcher idsMatcher = MerimeeCorrectorTask.ID_PATTERN.matcher(template);
                while (idsMatcher.find()) {
                    final String id = idsMatcher.group(1);
                    System.out.println(id);
                }
                final Matcher typeMatcher = MerimeeCorrectorTask.TYPE_PATTERN.matcher(template);
                if (typeMatcher.find()) {
                    System.out.println(typeMatcher.group(1));
                }
            }
        }
        assertEquals(this.matches, matches);
    }

}
