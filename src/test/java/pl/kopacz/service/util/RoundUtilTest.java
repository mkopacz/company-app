package pl.kopacz.service.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class RoundUtilTest {

    @Test
    public void shouldCorrectlyRoundValues() {
        BigDecimal a = new BigDecimal("0.1224");
        BigDecimal b = new BigDecimal("0.1226");
        BigDecimal c = new BigDecimal("0.1249");
        BigDecimal d = new BigDecimal("0.1251");
        BigDecimal e = new BigDecimal("0.1274");
        BigDecimal f = new BigDecimal("0.1276");

        BigDecimal g = new BigDecimal("0.12249");
        BigDecimal h = new BigDecimal("0.12251");
        BigDecimal i = new BigDecimal("0.12499");
        BigDecimal j = new BigDecimal("0.12501");
        BigDecimal k = new BigDecimal("0.12749");
        BigDecimal l = new BigDecimal("0.12751");

        assertThat(RoundUtil.roundToNearest005(a)).isEqualTo("0.12");
        assertThat(RoundUtil.roundToNearest005(b)).isEqualTo("0.125");
        assertThat(RoundUtil.roundToNearest005(c)).isEqualTo("0.125");
        assertThat(RoundUtil.roundToNearest005(d)).isEqualTo("0.125");
        assertThat(RoundUtil.roundToNearest005(e)).isEqualTo("0.125");

        assertThat(RoundUtil.roundToNearest005(g)).isEqualTo("0.12");
        assertThat(RoundUtil.roundToNearest005(h)).isEqualTo("0.125");
        assertThat(RoundUtil.roundToNearest005(i)).isEqualTo("0.125");
        assertThat(RoundUtil.roundToNearest005(j)).isEqualTo("0.125");
        assertThat(RoundUtil.roundToNearest005(k)).isEqualTo("0.125");
        assertThat(RoundUtil.roundToNearest005(l)).isEqualTo("0.13");
    }

}
