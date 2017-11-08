package ConvChain;

import java.util.function.BiPredicate;

public class Pattern implements Cloneable {
  public final boolean[][] data;

  public final BiPredicate<Integer, Integer>
    rotate = new BiPredicate<Integer, Integer>() {
    @Override public final boolean test(final Integer r, final Integer c) {
      return data[c][data[0].length - 1 - r];
    }
  };

  public final BiPredicate<Integer, Integer>
    reflect = new BiPredicate<Integer, Integer>() {
    @Override public final boolean test(final Integer r, final Integer c) {
      return data[r][data[0].length - 1 - c];
    }
  };

  public Pattern(final boolean[][] fields, 
    final int x, final int y, final int n) {
    data = new boolean[n][n];

    for (int r = 0; r < data.length; ++r) {
      final int idxRow = (y + r + fields.length) % fields.length;
      final boolean[] bools = data[r], cols = fields[idxRow];

      for (int c = 0; c < bools.length; ++c) {
        final int idxCol = (x + c + cols.length) % cols.length;
        bools[c] = cols[idxCol];
      }
    }
  }

  public Pattern(final int size, final BiPredicate<Integer, Integer> f) {
    data = new boolean[size][size];
    setBools(f);
  }

  public Pattern rotated() {
    return clone().rotates();
  }

  public Pattern reflected() {
    return clone().reflects();
  }

  public Pattern rotates() {
    return setBools(rotate);
  }

  public Pattern reflects() {
    return setBools(reflect);
  }

  public Pattern setBools(final BiPredicate<Integer, Integer> f) {
    for (int r = 0; r < data.length; ++r) {
      final boolean[] row = data[r];
      final int len = row.length;
      for (int c = 0; c < len; row[c] = f.test(r, c++));
    }

    return this;
  }

  public int index() {
    int result = 0;

    for (final boolean[] row : data)  for (final boolean datum : row) {
      result <<= 1;
      if (datum)  result += 1;
    }

    return result;
  }

  @Override public Pattern clone() {
    try {
      return (Pattern) super.clone();
    }
    catch (final CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override public String toString() {
    return "[" + data.length + "][" + data[0].length + "]";
  }
}