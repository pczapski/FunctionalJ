package nawaman.functionalj.fields;

import static java.util.Arrays.asList;
import static nawaman.functionalj.fields.MayBeFieldTest.Driver.theDriver;
import static org.assertj.core.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static java.util.stream.Collectors.joining;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.AllArgsConstructor;
import lombok.val;

public class MayBeFieldTest {
    
    @AllArgsConstructor
    public static class Car {
        
        public static CarField<Car> theCar = car -> car;
        
        private String color;
        public String color() { return color; }
    }
    
    @AllArgsConstructor
    public static class Driver {
        
        public static DriverField<Driver> theDriver = driver -> driver;
        
        private Car car;
        public Car car() { return car; }
    }
    
    public static interface CarField<HOST> extends ObjectField<HOST, Car> {
        
        public default StringField<HOST> color() {
            return h -> linkTo(Car::color).apply(h);
        }
    }
    
    public static interface DriverField<HOST> extends ObjectField<HOST, Driver> {
        
        public default CarField<HOST> car() {
            return h -> linkTo(Driver::car).apply(h);
        }
    }
    
    @Test
    public void test() {
        val drivers = asList(
                new Driver(new Car("blue")),
                new Driver(new Car(null)));
        String resultAsString
                = drivers.stream()
                .map(theDriver.car().color().toMayBe().map(String::length).or(-1))
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        assertThat(resultAsString).isEqualTo("4,-1");
    }
    
}
