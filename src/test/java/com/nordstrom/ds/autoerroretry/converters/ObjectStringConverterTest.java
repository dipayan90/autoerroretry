package com.nordstrom.ds.autoerroretry.converters;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Serializable;

@RunWith(MockitoJUnitRunner.class)
public class ObjectStringConverterTest implements Serializable{

    class TestClass implements Serializable{

        int intValue;
        String stringValue;
        NestedClass nestedClass;

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public NestedClass getNestedClass() {
            return nestedClass;
        }

        public void setNestedClass(NestedClass nestedClass) {
            this.nestedClass = nestedClass;
        }

    }

    class NestedClass implements Serializable{

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        String address;
    }

    @Test
    public void testConversionLogic() throws IOException, ClassNotFoundException {
        ObjectStringConverter converter = ObjectStringConverter.getObjectStringConverter();
        TestClass testClass = new TestClass();
        testClass.setIntValue(10);
        testClass.setStringValue("10");
        NestedClass nestedClass = new NestedClass();
        nestedClass.setAddress("address");
        testClass.setNestedClass(nestedClass);
        String toString = converter.toString(testClass);
        TestClass afterConversion = (TestClass) converter.fromString(toString);
        Assert.assertEquals(10, afterConversion.intValue);
    }

}
