package ch.basler.cat.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

public class CodeValueId implements Serializable {

    @Id
    @GenericGenerator(name = "codevalue_id", strategy = "ch.basler.cat.model.CodeValueIdGenerator")
    @GeneratedValue(generator = "codevalue_id")
    private long value;

    @Id
    @Column(name = "codetype_id")
    private long type;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getType() {
        return type;
    }

    public void setType(long typeId) {
        this.type = typeId;
    }

    public static CodeValueId of(long type, long value) {
        CodeValueId id = new CodeValueId();
        id.setType(type);
        id.setValue(value);
        return id;
    }
}
