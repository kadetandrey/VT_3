package by.bsuir.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KeyValue<K, V> {

    private K key;
    private V value;
}
