package br.com.jovirds.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapper {

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObject(O origen, Class<D> destination) {
        return mapper.map(origen,destination);
    }

    public static <O, D> List<D> parseListObject(List<O> origen, Class<D> destination) {
        return origen.stream().map(origenO -> parseObject(origenO, destination)).collect(Collectors.toList());
    }
}
