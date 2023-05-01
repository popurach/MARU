package com.bird.maru.common.util;

import java.util.Random;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtil {

    private static final Random random = new Random();

    public static int randomInt(int bound) {
        return random.nextInt(bound);
    }

}
