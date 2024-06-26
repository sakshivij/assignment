package com.root.Advice;

import net.bytebuddy.asm.Advice;

public class AdviceAgents {
 
    public static class ApiRecordAdvice{

        @Advice.OnMethodExit
        public static void after(@Advice.Return(readOnly = false) String returnValue) {
            // Set fixed return value
            System.out.println("Byte buddy Intercept. Return response: " + returnValue);
        }

    }

    public static class ApiReplayAdvice{
        private static Long value = 0L;
        
        @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
        public static boolean before() {
            // Skip original method execution (false is the default value for boolean)
            return false;
        }

    @Advice.OnMethodExit
    public static void after(@Advice.Return(readOnly = false) Long returnValue) {
        // Set fixed return value
        returnValue = value + 1;
        System.out.println("Byte buddy Intercept. HardCoded response" + returnValue);
    }
 
    }

    public static class DatabaseRecordAdvice{
        @Advice.OnMethodExit
        public static void after(@Advice.Return(readOnly = false) Long returnValue) {
            // Set fixed return value
            System.out.println("Byte buddy Intercept. Return response: " + returnValue);
        }
        
    }

    public static class DatabaseReplayAdvice{
        private static Long value = 0L;
        
        @Advice.OnMethodEnter(skipOn = Advice.OnDefaultValue.class)
        public static boolean before() {
            // Skip original method execution (false is the default value for boolean)
            return false;
        }

    @Advice.OnMethodExit
    public static void after(@Advice.Return(readOnly = false) String returnValue) {
        // Set fixed return value
        returnValue = "{\"abbreviation\":\"IST\",\"client_ip\":\"1.1.1.1\",\"datetime\":\"2024-06-27T00:52:16.377265+05:30\",\"day_of_week\":4,\"day_of_year\":179,\"dst\":false,\"dst_from\":null,\"dst_offset\":0,\"dst_until\":null,\"raw_offset\":19800,\"timezone\":\"Asia/Kolkata\",\"unixtime\":1719429736,\"utc_datetime\":\"2024-06-26T19:22:16.377265+00:00\",\"utc_offset\":\"+05:30\",\"week_number\":26}";
        System.out.println("Byte buddy Intercept. HardCoded response" + returnValue);
    }
        
    }
}
