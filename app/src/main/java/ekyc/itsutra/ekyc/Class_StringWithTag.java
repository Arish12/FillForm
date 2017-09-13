package ekyc.itsutra.ekyc;

/**
 * Created by Asis on 5/18/2016.
 */
public class Class_StringWithTag {
    public String string;
    public Object tag;

    public Class_StringWithTag(String string, Object tag) {
        this.string = string;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return string;
    }
}
