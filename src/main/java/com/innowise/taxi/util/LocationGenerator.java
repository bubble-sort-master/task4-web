package com.innowise.taxi.util;

import java.util.Random;

public final class LocationGenerator {
  private static final int MAX_X = 7;
  private static final int MAX_Y = 7;
  private static final Random RANDOM = new Random();

  private LocationGenerator() {
  }

  public static int nextLatitude() {
    return RANDOM.nextInt(MAX_X + 1);
  }

  public static int nextLongitude() {
    return RANDOM.nextInt(MAX_Y + 1);
  }
}
