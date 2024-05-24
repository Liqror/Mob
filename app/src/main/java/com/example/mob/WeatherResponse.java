package com.example.mob;

public class WeatherResponse {
    public Location location;
    public Current current;

    public class Location {
        public String name;
    }

    public class Current {
        public double temp_c;
        public Condition condition;

        public class Condition {
            public String text;
        }
    }
}
