package com.dians.data_service.datascraper.filters;

import java.io.IOException;
import java.util.Map;

public interface IFilter<K,V> {
  Map<K,V> execute(Map<K,V> map) throws IOException;
}
