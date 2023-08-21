package backend.codebackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//id 에 해당하는 모집글을 담을 객체를 싱글톤으로 생성
@Getter
@Setter
public class MozipMap {
    private static MozipMap mozipMap = new MozipMap();
    private ConcurrentMap<Long, MozipForm> mozips = new ConcurrentHashMap<>();

    private MozipMap() {}

    public static MozipMap getInstance() {
        return mozipMap;
    }
}
