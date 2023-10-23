package fr.simnbernard.vae.algorithmes;

import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;

/**
 * https://fr.wikipedia.org/wiki/Algorithme_de_Karatsuba
 */
public class Karatsuba {

  private static final Karatsuba INSTANCE = new Karatsuba();

  static Karatsuba getInstance() {
    return INSTANCE;
  }

  private static final BigInteger MIN_VALUE = BigInteger.TEN;

  private BigInteger tenPow(int e) {
    return BigInteger.TEN.pow(e);
  }

  BigInteger multiplier(final BigInteger x, final BigInteger y) {
    if (x.compareTo(MIN_VALUE) == -1 || y.compareTo(MIN_VALUE) == -1) {
      return x.multiply(y);
    }

    var xS = x.toString();
    var yS = y.toString();

    var maxLength = Math.max(xS.length(), yS.length());

    if (maxLength % 2 != 0) {
      maxLength++;
    }

    xS = StringUtils.leftPad(xS, maxLength, "0");
    yS = StringUtils.leftPad(yS, maxLength, "0");

    final var milieuIdx = maxLength / 2;

    final var a = new BigInteger(xS.substring(0, milieuIdx));
    final var b = new BigInteger(xS.substring(milieuIdx));
    final var c = new BigInteger(yS.substring(0, milieuIdx));
    final var d = new BigInteger(yS.substring(milieuIdx));

    final var ac = multiplier(a, c);
    final var bd = multiplier(b, d);
    final var adPlusBc = multiplier(a.add(b), c.add(d)).subtract(ac).subtract(bd);

    return ac.multiply(tenPow(maxLength)).add(adPlusBc.multiply(tenPow(milieuIdx))).add(bd);
  }

  public static void main(String[] args) {
    final var x = new BigInteger("146123");
    final var y = new BigInteger("352120");

    System.out.println(
        "%d * %d = %d (Karatsuba)".formatted(x, y, Karatsuba.getInstance().multiplier(x, y)));
    System.out.println("%d * %d = %d (Multiply)".formatted(x, y, x.multiply(y)));
  }
}
