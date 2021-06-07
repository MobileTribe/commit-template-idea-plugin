package i18n;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18NTest {

    @Test
    public void testDefault() {
        getResourceBundleString(Locale.getDefault(), "type.of.change");
        getResourceBundleString(Locale.getDefault(), "fix.description");
    }

    @Test
    public void testGiven() {
        Locale.setDefault(Locale.FRENCH);

        getResourceBundleString(Locale.JAPAN, "type.of.change");
        getResourceBundleString(Locale.JAPANESE, "fix.description");

        getResourceBundleString(Locale.US, "type.of.change");
        getResourceBundleString(Locale.ENGLISH, "fix.description");

        getResourceBundleString(Locale.SIMPLIFIED_CHINESE, "type.of.change");
        getResourceBundleString(Locale.CHINESE, "fix.description");

        Locale.setDefault(Locale.CHINESE);


        getResourceBundleString(Locale.SIMPLIFIED_CHINESE, "type.of.change");
        getResourceBundleString(Locale.CHINESE, "fix.description");
    }

    private void getResourceBundleString(Locale locale, String key) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/panel", locale);
        String typeOfChange = rb.getString(key);
        System.out.println(typeOfChange);
        Assert.assertNotNull(typeOfChange);
    }

}
