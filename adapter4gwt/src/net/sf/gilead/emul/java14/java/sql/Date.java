/*
 * Copyright 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package java.sql;

/**
 * Emulation of the java.sql.Date
 * Based on the source of java.util.Date emulation from GWT
 */
public class Date extends java.util.Date implements Cloneable, Comparable {
  /**
   * Used only by toString().
   */
  private static final String[] DAYS = {
      "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
  };

  /**
   * Used only by toString().
   */
  private static final String[] MONTHS = {
      "Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
  };

  // CHECKSTYLE_OFF: The underscore prefix is an old convention that could be
  // easily replaced.
  public static native long __parse(String s) /*-{
    var d = Date.parse(s);
    return isNaN(d) ? -1 : d;
  }-*/;

  public static long parse(String s) {
    long d = __parse(s);
    if (d != -1) {
      return d;
    } else {
      throw new IllegalArgumentException();
    }
  }

  public Date(int year, int month, int date) {
    init(year, month, date, 0, 0, 0);
  }

  public Date(long date) {
    init(date);
  }

  public boolean after(Date when) {
    return getTime() > when.getTime();
  }

  public boolean before(Date when) {
    return getTime() < when.getTime();
  }

  public Object clone() {
    return new Date(getTime());
  }

  public int compareTo(Date other) {
    long thisTime = getTime();
    long otherTime = other.getTime();
    if (thisTime < otherTime) {
      return -1;
    } else if (thisTime > otherTime) {
      return 1;
    } else {
      return 0;
    }
  }

  public int compareTo(Object other) {
    return compareTo((Date) other);
  }

  /**
   *  Return the names for the days of the week as specified by the Date
   *  specification.
   */
  private static String dayToString(int day) {
    return DAYS[day];
  }

  /**
   *  Return the names for the months of the year as specified by the Date
   *  specification.
   */
  private static String monthToString(int month) {
    return MONTHS[month];
  }

  /**
   *  Ensure a number is displayed with two digits.
   *  @return A two-character representation of the number.
   */
  private static String padTwo(int number) {
    if (number < 10) {
      return "0" + number;
    } else {
      return String.valueOf(number);
    }
  }

  public boolean equals(Object obj) {
    return ((obj instanceof Date) && (getTime() == ((Date) obj).getTime()));
  }

  public native int getDate() /*-{
    return this.jsdate.getDate();
  }-*/;

  public native int getDay() /*-{
    return this.jsdate.getDay();
  }-*/;

  public native int getHours() /*-{
    return this.jsdate.getHours();
  }-*/;

  public native int getMinutes() /*-{
    return this.jsdate.getMinutes();
  }-*/;

  public native int getMonth() /*-{
    return this.jsdate.getMonth();
  }-*/;

  public native int getSeconds() /*-{
    return this.jsdate.getSeconds();
  }-*/;

  public native long getTime() /*-{
    return this.jsdate.getTime();
  }-*/;

  public native int getTimezoneOffset() /*-{
    return this.jsdate.getTimezoneOffset();
  }-*/;

  public native int getYear() /*-{
    return this.jsdate.getFullYear()-1900;
  }-*/;

  public int hashCode() {
    return (int) (this.getTime() ^ (this.getTime() >>> 32));
  }

  public native void setDate(int date) /*-{
    this.jsdate.setDate(date);
  }-*/;

  public native void setHours(int hours) /*-{
    this.jsdate.setHours(hours);
  }-*/;

  public native void setMinutes(int minutes) /*-{
    this.jsdate.setMinutes(minutes);
  }-*/;

  public native void setMonth(int month) /*-{
    this.jsdate.setMonth(month);
  }-*/;

  // CHECKSTYLE_ON

  public native void setSeconds(int seconds) /*-{
    this.jsdate.setSeconds(seconds);
  }-*/;;

  public native void setTime(long time) /*-{
    this.jsdate.setTime(time);
  }-*/;

  // CHECKSTYLE_ON

  public native void setYear(int year) /*-{
    this.jsdate.setFullYear(year + 1900);
  }-*/;

  public native String toGMTString() /*-{
    return this.jsdate.toGMTString();
  }-*/;

  public native String toLocaleString() /*-{
    return this.jsdate.toLocaleString();
  }-*/;

  public native String toString() /*-{
    var d = this.jsdate;
    var padTwo = @java.sql.Date::padTwo(I);
    var day =
        @java.sql.Date::dayToString(I)(this.jsdate.getDay());
    var month =
        @java.sql.Date::monthToString(I)(this.jsdate.getMonth());

    // Compute timezone offset. The value that getTimezoneOffset returns is
    // backwards for the transformation that we want.
    var offset = -d.getTimezoneOffset();
    var hourOffset = String((offset >= 0) ?
        "+" + Math.floor(offset / 60) : Math.ceil(offset / 60));
    var minuteOffset = padTwo(Math.abs(offset) % 60);

    return day + " " + month + " " +
        padTwo(d.getDate()) + " " +
        padTwo(d.getHours()) + ":" +
        padTwo(d.getMinutes()) + ":" +
        padTwo(d.getSeconds()) +
        " GMT" + hourOffset + minuteOffset +
        + " " + d.getFullYear();
  }-*/;

  private native void init(int year, int month, int date, int hrs, int min,
      int sec) /*-{
    this.jsdate = new Date();
    this.jsdate.setFullYear(year + 1900, month, date);
    this.jsdate.setHours(hrs, min, sec, 0);
  }-*/;

  private native void init(long date) /*-{
    this.jsdate = new Date(date);
  }-*/;
}
