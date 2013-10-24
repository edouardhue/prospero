package prospero.commons.actions;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MerimeeCorrectorActionTest {

    private final String templateContent;
    
    public MerimeeCorrectorActionTest(final String templateContent) {
        this.templateContent = templateContent;
    }

    @Parameters
    public static List<String[]> data() {
        return Arrays.asList(
            new String[] {"|PA00090251"},
            new String[] {"|PA00090251|type=Inscrit"}
        );
    }
    
    @Test
    public void test() {
        final Matcher matcher = MerimeeCorrectorAction.ID_PATTERN.matcher(templateContent);
        Assert.assertTrue(matcher.find());
    }

}
