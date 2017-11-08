package ConvChain;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.function.BiFunction;

public class Generator {
  public static final float DEFAULT_WEIGHT = .01f;
  public static int PATTERNS = 8;

  public static final BiFunction<Integer, Float, Float>
    CLAMP_WEIGHT = new BiFunction<Integer, Float, Float>() {
    @Override public final Float apply(final Integer idx, final Float wgt) {
      return wgt > 0f? wgt : DEFAULT_WEIGHT;
    }
  };

  public final Random rnd = new Random();
  public final Map<Integer, Float> weights = new HashMap<>();
  public final boolean[][] fields;

  public final float t;
  public final int n, w, h;

  public Generator(final boolean[][] samp, 
    final int n$, final float t$, final int w$, final int h$) {
    n = n$;
    t = t$;
    w = w$;
    h = h$;

    final Pattern[] p = new Pattern[PATTERNS];
    final int rows = samp.length, cols = samp[0].length;

    for (int y = 0; y < rows; ++y)  for (int x = 0; x < cols; ++x) {
      p[0] = new Pattern(samp, x, y, n);

      for (int i = 0; i < 3; p[i + 1] = p[i++].rotated());
      for (int i = 0; i < 4; p[i + 4] = p[i++].reflected());

      for (final Pattern k : p)  weights.put(k.index(), 
        1f + weights.getOrDefault(k.index(), 0f));
    }

    weights.replaceAll(CLAMP_WEIGHT);

    for (final boolean[] row : fields = new boolean[h][w])
      for (int x = 0; x < w; row[x++] = rnd.nextBoolean());
  }

  public float energyExp(final int row, final int col) {
    final int yj = col + n, xi = row + n;
    float value = 1f;

    for (int y = col - n + 1; y < yj; ++y)
      for (int x = row - n + 1; x < xi; ++x)
        value *= weights.getOrDefault(
          new Pattern(fields, x, y, n).index(), DEFAULT_WEIGHT);

    return value;
  }

  public void metropolis(final int row, final int col) {
    final float p = energyExp(row, col);
    fields[row][col] ^= true;
    final float q = energyExp(row, col);

    if (Math.pow(q/p, 1f/t) < rnd.nextFloat())  fields[row][col] ^= true;
  }

  public void iterate() {
    for (int i = 0, len = w*h; i < len; ++i)
      metropolis(rnd.nextInt(h), rnd.nextInt(w));
  }
}