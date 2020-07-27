
package model;

/**
 *
 * @author suitcase
 */
public class LangString implements java.io.Serializable {
    
    private static final long serialVersionUID = 6382664478446016834L;

    private String value;
    private String lang;


    /**
     * Creates a LangString object.
     */
    public LangString() {
    	this(null, null);
    }   

    /**
     * Creates a LangString object using specified values.
     *
     * @param value     The text value.
     * @param lang      The value's language.
     */
    public LangString(String value, String lang) {
    	this.value = value;
        this.lang = lang;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


}