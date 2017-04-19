package com.poseidon.pokers.domain.mapper;

import java.util.Collection;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created by then on 2017/2/6.
 */

public interface Mapper<T, K> extends Function<T, K> {

    List<K> apply(Collection<T> t);
}
