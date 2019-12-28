package top.ccheng.springbootlearn.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ccheng
 * @date 2018/9/8
 */
@Component
public class RandomValue {
    @Value("${top.ccheng.random.stringValue}")
    private String stringValue;
    @Value("${top.ccheng.random.intValue}")
    private Integer intValue;
    @Value("${top.ccheng.random.intValue2}")
    private Integer intValue2;
    @Value("${top.ccheng.random.intValue3}")
    private Integer intValue3;
    @Value("${top.ccheng.random.longValue}")
    private Long longValue;
    @Value("${top.ccheng.random.longValue2}")
    private Long longValue2;
    @Value("${top.ccheng.random.longValue3}")
    private Long longValue3;
    @Value("${top.ccheng.random.uuidValue}")
    private String uuid;

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public Integer getIntValue2() {
        return intValue2;
    }

    public void setIntValue2(Integer intValue2) {
        this.intValue2 = intValue2;
    }

    public Integer getIntValue3() {
        return intValue3;
    }

    public void setIntValue3(Integer intValue3) {
        this.intValue3 = intValue3;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public Long getLongValue2() {
        return longValue2;
    }

    public void setLongValue2(Long longValue2) {
        this.longValue2 = longValue2;
    }

    public Long getLongValue3() {
        return longValue3;
    }

    public void setLongValue3(Long longValue3) {
        this.longValue3 = longValue3;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "RandomValue{" +
                "stringValue='" + stringValue + '\'' +
                ", intValue=" + intValue +
                ", intValue2=" + intValue2 +
                ", intValue3=" + intValue3 +
                ", longValue=" + longValue +
                ", longValue2=" + longValue2 +
                ", longValue3=" + longValue3 +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
