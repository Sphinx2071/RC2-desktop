package edu.uw.cse.ifrcdemo.setupfun.ui.barcode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.stream.LongStream;

public class Range extends RangeDescriptor {

  @JsonProperty(required = true)
  private long min;

  @JsonProperty(required = true)
  private long max;

  public Range() {
    this.min = -1;
    this.max = -1;
  }

  @JsonCreator
  public Range(@JsonProperty(value = "min", required = true) long min,
               @JsonProperty(value = "max", required = true) long max) {
    this.min = min;
    this.max = max;
  }

  public long getMin() {
    return this.min;
  }

  public void setMin(long min) {
    this.min = min;
  }

  public long getMax() {
    return this.max;
  }

  public void setMax(long max) {
    this.max = max;
  }

  public LongStream toLongStream() {
    return LongStream.rangeClosed(getMin(), getMax());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Range range = (Range) o;

    if (getMin() != range.getMin()) return false;
    return getMax() == range.getMax();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getMin(), getMax());
  }

  @Override
  public String toString() {
    return "Range{" +
        "min=" + min +
        ", max=" + max +
        "}";
  }
}