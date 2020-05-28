//package ml.socshared.bstatistics.service.impl;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UtilTest {
//    private List<Integer> l;
//    @BeforeEach
//    public void createArray() {
//        l = new ArrayList<>();
//        l.add(1);
//        l.add(1);
//        l.add(2);
//        l.add(2);
//        l.add(3);
//        l.add(3);
//        l.add(3);
//        l.add(3);
//        l.add(3);
//        l.add(4);
//        l.add(4);
//        l.add(5);
//    }
//    @Test
//    public void upperBoundTestIsFound1() {
//        int res = Util.upperBound(l, 3, (a, b) -> a.compareTo(b), 0,l.size());
//        Assertions.assertEquals(8, res);
//    }
//    @Test
//    public void upperBoundTestIsFound2() {
//        int res = Util.upperBound(l, 1, (a, b) -> a.compareTo(b), 0,l.size());
//        Assertions.assertEquals(1, res);
//    }
//
//    @Test
//    public void upperBoundTestIsNotFound() {
//        int res = Util.upperBound(l, 10, (a, b) -> a.compareTo(b), 0,l.size());
//        Assertions.assertEquals(-1, res);
//    }
//}